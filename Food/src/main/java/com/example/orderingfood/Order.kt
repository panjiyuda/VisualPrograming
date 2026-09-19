package com.example.orderingfood.model

data class Order(
    val id: Int,
    val customerName: String,
    val items: List<OrderItem>
) {
    val totalPrice: Double
        get() = items.sumOf { it.subtotal }
}