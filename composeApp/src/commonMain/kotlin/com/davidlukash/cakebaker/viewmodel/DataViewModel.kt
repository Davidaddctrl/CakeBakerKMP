package com.davidlukash.cakebaker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidlukash.cakebaker.VERSION
import com.davidlukash.cakebaker.VERSIONCODE
import com.davidlukash.cakebaker.data.item.Item
import com.davidlukash.cakebaker.data.item.ItemType
import com.davidlukash.cakebaker.data.order.Order
import com.davidlukash.cakebaker.data.order.OrderCakeSettings
import com.davidlukash.cakebaker.data.order.OrderFactory
import com.davidlukash.cakebaker.data.save.Save
import com.davidlukash.cakebaker.data.UIState
import com.davidlukash.cakebaker.data.Upgrade
import com.davidlukash.cakebaker.engine.CakeBakerEngine
import com.davidlukash.cakebaker.engine.CakeBakerScope
import com.davidlukash.cakebaker.globalDecimalMode
import com.davidlukash.cakebaker.logger
import com.davidlukash.cakebaker.mapDouble
import com.davidlukash.cakebaker.mapDoubleBiased
import com.davidlukash.cakebaker.takeOrDefaultWithWarn
import com.davidlukash.cakebaker.takeOrNullWithWarn
import com.davidlukash.cakebaker.toBoolean
import com.davidlukash.cakebaker.ui.navigation.KitchenScreen
import com.davidlukash.cakebaker.ui.navigation.SaveScreen
import com.davidlukash.cakebaker.weightedRandomInt
import com.davidlukash.cakebaker.withResult
import com.davidlukash.cakebaker.withResultSuspend
import com.davidlukash.jsonmath.createObject
import com.davidlukash.jsonmath.engine.basic.OriginNode
import com.davidlukash.jsonmath.engine.normal.EnumScopeType
import com.davidlukash.jsonmath.engine.normal.ScopeType
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.collections.listOf
import kotlin.random.Random
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi

