package com.davidlukash.cakebaker.data

import com.davidlukash.cakebaker.data.item.Item
import com.davidlukash.cakebaker.data.order.Order
import com.davidlukash.cakebaker.viewmodel.DataViewModel

interface DataActions {
    fun getUpgrades(): List<Upgrade>

    fun updateUpgrade(upgrade: Upgrade)

    fun setUpgrades(upgrades: List<Upgrade>)

    fun getItems(): List<Item>

    fun updateItem(item: Item)

    fun setItems(items: List<Item>)

    fun getOrders(): List<Order>

    fun updateOrder(order: Order)

    fun updateOrderAtIndex(order: Order, index: Int)

    fun setOrders(orders: List<Order>)

    companion object {
        fun fromDataViewModel(dataViewModel: DataViewModel) = object : DataActions {
            override fun getUpgrades(): List<Upgrade> {
                return dataViewModel.upgrades.value
            }

            override fun updateUpgrade(upgrade: Upgrade) {
                dataViewModel.updateUpgrade(upgrade)
            }

            override fun setUpgrades(upgrades: List<Upgrade>) {
                dataViewModel.setUpgrades(upgrades)
            }

            override fun getItems(): List<Item> {
                return dataViewModel.items.value
            }

            override fun updateItem(item: Item) {
                dataViewModel.updateItem(item)
            }

            override fun setItems(items: List<Item>) {
                dataViewModel.setItems(items)
            }

            override fun getOrders(): List<Order> {
                return dataViewModel.orders.value
            }

            override fun updateOrder(order: Order) {
                dataViewModel.updateOrder(order)
            }

            override fun updateOrderAtIndex(order: Order, index: Int) {
                dataViewModel.updateOrderAtIndex(order, index)
            }

            override fun setOrders(orders: List<Order>) {
               dataViewModel.setOrders(orders)
            }
        }
    }
}