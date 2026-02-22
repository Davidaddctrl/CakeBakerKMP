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
}