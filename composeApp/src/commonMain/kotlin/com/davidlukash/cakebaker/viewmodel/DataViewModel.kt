package com.davidlukash.cakebaker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidlukash.cakebaker.data.ConsoleType
import com.davidlukash.cakebaker.data.Item
import com.davidlukash.cakebaker.data.ItemType
import com.davidlukash.cakebaker.data.Log
import com.davidlukash.cakebaker.data.LogType
import com.davidlukash.cakebaker.data.Order
import com.davidlukash.cakebaker.data.OrderCakeSettings
import com.davidlukash.cakebaker.data.OrderFactory
import com.davidlukash.cakebaker.data.Upgrade
import com.davidlukash.cakebaker.globalDecimalMode
import com.davidlukash.cakebaker.mapDouble
import com.davidlukash.cakebaker.toBoolean
import com.davidlukash.cakebaker.ui.navigation.KitchenScreen
import com.davidlukash.cakebaker.weightedRandomInt
import com.davidlukash.cakebaker.withErrorHandling
import com.davidlukash.cakebaker.withErrorHandlingAsync
import com.davidlukash.jsonmath.buildExpressionList
import com.davidlukash.jsonmath.createObject
import com.davidlukash.jsonmath.data.Expression
import com.davidlukash.jsonmath.engine.basic.OriginNode
import com.davidlukash.jsonmath.engine.normal.EnumScopeType
import com.davidlukash.jsonmath.engine.normal.ScopeType
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
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

    init {
        loop()
    }

    private val _allItems = MutableStateFlow(
        listOf(
            Item(
                name = "Butter",
                type = ItemType.INGREDIENT,
                amount = BigDecimal.fromFloat(0.2f),
                price = BigDecimal.fromFloat(250f),
                fastPriceGrowth = true,
                total = BigDecimal.fromFloat(0f),
                increment = BigDecimal.fromInt(1),
                increaseSlope = BigDecimal.fromFloat(0.25f),
                cakePriceAccountability = mapOf(
                    1 to BigDecimal.ZERO,
                    2 to BigDecimal.ZERO,
                    3 to BigDecimal.ZERO,
                ),
                cakePrices = mapOf(
                    1 to BigDecimal.fromFloat(0.2f),
                    2 to BigDecimal.fromFloat(0.4f),
                    3 to BigDecimal.fromFloat(0.8f),
                ),
            ),
            Item(
                name = "Egg",
                type = ItemType.INGREDIENT,
                amount = BigDecimal.fromFloat(4f),
                price = BigDecimal.fromFloat(30f),
                fastPriceGrowth = false,
                total = BigDecimal.fromFloat(0f),
                increment = BigDecimal.fromInt(1),
                increaseSlope = BigDecimal.fromFloat(0.1f),
                cakePriceAccountability = mapOf(
                    1 to BigDecimal.ZERO,
                    2 to BigDecimal.ZERO,
                    3 to BigDecimal.ZERO,
                ),
                cakePrices = mapOf(
                    1 to BigDecimal.fromFloat(4f),
                    2 to BigDecimal.fromFloat(8f),
                    3 to BigDecimal.fromFloat(16f),
                ),
            ),
            Item(
                name = "Flour",
                type = ItemType.INGREDIENT,
                amount = BigDecimal.fromFloat(0.1f),
                price = BigDecimal.fromFloat(400f),
                fastPriceGrowth = true,
                total = BigDecimal.fromFloat(0f),
                increment = BigDecimal.fromInt(1),
                increaseSlope = BigDecimal.fromFloat(0.3f),
                cakePriceAccountability = mapOf(
                    1 to BigDecimal.ZERO,
                    2 to BigDecimal.ZERO,
                    3 to BigDecimal.ZERO,
                ),
                cakePrices = mapOf(
                    1 to BigDecimal.fromFloat(0.1f),
                    2 to BigDecimal.fromFloat(0.2f),
                    3 to BigDecimal.fromFloat(0.4f),
                ),
            ),
            Item(
                name = "Sugar",
                type = ItemType.INGREDIENT,
                amount = BigDecimal.fromFloat(0.2f),
                price = BigDecimal.fromFloat(200f),
                fastPriceGrowth = true,
                total = BigDecimal.fromFloat(0f),
                increment = BigDecimal.fromInt(1),
                increaseSlope = BigDecimal.fromFloat(0.15f),
                cakePriceAccountability = mapOf(
                    1 to BigDecimal.ZERO,
                    2 to BigDecimal.ZERO,
                    3 to BigDecimal.ZERO,
                ),
                cakePrices = mapOf(
                    1 to BigDecimal.fromFloat(0.2f),
                    2 to BigDecimal.fromFloat(0.4f),
                    3 to BigDecimal.fromFloat(0.8f),
                ),
            ),
            Item(
                name = "Vanilla Extract",
                type = ItemType.INGREDIENT,
                amount = BigDecimal.fromFloat(0.5f),
                price = BigDecimal.fromFloat(150f),
                fastPriceGrowth = true,
                total = BigDecimal.fromFloat(0.5f),
                increment = BigDecimal.fromInt(1),
                increaseSlope = BigDecimal.fromFloat(0.4f),
                cakePriceAccountability = mapOf(
                    1 to BigDecimal.ZERO,
                    2 to BigDecimal.ZERO,
                    3 to BigDecimal.ZERO,
                ),
                cakePrices = mapOf(
                    1 to BigDecimal.fromFloat(0.5f),
                    2 to BigDecimal.fromFloat(1f),
                    3 to BigDecimal.fromFloat(2f),
                ),
            ),
            Item(
                name = "Baking Powder",
                type = ItemType.INGREDIENT,
                amount = BigDecimal.fromFloat(0.2f),
                price = BigDecimal.fromFloat(175f),
                fastPriceGrowth = true,
                total = BigDecimal.fromFloat(0.2f),
                increment = BigDecimal.fromInt(1),
                increaseSlope = BigDecimal.fromFloat(0.2f),
                cakePriceAccountability = mapOf(
                    1 to BigDecimal.ZERO,
                    2 to BigDecimal.ZERO,
                    3 to BigDecimal.ZERO,
                ),
                cakePrices = mapOf(
                    1 to BigDecimal.fromFloat(0.2f),
                    2 to BigDecimal.fromFloat(0.4f),
                    3 to BigDecimal.fromFloat(0.8f),
                ),
            ),
            Item(
                name = "Cocoa Powder",
                type = ItemType.INGREDIENT,
                amount = BigDecimal.fromFloat(0f),
                price = BigDecimal.fromFloat(4000f),
                fastPriceGrowth = true,
                total = BigDecimal.fromFloat(0f),
                increment = BigDecimal.fromInt(1),
                increaseSlope = BigDecimal.fromFloat(0.5f),
                cakePriceAccountability = mapOf(
                    1 to BigDecimal.ZERO,
                    2 to BigDecimal.ZERO,
                    3 to BigDecimal.ZERO,
                ),
                cakePrices = mapOf(
                    1 to BigDecimal.fromFloat(0.0f),
                    2 to BigDecimal.fromFloat(0.5f),
                    3 to BigDecimal.fromFloat(0.0f),
                ),
            ),
            Item(
                name = "Honey Pot",
                type = ItemType.INGREDIENT,
                amount = BigDecimal.fromFloat(0f),
                price = BigDecimal.fromFloat(7500f),
                fastPriceGrowth = true,
                total = BigDecimal.fromFloat(0f),
                increment = BigDecimal.fromInt(1),
                increaseSlope = BigDecimal.fromFloat(1f),
                cakePriceAccountability = mapOf(
                    1 to BigDecimal.ZERO,
                    2 to BigDecimal.ZERO,
                    3 to BigDecimal.ZERO,
                ),
                cakePrices = mapOf(
                    1 to BigDecimal.fromFloat(0f),
                    2 to BigDecimal.fromFloat(0f),
                    3 to BigDecimal.fromFloat(1f),
                ),
            ),
            Item(
                name = "Vanilla Cake",
                type = ItemType.CAKE,
                amount = BigDecimal.ZERO,
                cakeTier = 1,
                salePrice = BigDecimal.fromFloat(1300f)
            ),
            Item(
                name = "Chocolate Cake",
                type = ItemType.CAKE,
                amount = BigDecimal.ZERO,
                cakeTier = 2,
                salePrice = BigDecimal.fromFloat(2000f)
            ),
            Item(
                name = "Honey Cake",
                type = ItemType.CAKE,
                amount = BigDecimal.ZERO,
                cakeTier = 3,
                salePrice = BigDecimal.fromFloat(4000f)
            ),
            Item(
                name = "Money",
                type = ItemType.CURRENCY,
                amount = BigDecimal.fromFloat(0f),
            )
        )
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
            return _allItems.value.filter { it.type == ItemType.CAKE && it.cakeTier != null }.associateBy { it.cakeTier!!  }
        }

    val cakesFlow = allItemsFlow.map { items ->
        items.filter { it.type == ItemType.CAKE && it.cakeTier != null }.associateBy { it.cakeTier!!  }
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
        cakes[currentCakeTier - 1]
    }

    private val _upgradesFlow = MutableStateFlow(
        listOf(
            Upgrade(
                pageName = "Oven",
                imageName = "Oven",
                name = "Faster Oven",
                price = 1,
                cakeTier = 1,
                maxLevel = 45,
                onBuy = createLinearOnBuy(),
                parameters = mapOf(
                    "shouldTierChange" to createObject(true),
                    "tierChangeLevel" to createObject(35.toBigDecimal()),
                    "newTier" to createObject(2.toBigDecimal()),
                    "priceIncrement" to createObject(1.toBigDecimal()),
                    "initialPrice" to createObject(1.toBigDecimal()),
                    "levelsUntilPriceIncrease" to createObject(15.toBigDecimal()),
                )
            ),
            Upgrade(
                pageName = "Oven",
                imageName = "Oven",
                name = "Auto Oven",
                price = 3,
                cakeTier = 1,
                maxLevel = 1,
                onBuy = createLinearOnBuy(),
                parameters = mapOf(
                    "shouldTierChange" to createObject(false),
                    "tierChangeLevel" to createObject(1.toBigDecimal()),
                    "newTier" to createObject(1.toBigDecimal()),
                    "priceIncrement" to createObject(1.toBigDecimal()),
                    "initialPrice" to createObject(1.toBigDecimal()),
                    "levelsUntilPriceIncrease" to createObject(1.toBigDecimal()),
                )
            )
        )
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

    val satisfactionLevel = customerSatisfaction.map {
        ceil(it.toDouble() / 20.0).toInt()
    }

    val orderFactory = OrderFactory(this)

    private val _orderCakeSettings = MutableStateFlow(
        mapOf(
            1 to OrderCakeSettings(
                90.0, 45.0,
                1.05, 0.99,
                5,
                35.0, 25.0,
                5, 1,
                -10, -30
            ),
            2 to OrderCakeSettings(
                120.0, 60.0,
                1.2, 0.9,
                3,
                45.0, 30.0,
                15, 10,
                -5, -10
            ),
            3 to OrderCakeSettings(
                150.0, 75.0,
                1.3, 0.8,
                2,
                55.0, 35.0,
                45, 30,
                -1, -5
            )
        )
    )

    val orderCakeSettings = _orderCakeSettings.asStateFlow()

    private val _ordersList = MutableStateFlow(listOf<Order>())
    val ordersList = _ordersList.asStateFlow()

    var random = Random(Random.nextLong())

    /**
     * This is a simple linear price growth. It requires shouldTierChange, tierChangeLevel, newTier, priceIncrement,
     * initialPrice, levelsUntilPriceIncrease in parameters
     */
    private fun createLinearOnBuy(): List<Expression> = buildExpressionList {
        appendFunction {
            name = "control.if"
            appendFunction {
                name = "compare.and"
                appendFunction {
                    name = "compare.equalTo"
                    appendFunction {
                        name = "variable.get"
                        appendFunction {
                            name = "string.join"
                            appendString(".")
                            appendFunction {
                                name = "variable.get"
                                appendString("locals.this")
                            }
                            appendString("level")
                        }
                        appendBoolean(true)
                    }
                    appendFunction {
                        name = "variable.get"
                        appendFunction {
                            name = "string.join"
                            appendString(".")
                            appendFunction {
                                name = "variable.get"
                                appendString("locals.this")
                            }
                            appendString("parameters")
                            appendString("tierChangeLevel")
                        }
                        appendBoolean(true)
                    }
                }
                appendFunction {
                    name = "variable.get"
                    appendFunction {
                        name = "string.join"
                        appendString(".")
                        appendFunction {
                            name = "variable.get"
                            appendString("locals.this")
                        }
                        appendString("parameters")
                        appendString("shouldTierChange")
                    }
                    appendBoolean(true)
                }
            }
            appendList {
                appendFunction {
                    name = "variable.set"
                    appendFunction {
                        name = "string.join"
                        appendString(".")
                        appendFunction {
                            name = "variable.get"
                            appendString("locals.this")
                        }
                        appendString("cakeTier")
                    }
                    appendFunction {
                        name = "variable.get"
                        appendFunction {
                            name = "string.join"
                            appendString(".")
                            appendFunction {
                                name = "variable.get"
                                appendString("locals.this")
                            }
                            appendString("parameters")
                            appendString("newTier")
                        }
                        appendBoolean(true)
                    }
                    appendBoolean(true)
                }
            }
            appendList {
                appendExpression {
                    obj {
                        string("Hello")
                    }
                }
            }
        }
        appendFunction {
            name = "variable.set"
            appendFunction {
                name = "string.join"
                appendString(".")
                appendFunction {
                    name = "variable.get"
                    appendString("locals.this")
                }
                appendString("price")
            }
            appendFunction {
                name = "math.sum"
                appendFunction { 
                    name = "variable.get"
                    appendFunction {
                        name = "string.join"
                        appendString(".")
                        appendFunction {
                            name = "variable.get"
                            appendString("locals.this")
                        }
                        appendString("parameters")
                        appendString("initialPrice")
                    }
                    appendBoolean(true)
                }
                appendFunction {
                    name = "math.product"
                    //locals.this.parameters.priceIncrement
                    appendFunction {
                        name = "variable.get"
                        appendFunction {
                            name = "string.join"
                            appendString(".")
                            appendFunction {
                                name = "variable.get"
                                appendString("locals.this")
                            }
                            appendString("parameters")
                            appendString("priceIncrement")
                        }
                        appendBoolean(true)
                    }
                    appendFunction {
                        name = "math.floor"
                        appendFunction {
                            name = "math.divide"
                            //locals.this.level
                            appendFunction {
                                name = "variable.get"
                                appendFunction {
                                    name = "string.join"
                                    appendString(".")
                                    appendFunction {
                                        name = "variable.get"
                                        appendString("locals.this")
                                    }
                                    appendString("level")
                                }
                                appendBoolean(true)
                            }
                            //locals.this.parameters.levelsUntilPriceIncrease
                            appendFunction {
                                name = "variable.get"
                                appendFunction {
                                    name = "string.join"
                                    appendString(".")
                                    appendFunction {
                                        name = "variable.get"
                                        appendString("locals.this")
                                    }
                                    appendString("parameters")
                                    appendString("levelsUntilPriceIncrease")
                                }
                                appendBoolean(true)
                            }
                        }
                    }
                }
            }
            appendBoolean(true)
        }
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
                        amount = item.amount - (item.cakePrices?.getValue(tempCakeTier) ?: BigDecimal.ZERO)
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
            price = oldPrice.multiply(increaseSlope, globalDecimalMode) + if (fastPriceGrowth) total else BigDecimal.ZERO,
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
    fun loop() {
        viewModelScope.launch {
            var time = Clock.System.now().toEpochMilliseconds()
            while (true) {
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
}