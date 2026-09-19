package com.example.orderingfood.manager

import com.example.orderingfood.model.Food
import com.example.orderingfood.model.Order
import com.example.orderingfood.model.OrderItem

class RestaurantManager {

    private val menuList = mutableListOf<Food>()
    private val orderList = mutableListOf<Order>()

    private var nextFoodId = 1
    private var nextOrderId = 1

    // =========================
    // MENU
    // =========================

    fun getMenu(): List<Food> {
        return menuList
    }

    fun addFood(
        name: String,
        description: String,
        price: Double
    ): Boolean {

        if (name.isBlank()) return false
        if (description.isBlank()) return false
        if (price <= 0) return false

        val food = Food(
            id = nextFoodId++,
            name = name,
            description = description,
            price = price
        )

        menuList.add(food)

        return true
    }

    fun editFood(
        id: Int,
        name: String,
        description: String,
        price: Double
    ): Boolean {

        if (name.isBlank()) return false
        if (description.isBlank()) return false
        if (price <= 0) return false

        val food = menuList.find { it.id == id }
            ?: return false

        food.name = name
        food.description = description
        food.price = price

        return true
    }

    fun deleteFood(id: Int): Boolean {

        val food = menuList.find { it.id == id }
            ?: return false

        menuList.remove(food)

        return true
    }

    // =========================
    // ORDER
    // =========================

    fun getOrders(): List<Order> {
        return orderList
    }

    fun createOrder(
        customerName: String,
        items: List<OrderItem>
    ): Boolean {

        if (customerName.isBlank()) return false
        if (items.isEmpty()) return false

        val order = Order(
            id = nextOrderId++,
            customerName = customerName,
            items = items
        )

        orderList.add(order)

        return true
    }
}