class DataViewModel(
    val uiViewModel: UIViewModel,
    val saveFileViewModel: SaveFileViewModel,
    val engine: CakeBakerEngine
) : ViewModel() {
    val globalScope = CakeBakerScope(ScopeType(EnumScopeType.GLOBAL), this)

    var loopJob: Job? = null

    var random = Random(Random.nextLong())

    init {
        startLoop()
    }

    var shouldListSaves = true

    @OptIn(ExperimentalTime::class)
    fun startLoop() {
        if (loopJob?.isActive == true) return
        loopJob = viewModelScope.launch {
            var time = Clock.System.now().toEpochMilliseconds()
            while (isActive) {
                withResultSuspend {
                    val newTime = Clock.System.now().toEpochMilliseconds()
                    val dt = newTime - time
                    time = newTime
                    tickOven(dt)
                    tickOrderCounterCreate()
                    tickOrderCreate(dt)
                    tickOrderCounterCreate()
                    tickOrder(dt)
                    tickAutoOven()
                    tickAutoOrderComplete()
                    if (uiViewModel.currentScreen.value == SaveScreen) {
                        if (shouldListSaves) {
                            saveFileViewModel.listSavesSuspend().onFailure {
                                shouldListSaves = false
                                uiViewModel.addTextButtonPopup("Error Listing Saves", false, "Retry") {
                                    shouldListSaves = true
                                }
                            }
                        }
                    }
                    delay(100)
                }.onFailure {
                    stopLoop()
                    uiViewModel.addTextButtonPopup("The main loop has failed", false, "Retry") {
                        startLoop()
                    }
                }
            }
        }
    }

    fun stopLoop() {
        loopJob?.cancel()
        loopJob = null
    }

    private val _ovenProgress = MutableStateFlow(0.0)
    private val _ovenRunning = MutableStateFlow(false)

    val ovenProgress = _ovenProgress.asStateFlow()
    val ovenRunning = _ovenRunning.asStateFlow()

    private var tempCakeTier: Int = 1

    suspend fun tickOven(dt: Long) {
        val ovenRunning = ovenRunning.value
        val fasterOven = upgrades.value.find { it.name == "Faster Oven" }
        val level = fasterOven?.level ?: 0
        if (ovenRunning) {
            val speed = 5000.0 - level.toDouble() * 100.0
            _ovenProgress.emit(ovenProgress.value + dt.toDouble() / speed)
            if (_ovenProgress.value >= 1) {
                _ovenRunning.emit(false)
                _ovenProgress.emit(0.0)
                val cake =
                    cakes[tempCakeTier].takeOrNullWithWarn("Cake with tier $tempCakeTier does not exist")
                if (cake == null) {
                    uiViewModel.addTextPopup("Failed to bake cake")
                    return
                }
                updateItem(
                    cake.copy(
                        amount = cake.amount + 1
                    )
                )
            }
        }
    }

    private val _autoOvenEnabled = MutableStateFlow(false)
    val autoOvenEnabled = _autoOvenEnabled.asStateFlow()

    fun tickAutoOven() {
        val ovenRunning = ovenRunning.value
        val autoOven = upgrades.value.find { it.name == "Auto Oven" }?.level?.toBoolean() ?: false
        val autoOvenEnabled = autoOvenEnabled.value
        if (!ovenRunning && canBake(currentCakeTier.value) && autoOven && autoOvenEnabled) {
            startBake()
        }
    }

    private val _autoOrderCompleteEnabled = MutableStateFlow(false)
    val autoOrderCompleteEnabled = _autoOrderCompleteEnabled.asStateFlow()

    fun tickAutoOrderComplete() {
        val autoOrderComplete =
            upgrades.value.find { it.name == "Auto Order Complete" }?.level?.toBoolean() ?: false
        val autoOrderCompleteEnabled = autoOrderCompleteEnabled.value
        val orders = orders.value
        if (autoOrderComplete && autoOrderCompleteEnabled) {
            orders.forEach { order ->
                val cake =
                    cakes[order.cakeTier].takeOrNullWithWarn("Cake with tier ${order.cakeTier} does not exist")
                        ?: return@forEach
                if (cake.amount >= order.amount) {
                    handleCompleteOrder(order)
                }
            }
        }
    }

    private val _items = MutableStateFlow(
        listOf<Item>()
    )

    val items = _items.asStateFlow()

    val ingredients: List<Item>
        get() {
            return _items.value.filter { it.type == ItemType.INGREDIENT }
        }
    val cakes: Map<Int, Item>
        get() {
            return _items.value.filter { it.type == ItemType.CAKE && it.cakeTier != null }
                .associateBy { it.cakeTier!! }
        }

    val money: Item
        get() {
            return _items.value.find { it.name == "Money" } ?: Item("Money", ItemType.CURRENCY, BigDecimal.ZERO)
        }

    private val _currentCakeTier = MutableStateFlow(1)
    val currentCakeTier = _currentCakeTier.asStateFlow()

    private val _upgrades = MutableStateFlow(
        listOf<Upgrade>()
    )

    val upgrades = _upgrades.asStateFlow()

    //1 to 100
    private val _customerSatisfaction = MutableStateFlow(80)
    val customerSatisfaction = _customerSatisfaction.asStateFlow()

    private val _orderCakeSettings = MutableStateFlow(
        mapOf<Int, OrderCakeSettings>()
    )

    val orderCakeSettings = _orderCakeSettings.asStateFlow()

    private val _orders = MutableStateFlow(listOf<Order>())
    val orders = _orders.asStateFlow()

    private val _orderCakeTimeCounters = MutableStateFlow(mapOf<Int, Double>())

    val nextOrderRemainingTime = _orderCakeTimeCounters.map { if (it.values.isEmpty()) null else it.values.min() }

    val orderFactory = OrderFactory { uiViewModel.addTextPopup(it) }

    suspend fun tickOrderCounterCreate() {
        val nextTier = orderFactory.selectCakeTier(
            cakes,
            customerSatisfaction.value,
            random,
            _orderCakeTimeCounters.value.keys.toList()
        )
        nextTier?.let { nextTier ->
            val settings = orderCakeSettings.value[nextTier] ?: return@let
            val weight = mapDoubleBiased(customerSatisfaction.value.toDouble(), 1.0, 100.0, 0.5, 2.5, -1.5)
            val waitTime = mapDouble(
                weightedRandomInt(weight, 10001, random).toDouble(),
                0.0,
                10000.0,
                settings.waitTimeMax,
                settings.waitTimeMin,
            )
            _orderCakeTimeCounters.emit(_orderCakeTimeCounters.value + (nextTier to waitTime))
        }
    }

    suspend fun tickOrderCreate(dt: Long) {
        _orderCakeTimeCounters.emit(
            _orderCakeTimeCounters.value.mapNotNull { entry ->
                val cakeTier = entry.key
                val remainingTime = entry.value
                val nextRemainingTime = remainingTime - dt.toDouble() / 1000.0
                if (nextRemainingTime <= 0) {
                    val order = orderFactory.createOrder(
                        cakeTier,
                        cakes,
                        customerSatisfaction.value,
                        random,
                        orderCakeSettings.value,
                        calculateCakePrice(cakeTier)
                    )
                    if (order == null) {
                        uiViewModel.addTextPopup("Failed to create order, deleting timer")
                        return@mapNotNull null
                    }
                    val cake =
                        cakes[cakeTier].takeOrNullWithWarn("Cake with tier $cakeTier does not exist")
                    if (cake == null) {
                        uiViewModel.addTextPopup("Failed to create order, deleting timer")
                        return@mapNotNull null
                    }
                    if (uiViewModel.currentScreen.value != KitchenScreen)
                        uiViewModel.addTextPopup("New Order for ${order.amount} ${cake.name}")
                    _orders.emit(
                        _orders.value + order
                    )
                    return@mapNotNull null
                }
                cakeTier to nextRemainingTime
            }.toMap()
        )
    }

    suspend fun tickOrder(dt: Long) {
        val seconds = dt.toDouble() / 1000.0
        _orders.emit(
            _orders.value.mapNotNull {
                val new = it.copy(
                    remainingTime = it.remainingTime - seconds
                )
                if (new.remainingTime <= 0.0) {
                    handleFailedOrder(new)
                    return@mapNotNull null
                }
                new
            }
        )
    }

    suspend fun handleFailedOrder(order: Order) {
        val settings = orderCakeSettings.value[order.cakeTier].takeOrNullWithWarn("Order cake settings with tier ${order.cakeTier} does not exist")
        if (settings == null) {
            uiViewModel.addTextPopup("Failed to fail order, deleting order")
            viewModelScope.launch {
                _orders.emit(
                    _orders.value.filter { it.id != order.id }
                )
            }
            return
        }
        val modifier = mapDouble(
            weightedRandomInt(1.0, 10001, random).toDouble(),
            0.0,
            10000.0,
            settings.minUnfulfilledCustomerSatisfaction.toDouble(),
            settings.maxUnfulfilledCustomerSatisfaction.toDouble()
        ).toInt()
        _customerSatisfaction.emit(
            maxOf(1, _customerSatisfaction.value + modifier)
        )
    }

    fun handleCompleteOrder(order: Order) {
        val settings = orderCakeSettings.value[order.cakeTier].takeOrNullWithWarn("Order cake settings with tier ${order.cakeTier} does not exist")
        if (settings == null) {
            uiViewModel.addTextPopup("Failed to complete order, deleting order")
            viewModelScope.launch {
                _orders.emit(
                    _orders.value.filter { it.id != order.id }
                )
            }
            return
        }
        val weight = mapDouble(order.remainingTime / order.totalTime, 0.0, 1.0, 0.5, 2.0)
        val modifier = mapDouble(
            weightedRandomInt(weight, 10001, random).toDouble(),
            0.0,
            10000.0,
            settings.minFulfilledCustomerSatisfaction.toDouble(),
            settings.maxFulfilledCustomerSatisfaction.toDouble()
        ).toInt()
        var cake = cakes[order.cakeTier].takeOrNullWithWarn("Cake with tier ${order.cakeTier} does not exist, cancelling order completion")
        if (cake == null) {
            uiViewModel.addTextPopup("Order with id ${order.id} has an invalid cake tier. It has been deleted")
            viewModelScope.launch {
                _orders.emit(
                    _orders.value.filter { it.id != order.id }
                )
            }
            return
        }
        cake = cake.copy(
            amount = cake.amount - order.amount.toBigDecimal()
        )
        updateItem(cake)
        updateItem(
            money.copy(
                amount = money.amount + order.salePrice
            )
        )
        viewModelScope.launch {
            _orders.emit(
                _orders.value.filter { it.id != order.id }
            )
            _customerSatisfaction.emit(
                minOf(100, _customerSatisfaction.value + modifier * order.amount)
            )
        }
    }

    fun setOrders(orders: List<Order>) {
        viewModelScope.launch {
            _orders.emit(orders)
        }
    }

    fun canBake(tier: Int): Boolean {
        ingredients.forEach { ingredient ->
            val cakePrices =
                ingredient.cakePrices.takeOrNullWithWarn("Ingredient ${ingredient.name} does not have cake prices, skipping iteration")
                    ?: return@forEach
            val cakePrice =
                cakePrices[tier].takeOrNullWithWarn("Ingredient ${ingredient.name} cake price for cake tier $tier does not exist, skipping iteration")
                    ?: return@forEach

            if (ingredient.amount < cakePrice) return false
        }
        return true
    }

    fun updateOrderSettings(tier: Int, settings: OrderCakeSettings) {
        viewModelScope.launch {
            _orderCakeSettings.emit(
                _orderCakeSettings.value + (tier to settings)
            )
        }
    }

    fun setAutoOvenEnabled(enabled: Boolean) {
        viewModelScope.launch {
            _autoOvenEnabled.emit(enabled)
        }
    }

    fun setAutoOrderCompleteEnabled(enabled: Boolean) {
        viewModelScope.launch {
            _autoOrderCompleteEnabled.emit(enabled)
        }
    }

    fun setCurrentCake(tier: Int) {
        viewModelScope.launch {
            _currentCakeTier.emit(tier)
        }
    }

    fun updateItem(item: Item) {
        viewModelScope.launch {
            var hasMatch = false
            _items.emit(
                _items.value.map {
                    if (it.name == item.name) {
                        hasMatch = true
                        item
                    } else it
                }
            )
            if (!hasMatch) {
                logger.logWarn("Item with name ${item.name} does not exist")
            }
        }
    }

    fun setItems(items: List<Item>) {
        viewModelScope.launch {
            _items.emit(items)
        }
    }

    fun updateUpgrade(upgrade: Upgrade) {
        viewModelScope.launch {
            var hasMatch = false
            _upgrades.emit(
                _upgrades.value.map {
                    if (it.name == upgrade.name) {
                        hasMatch = true
                        upgrade
                    } else it
                }
            )
            if (!hasMatch) {
                logger.logWarn("Upgrade with name ${upgrade.name} does not exist")
            }
        }
    }

    fun setUpgrades(upgrades: List<Upgrade>) {
        viewModelScope.launch {
            _upgrades.emit(upgrades)
        }
    }

    fun calculateCakePrice(tier: Int): BigDecimal {
        val cake = cakes[tier]
        if (cake == null) {
            logger.logWarn("Cake with tier $tier does not exist, defaulting to zero")
            return BigDecimal.ZERO
        }
        return calculateCakePrice(cake, ingredients)
    }

    fun calculateCakePrice(cake: Item, ingredients: List<Item>): BigDecimal {
        var cakePrice = cake.salePrice.takeOrDefaultWithWarn(
            "Cake ${cake.name} does not have salePrice, defaulting to zero",
            BigDecimal.ZERO
        )
        ingredients.forEach { ingredient ->
            cakePrice += ingredient.cakePriceAccountability?.get(cake.cakeTier ?: 1) ?: BigDecimal.ZERO
        }
        return cakePrice
    }

    fun startBake() {
        viewModelScope.launch {
            tempCakeTier = currentCakeTier.value
            ingredients.forEach { ingredient ->
                val cakePrices = ingredient.cakePrices
                if (cakePrices == null) {
                    logger.logWarn("Ingredient ${ingredient.name} does not have cake prices, skipping iteration")
                    return@forEach
                }
                val cakePrice = cakePrices[tempCakeTier]
                if (cakePrice == null) {
                    logger.logWarn("Ingredient ${ingredient.name} cake price for cake tier $tempCakeTier does not exist, skipping iteration")
                    return@forEach
                }
                updateItem(
                    ingredient.copy(
                        amount = ingredient.amount.subtract(cakePrice, globalDecimalMode)
                    )
                )
            }
            _ovenRunning.emit(true)
        }
    }

    fun buyIngredient(name: String) {
        val ingredient = ingredients.find { it.name == name }
            .takeOrNullWithWarn("Ingredient $name does not exist, cancelling ingredient buy")
        if (ingredient == null) {
            uiViewModel.addTextPopup("Ingredient $name")
            return
        }
        var tempIngredient = ingredient
        if (money.amount < (ingredient.price.takeOrDefaultWithWarn(
                "Ingredient $name does not have price, defaulting to zero",
                BigDecimal.ZERO
            ))
        ) {
            uiViewModel.addTextPopup("You do not have enough money to buy $name")
            return
        }
        updateItem(
            money.copy(
                amount = money.amount - ingredient.price.takeOrDefaultWithWarn(
                    "Ingredient $name does not have price, defaulting to zero",
                    BigDecimal.ZERO
                )
            )
        )
        val oldPrice = tempIngredient.price.takeOrDefaultWithWarn(
            "Ingredient $name does not have price, defaulting to zero",
            BigDecimal.ZERO
        )
        val increment = tempIngredient.increment.takeOrDefaultWithWarn(
            "Ingredient $name does not have increment, defaulting to zero",
            BigDecimal.ZERO
        )
        val total = (tempIngredient.total.takeOrDefaultWithWarn(
            "Ingredient $name does not have total, defaulting to zero",
            BigDecimal.ZERO
        )) + increment
        val increaseSlope = (tempIngredient.increaseSlope.takeOrDefaultWithWarn(
            "Ingredient $name does not have increaseSlope, defaulting to zero",
            BigDecimal.ZERO
        )) + BigDecimal.ONE
        val fastPriceGrowth = tempIngredient.fastPriceGrowth.takeOrDefaultWithWarn(
            "Ingredient $name does not have fastPriceGrowth, defaulting to false",
            false
        )
        val cakePriceAccountability = tempIngredient.cakePriceAccountability.takeOrDefaultWithWarn(
            "Ingredient $name does not have cakePriceAccountability, defaulting to emptyMap()",
            emptyMap()
        ).toMutableMap()
        tempIngredient = tempIngredient.copy(
            price = oldPrice.multiply(
                increaseSlope,
            ).roundSignificand(globalDecimalMode) + if (fastPriceGrowth) total else BigDecimal.ZERO,
            total = total,
            amount = tempIngredient.amount + increment
        )

        val price = tempIngredient.price.takeOrDefaultWithWarn(
            "Ingredient $name does not have price, defaulting to zero",
            BigDecimal.ZERO
        )
        val maxTier = cakePriceAccountability.keys.maxOrNull().takeOrDefaultWithWarn(
            "Ingredient $name does not have cakePriceAccountability, defaulting to emptyMap()",
            1
        )
        val minTier = cakePriceAccountability.keys.minOrNull().takeOrDefaultWithWarn(
            "Ingredient $name does not have cakePriceAccountability, defaulting to emptyMap()",
            1
        )
        val tiers = cakePriceAccountability.keys.size
        val denominator = (tiers + 1).toBigDecimal()
        //Low tier cakes get a smaller price accountabiliy than higher tier
        val factors = (minTier..maxTier).associateWith { tier -> //1: 2/4 2: 3/4 3: 4/4
            val numerator = (tier + 1).toBigDecimal()
            numerator.divide(
                denominator,
                decimalMode = globalDecimalMode,
            )
        }
        factors.forEach { (tier, factor) ->
            val oldAccountability = cakePriceAccountability[tier].takeOrDefaultWithWarn(
                "Ingredient $name does not have cakePriceAccountability for tier $tier, defaulting to zero",
                BigDecimal.ZERO
            )
            val difference = ((price - oldPrice) / 2)
            cakePriceAccountability[tier] = oldAccountability + factor.multiply(difference, globalDecimalMode)
        }

        tempIngredient = tempIngredient.copy(
            cakePriceAccountability = cakePriceAccountability.toMap()
        )

        updateItem(
            tempIngredient
        )
    }

    @OptIn(ExperimentalUuidApi::class)
    fun buyUpgrade(upgrade: Upgrade) {
        val snapshot = createSave()
        val cake = cakes[upgrade.cakeTier].takeOrNullWithWarn(
            "Cake with tier ${upgrade.cakeTier} does not exist, returning and notifying the user"
        )
        if (cake == null) {
            uiViewModel.addTextPopup("Upgrade \"${upgrade.name}\" is linked to an invalid cake. It was not bought")
            return
        }
        updateUpgrade(
            upgrade.copy(
                level = upgrade.level + 1,
            )
        )
        updateItem(
            cake.copy(
                amount = cake.amount - upgrade.price.toBigDecimal()
            )
        )
        val localScope = CakeBakerScope(ScopeType(EnumScopeType.LOCAL), this)
        localScope.setVariable("locals.this", createObject("globals.upgrades.${upgrade.name}"))
        val origin = OriginNode("Upgrade On Buy", upgrade.onBuy)
        upgrade.onBuy.forEach { expression ->
            logger.logDebug(expression.toString())
        }
        val result = withResult {
            engine.evaluateExpressions(upgrade.onBuy, listOf(globalScope, localScope), listOf(origin))
        }
        result.onSuccess { obj ->
            logger.logDebug("Result: $obj")
        }
        result.onFailure {
            uiViewModel.addTextPopup("When buying upgrade ${upgrade.name} an error occurred. No cakes were deducted")
            loadSave(snapshot)
        }
    }

    fun loadSave(save: Save) {
        viewModelScope.launch {
            _items.emit(save.items)
            _currentCakeTier.emit(save.currentCakeTier)
            _upgrades.emit(save.upgrades)
            _ovenProgress.emit(save.ovenProgress)
            _ovenRunning.emit(save.ovenRunning)
            _autoOvenEnabled.emit(save.autoOvenEnabled)
            _autoOrderCompleteEnabled.emit(save.autoOrderCompleteEnabled)
            tempCakeTier = save.tempCakeTier
            _customerSatisfaction.emit(save.customerSatisfaction)
            _orderCakeSettings.emit(save.orderCakeSettings)
            _orders.emit(save.orders)
            _orderCakeTimeCounters.emit(save.orderCakeTimeCounters)
        }
    }

    fun createSave(): Save = Save(
        version = VERSION,
        versionCode = VERSIONCODE,
        _items.value,
        _currentCakeTier.value,
        _upgrades.value,
        _ovenProgress.value,
        _ovenRunning.value,
        _autoOvenEnabled.value,
        _autoOrderCompleteEnabled.value,
        tempCakeTier,
        _customerSatisfaction.value,
        _orderCakeSettings.value,
        _orders.value,
        _orderCakeTimeCounters.value
    )

    val uiStateFlow =
        combine(
            items,
            currentCakeTier,
            upgrades,
            autoOvenEnabled,
            autoOrderCompleteEnabled,
            customerSatisfaction,
        ) { list ->
            val items = list[0] as List<Item>
            val currentCakeTier = list[1] as Int
            val upgrades = list[2] as List<Upgrade>
            val autoOvenEnabled = list[3] as Boolean
            val autoOrderCompleteEnabled = list[4] as Boolean
            val customerSatisfaction = list[5] as Int
            val canBake = canBake(currentCakeTier)
            UIState(
                items = items,
                currentCakeTier = currentCakeTier,
                upgrades = upgrades,
                autoOvenEnabled = autoOvenEnabled,
                autoOrderCompleteEnabled = autoOrderCompleteEnabled,
                customerSatisfaction = customerSatisfaction,
                canBake = canBake,
            )
        }
}
