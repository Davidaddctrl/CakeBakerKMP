package com.davidlukash.cakebaker

import com.davidlukash.jsonmath.buildExpressionList
import com.davidlukash.jsonmath.buildFunction
import com.davidlukash.jsonmath.createExpression
import com.davidlukash.jsonmath.data.Expression
import com.davidlukash.jsonmath.data.FunctionCall

object JsonMathHelpers {
    /**
     * This is a simple item cheaper expression list. It requires in parameters itemName (eg. globals.items.Egg), priceDivisor, slopeDivisor
     */
    fun createCheaperItem(): List<Expression> = buildExpressionList {
        //Short term
        appendFunction {
            name = "variable.set"
            appendFunction {
                name = "string.join"
                appendString(".")
                expressions.add(
                    createExpression(createGetDynamic("parameters.itemName"))
                )
                appendString("price")
            }
            appendFunction {
                name = "math.divide"
                appendFunction {
                    name = "variable.get"
                    appendFunction {
                        name = "string.join"
                        appendString(".")
                        expressions.add(
                            createExpression(createGetDynamic("parameters.itemName"))
                        )
                        appendString("price")
                    }
                    appendBoolean(true)
                }
                expressions.add(
                    createExpression(createGetDynamic("parameters.priceDivisor"))
                )
            }
            appendBoolean(true)
        }
        //Long term effects
        appendFunction {
            name = "variable.set"
            appendFunction {
                name = "string.join"
                appendString(".")
                expressions.add(
                    createExpression(createGetDynamic("parameters.itemName"))
                )
                appendString("increaseSlope")
            }
            appendFunction {
                name = "math.divide"
                appendFunction {
                    name = "variable.get"
                    appendFunction {
                        name = "string.join"
                        appendString(".")
                        expressions.add(
                            createExpression(createGetDynamic("parameters.itemName"))
                        )
                        appendString("increaseSlope")
                    }
                    appendBoolean(true)
                }
                expressions.add(
                    createExpression(createGetDynamic("parameters.slopeDivisor"))
                )
            }
            appendBoolean(true)
        }
    }


    /**
     * This creates code where the cake tier required to buy an upgrade changes depending on cakeTiers: Dictionary[Int, Int] in parameters the key should be the level and the value is the tier to change to.
     * */
    fun createChangeableCakeTier(): FunctionCall = buildFunction {
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
            name = "dictionary.getOrDefault"
            expressions.add(
                createExpression(createGetDynamic("parameters.cakeTiers"))
            )
            expressions.add(
                createExpression(createGetDynamic("level"))
            )
            expressions.add(
                createExpression(createGetDynamic("cakeTier"))
            )
        }
        appendBoolean(true)
    }

    /**
     * returns a function call that gets ${get(variableName, global)}.$path
     */
    fun createGetDynamic(
        path: String,
        variableName: String = "locals.this",
        global: Boolean = true
    ): FunctionCall = buildFunction {
        name = "variable.get"
        appendFunction {
            name = "string.join"
            appendString(".")
            appendFunction {
                name = "variable.get"
                appendString(variableName)
            }
            appendString(path)
        }
        appendBoolean(global)
    }

    /**
     * This is a simple linear price growth. It also includes changeable cake tier. It requires cakeTiers: Dictionary[Int, Int], priceIncrement,
     * initialPrice, levelsUntilPriceIncrease in parameters
     */
    fun createLinearGrowth(): List<Expression> = buildExpressionList {
        expressions.add(
            createExpression(createChangeableCakeTier())
        )
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

    /**
     * This is a simple script that requires 2 parameters: variable: String, argument: Any? in parameters. It simply sets the variable to the variable <operation> product
     */
    fun createOperation(operation: String): List<Expression> = buildExpressionList {
        appendFunction {
            name = "variable.set"
            expressions.add(
                createExpression(createGetDynamic("parameters.variable"))
            )
            appendFunction {
                name = operation
                appendFunction {
                    name = "variable.get"
                    expressions.add(
                        createExpression(createGetDynamic("parameters.variable"))
                    )
                    appendBoolean(true)
                }
                expressions.add(
                    createExpression(createGetDynamic("parameters.argument"))
                )
            }
            appendBoolean(true)
        }
    }

    /**
     *  This is a script that is used for dense item upgrades. It sets each cake price to the cake price -1
     */
    fun createDense(): List<Expression> = buildExpressionList {
        appendFunction {
            name = "variable.set"
            appendFunction {
                name = "string.join"
                appendString(".")
                expressions.add(
                    createExpression(createGetDynamic("parameters.itemName"))
                )
                appendString("cakePrices")
            }
            appendFunction {
                name = "dictionary.construct"
                appendFunction {
                    name = "dictionary.keys"
                    appendFunction {
                        name = "variable.get"
                        appendFunction {
                            name = "string.join"
                            appendString(".")
                            expressions.add(
                                createExpression(createGetDynamic("parameters.itemName"))
                            )
                            appendString("cakePrices")
                        }
                        appendBoolean(true)
                    }
                }
                appendFunction {
                    name = "control.forEach"
                    appendString("locals.value")
                    appendFunction {
                        name = "dictionary.values"
                        appendFunction {
                            name = "variable.get"
                            appendFunction {
                                name = "string.join"
                                appendString(".")
                                expressions.add(
                                    createExpression(createGetDynamic("parameters.itemName"))
                                )
                                appendString("cakePrices")
                            }
                            appendBoolean(true)
                        }
                    }
                    appendList {
                        appendFunction {
                            name = "math.max"
                            appendFunction {
                                name = "control.if"
                                appendFunction {
                                    name = "compare.greaterThan"
                                    appendFunction {
                                        name = "variable.get"
                                        appendString("locals.value")
                                    }
                                    // >
                                    appendNumber("1")
                                }
                                appendList {
                                    appendExpression { function {
                                        name = "math.subtract"
                                        appendFunction {
                                            name = "variable.get"
                                            appendString("locals.value")
                                        }
                                        appendNumber("1")
                                    } }
                                }
                                appendList {
                                    appendExpression { function {
                                        name = "math.subtract"
                                        appendFunction {
                                            name = "variable.get"
                                            appendString("locals.value")
                                        }
                                        appendNumber("0.1")
                                    } }
                                }
                            }
                            appendNumber("0.1")
                        }
                    }
                }
            }
            appendBoolean(true)
        }
    }
}