package com.example.orderingfood.model

data class OrderItem(
    val food: Food,
    var quantity: Int
) {
    val subtotal: Double
        get() = food.price * quantity
}