package com.davidlukash.cakebaker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidlukash.cakebaker.data.Item
import com.davidlukash.cakebaker.data.ItemType
import com.davidlukash.cakebaker.data.Log
import com.davidlukash.cakebaker.data.LogType
import com.davidlukash.cakebaker.data.Order
import com.davidlukash.cakebaker.data.OrderCakeSettings
import com.davidlukash.cakebaker.data.OrderFactory
import com.davidlukash.cakebaker.data.Save
import com.davidlukash.cakebaker.data.UIState
import com.davidlukash.cakebaker.data.Upgrade
import com.davidlukash.cakebaker.globalDecimalMode
import com.davidlukash.cakebaker.mapDouble
import com.davidlukash.cakebaker.toBoolean
import com.davidlukash.cakebaker.ui.navigation.KitchenScreen
import com.davidlukash.cakebaker.weightedRandomInt
import com.davidlukash.cakebaker.withErrorHandling
import com.davidlukash.cakebaker.withErrorHandlingAsync
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
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.collections.listOf
import kotlin.math.ceil
import kotlin.random.Random
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi

class DataViewModel(
    val uiViewModel: UIViewModel,
    val engine: CakeBakerEngine
) : ViewModel() {
    val globalScope = CakeBakerScope(ScopeType(EnumScopeType.GLOBAL), this)

    var loopJob: Job? = null

    init {
        startLoop()
    }

    private val _allItems = MutableStateFlow(
        listOf<Item>()
    )

    val allItemsFlow = _allItems.asStateFlow()

    val ingredientsFlow = allItemsFlow.map { items ->
        items.filter { it.type == ItemType.INGREDIENT }
    }

    val ingredients: List<Item>
        get() {
            return _allItems.value.filter { it.type == ItemType.INGREDIENT }
        }
    val cakes: Map<Int, Item>
        get() {
            return _allItems.value.filter { it.type == ItemType.CAKE && it.cakeTier != null }
                .associateBy { it.cakeTier!! }
        }

    val cakesFlow = allItemsFlow.map { items ->
        items.filter { it.type == ItemType.CAKE && it.cakeTier != null }.associateBy { it.cakeTier!! }
    }

    val cakePricesFlow = combine(cakesFlow, ingredientsFlow) { cakes: Map<Int, Item>, ingredients: List<Item> ->
        cakes.map { (tier, cake) ->
            tier to calculateCakePrice(cake, ingredients)
        }.toMap()
    }

    val itemMoney: Item
        get() {
            return _allItems.value.find { it.name == "Money" } ?: Item("Money", ItemType.CURRENCY, BigDecimal.ZERO)
        }

    val itemMoneyFlow = allItemsFlow.map { items ->
        items.find { it.name == "Money" } ?: Item("Money", ItemType.CURRENCY, BigDecimal.ZERO)
    }

    private val _currentCakeTier = MutableStateFlow(1)
    val currentCakeTier = _currentCakeTier.asStateFlow()

    val currentCake = combine(currentCakeTier, cakesFlow) { currentCakeTier: Int, cakes: Map<Int, Item> ->
        cakes[currentCakeTier]
    }

    private val _upgradesFlow = MutableStateFlow(
        listOf<Upgrade>()
    )

    val upgradesFlow = _upgradesFlow.asStateFlow()

    private val _ovenProgress = MutableStateFlow(0.0)
    private val _ovenRunning = MutableStateFlow(false)

    val ovenProgress = _ovenProgress.asStateFlow()
    val ovenRunning = _ovenRunning.asStateFlow()

    val canBakeFlow = ingredientsFlow.combine(currentCakeTier) { ingredients, currentCakeTier ->
        ingredients.forEach { ingredient ->
            if (ingredient.amount < (ingredient.cakePrices?.get(currentCakeTier) ?: 0)) return@combine false
        }
        true
    }

    fun canBake(tier: Int): Boolean {
        ingredients.forEach { ingredient ->
            if (ingredient.amount < (ingredient.cakePrices?.get(tier) ?: 0)) return false
        }
        return true
    }

    private val _autoOvenEnabled = MutableStateFlow(false)
    val autoOvenEnabled = _autoOvenEnabled.asStateFlow()

    private var tempCakeTier: Int = 1

    //1 to 100
    private val _customerSatisfaction = MutableStateFlow(80)
    val customerSatisfaction = _customerSatisfaction.asStateFlow()


    val orderFactory = OrderFactory(this)

    private val _orderCakeSettings = MutableStateFlow(
        mapOf<Int, OrderCakeSettings>()
    )

    val orderCakeSettings = _orderCakeSettings.asStateFlow()

    private val _ordersList = MutableStateFlow(listOf<Order>())
    val ordersList = _ordersList.asStateFlow()

    var random = Random(Random.nextLong())


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

    fun setCurrentCake(tier: Int) {
        viewModelScope.launch {
            _currentCakeTier.emit(tier)
        }
    }

    fun updateItem(item: Item) {
        viewModelScope.launch {
            _allItems.emit(
                _allItems.value.map {
                    if (it.name == item.name) item else it
                }
            )
        }
    }

    fun setItems(items: List<Item>) {
        viewModelScope.launch {
            _allItems.emit(items)
        }
    }

    fun updateUpgrade(upgrade: Upgrade) {
        viewModelScope.launch {
            _upgradesFlow.emit(
                _upgradesFlow.value.map {
                    if (it.name == upgrade.name) upgrade else it
                }
            )
        }
    }

    fun setUpgrades(upgrades: List<Upgrade>) {
        viewModelScope.launch {
            _upgradesFlow.emit(upgrades)
        }
    }

    fun calculateCakePrice(tier: Int): BigDecimal {
        val cake = cakes[tier]
            ?: throw IllegalArgumentException("Cake with tier $tier does not exist")
        return calculateCakePrice(cake, ingredients)
    }

    fun calculateCakePrice(cake: Item, ingredients: List<Item>): BigDecimal {
        var cakePrice = cake.salePrice ?: BigDecimal.ZERO
        ingredients.forEach { ingredient ->
            cakePrice += ingredient.cakePriceAccountability?.get(cake.cakeTier ?: 1) ?: BigDecimal.ZERO
        }
        return cakePrice
    }

    fun bake() {
        viewModelScope.launch {
            tempCakeTier = currentCakeTier.value
            ingredients.forEach { item ->
                updateItem(
                    item.copy(
                        amount = item.amount - (item.cakePrices?.get(tempCakeTier) ?: BigDecimal.ZERO)
                    )
                )
            }
            _ovenRunning.emit(true)
        }
    }

    fun buyIngredient(name: String) {
        val item = ingredients.find { it.name == name }
            ?: throw IllegalArgumentException("Ingredient $name does not exist")
        var tempItem = item
        updateItem(
            itemMoney.copy(
                amount = itemMoney.amount - (item.price ?: BigDecimal.ZERO)
            )
        )
        val oldPrice = tempItem.price ?: BigDecimal.ZERO
        val increment = tempItem.increment ?: BigDecimal.ZERO
        val total = (tempItem.total ?: BigDecimal.ZERO) + increment
        val increaseSlope = (tempItem.increaseSlope ?: BigDecimal.ZERO) + BigDecimal.ONE
        val fastPriceGrowth = tempItem.fastPriceGrowth ?: false
        val cakePriceAccountability = (tempItem.cakePriceAccountability ?: emptyMap()).toMutableMap()
        tempItem = tempItem.copy(
            price = oldPrice.multiply(
                increaseSlope,
                globalDecimalMode
            ) + if (fastPriceGrowth) total else BigDecimal.ZERO,
            total = total,
            amount = tempItem.amount + increment
        )

        val price = tempItem.price ?: BigDecimal.ZERO
        val maxTier = cakePriceAccountability.keys.maxOrNull() ?: 1
        val minTier = cakePriceAccountability.keys.minOrNull() ?: 1
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
            val oldAccountability = cakePriceAccountability[tier] ?: BigDecimal.ZERO
            val difference = ((price - oldPrice) / 2)
            cakePriceAccountability[tier] = oldAccountability + factor * difference
        }

        tempItem = tempItem.copy(
            cakePriceAccountability = cakePriceAccountability.toMap()
        )

        updateItem(
            tempItem
        )

    }

    @OptIn(ExperimentalTime::class)
    fun startLoop() {
        if (loopJob?.isActive == true) return
        loopJob = viewModelScope.launch {
            var time = Clock.System.now().toEpochMilliseconds()
            while (isActive) {
                withErrorHandlingAsync {
                    delay(100)
                    val newTime = Clock.System.now().toEpochMilliseconds()
                    val dt = newTime - time
                    time = newTime
                    tickOven(dt)
                    tickOrderCreate(dt)
                    tickOrder(dt)
                    tickAutoOven()
                }
            }
        }
    }

    fun stopLoop() {
        loopJob?.cancel()
        loopJob = null
    }

    suspend fun tickOven(dt: Long) {
        val ovenRunning = ovenRunning.value
        val fasterOven = upgradesFlow.value.find { it.name == "Faster Oven" }
        val level = fasterOven?.level ?: 0
        if (ovenRunning) {
            val speed = 5000.0 - level.toDouble() * 100.0
            _ovenProgress.emit(ovenProgress.value + dt.toDouble() / speed)
            if (_ovenProgress.value >= 1) {
                _ovenRunning.emit(false)
                _ovenProgress.emit(0.0)
                val cake = cakes[tempCakeTier]
                    ?: throw IllegalArgumentException("Cake with tier $tempCakeTier does not exist")
                updateItem(
                    cake.copy(
                        amount = cake.amount + 1
                    )
                )
            }
        }
    }

    fun tickAutoOven() {
        val ovenRunning = ovenRunning.value
        val autoOven = upgradesFlow.value.find { it.name == "Auto Oven" }?.level?.toBoolean() ?: false
        val autoOvenEnabled = autoOvenEnabled.value
        if (!ovenRunning && canBake(currentCakeTier.value) && autoOven && autoOvenEnabled) {
            bake()
        }
    }

    private val _orderCakeTimeCounters = MutableStateFlow(mapOf<Int, Double>())

    val nextOrderRemainingTime = _orderCakeTimeCounters.map { if (it.values.isEmpty()) null else it.values.min() }

    suspend fun tickOrderCreate(dt: Long) {
        val nextTier = orderFactory.selectCakeTier(_orderCakeTimeCounters.value.keys.toList())
        nextTier?.let { nextTier ->
            val settings = orderCakeSettings.value[nextTier] ?: return@let
            val weight = mapDouble(customerSatisfaction.value.toDouble(), 1.0, 100.0, 0.5, 2.5)
            val waitTime = mapDouble(
                weightedRandomInt(weight, 10001, random).toDouble(),
                0.0,
                10000.0,
                settings.waitTimeMax,
                settings.waitTimeMin,
            )
            _orderCakeTimeCounters.emit(_orderCakeTimeCounters.value + (nextTier to waitTime))
        }
        _orderCakeTimeCounters.emit(
            _orderCakeTimeCounters.value.mapNotNull { entry ->
                val cakeTier = entry.key
                val remainingTime = entry.value
                val nextRemainingTime = remainingTime - dt.toDouble() / 1000.0
                if (nextRemainingTime <= 0) {
                    val order = orderFactory.createOrder(cakeTier).copy(
                        id = random.nextInt(10000, 99999)
                    )
                    val cake = cakes[cakeTier]
                        ?: throw IllegalArgumentException("Cake with tier $cakeTier does not exist")
                    if (uiViewModel.currentScreen.value != KitchenScreen)
                        uiViewModel.addTextPopup("New Order for ${order.amount} ${cake.name}")
                    _ordersList.emit(
                        _ordersList.value + order
                    )
                    return@mapNotNull null
                }
                cakeTier to nextRemainingTime
            }.toMap()
        )
    }

    suspend fun tickOrder(dt: Long) {
        val seconds = dt.toDouble() / 1000.0
        _ordersList.emit(
            _ordersList.value.mapNotNull {
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
        val settings = orderCakeSettings.value[order.cakeTier] ?: return
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
        withErrorHandling {
            val settings = orderCakeSettings.value[order.cakeTier]
                ?: throw IllegalArgumentException("Order Cake Settings with tier ${order.cakeTier} does not exist")
            val weight = mapDouble(order.remainingTime / order.totalTime, 0.0, 1.0, 0.5, 2.0)
            val modifier = mapDouble(
                weightedRandomInt(weight, 10001, random).toDouble(),
                0.0,
                10000.0,
                settings.minFulfilledCustomerSatisfaction.toDouble(),
                settings.maxFulfilledCustomerSatisfaction.toDouble()
            ).toInt()
            var cake = cakes[order.cakeTier]
                ?: throw IllegalArgumentException("Cake with tier ${order.cakeTier} does not exist")
            cake = cake.copy(
                amount = cake.amount - order.amount.toBigDecimal()
            )
            updateItem(cake)
            updateItem(
                itemMoney.copy(
                    amount = itemMoney.amount + order.salePrice
                )
            )
            viewModelScope.launch {
                _ordersList.emit(
                    _ordersList.value.filter { it.id != order.id }
                )
                _customerSatisfaction.emit(
                    minOf(100, _customerSatisfaction.value + modifier * order.amount)
                )
            }
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    fun buyUpgrade(upgrade: Upgrade) {
        withErrorHandling {
            updateUpgrade(
                upgrade.copy(
                    level = upgrade.level + 1,
                )
            )
            val cake = cakes[upgrade.cakeTier]
                ?: throw IllegalArgumentException("Cake with tier ${upgrade.cakeTier} does not exist")
            updateItem(
                cake.copy(
                    amount = cake.amount - upgrade.price.toBigDecimal()
                )
            )
            val localScope = CakeBakerScope(ScopeType(EnumScopeType.LOCAL), this)
            localScope.setVariable("locals.this", createObject("globals.upgrades.${upgrade.name}"))
            val origin = OriginNode("Upgrade On Buy", upgrade.onBuy)
            upgrade.onBuy.forEach { expression ->
                uiViewModel.appendLog(Log(expression.toString(), LogType.MESSAGE))
            }
            val result = engine.evaluateExpressions(upgrade.onBuy, listOf(globalScope, localScope), listOf(origin))
            uiViewModel.appendLog(Log(result.toString(), LogType.RESULT))
        }
    }

    fun loadSave(save: Save) {
        viewModelScope.launch {
            _allItems.emit(save.items)
            _currentCakeTier.emit(save.currentCakeTier)
            _upgradesFlow.emit(save.upgrades)
            _ovenProgress.emit(save.ovenProgress)
            _ovenRunning.emit(save.ovenRunning)
            _autoOvenEnabled.emit(save.autoOvenEnabled)
            tempCakeTier = save.tempCakeTier
            _customerSatisfaction.emit(save.customerSatisfaction)
            _orderCakeSettings.emit(save.orderCakeSettings)
            _ordersList.emit(save.orders)
            _orderCakeTimeCounters.emit(save.orderCakeTimeCounters)
        }
    }

    fun createSave(): Save = Save(
        _allItems.value,
        _currentCakeTier.value,
        _upgradesFlow.value,
        _ovenProgress.value,
        _ovenRunning.value,
        _autoOvenEnabled.value,
        tempCakeTier,
        _customerSatisfaction.value,
        _orderCakeSettings.value,
        _ordersList.value,
        _orderCakeTimeCounters.value
    )

    val uiStateFlow =
        combine(
            allItemsFlow,
            currentCakeTier,
            upgradesFlow,
            ovenProgress,
            ovenRunning,
            autoOvenEnabled,
            customerSatisfaction,
            ordersList,
            nextOrderRemainingTime,
        ) { list ->
            val items = list[0] as List<Item>
            val currentCakeTier = list[1] as Int
            val upgrades = list[2] as List<Upgrade>
            val ovenProgress = list[3] as Double
            val ovenRunning = list[4] as Boolean
            val autoOvenEnabled = list[5] as Boolean
            val customerSatisfaction = list[6] as Int
            val orders = list[7] as List<Order>
            val nextOrderRemainingTime = list[8] as? Double
            val canBake = canBake(currentCakeTier)
            UIState(
                items = items,
                currentCakeTier = currentCakeTier,
                upgrades = upgrades,
                ovenProgress = ovenProgress,
                ovenRunning = ovenRunning,
                autoOvenEnabled = autoOvenEnabled,
                customerSatisfaction = customerSatisfaction,
                orders = orders,
                nextOrderRemainingTime = nextOrderRemainingTime,
                canBake = canBake,
            )
        }
}