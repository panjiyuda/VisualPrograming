package com.example.orderingfood

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.orderingfood.manager.RestaurantManager
import com.example.orderingfood.model.Food
import com.example.orderingfood.model.OrderItem
import java.util.Locale

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                OrderingFoodApp()
            }
        }
    }
}

@Composable
fun OrderingFoodApp() {

    val manager = remember {
        RestaurantManager().apply {

            addFood(
                "Nasi Goreng",
                "Nasi goreng spesial dengan telur",
                25000.0
            )

            addFood(
                "Mie Goreng",
                "Mie goreng dengan sayuran",
                20000.0
            )

            addFood(
                "Ayam Goreng",
                "Ayam goreng crispy",
                30000.0
            )
        }
    }

    var currentScreen by remember {
        mutableStateOf("home")
    }

    var selectedFood by remember {
        mutableStateOf<Food?>(null)
    }

    var refresh by remember {
        mutableStateOf(false)
    }

    when (currentScreen) {

        "home" -> {
            HomeScreen(
                onMakeOrder = {
                    currentScreen = "order"
                },
                onViewOrders = {
                    currentScreen = "orders"
                },
                onViewMenu = {
                    currentScreen = "menu"
                },
                onAddMenu = {
                    selectedFood = null
                    currentScreen = "add"
                }
            )
        }

        "menu" -> {
            MenuScreen(
                foods = manager.getMenu(),
                onBack = {
                    currentScreen = "home"
                },
                onEdit = { food ->
                    selectedFood = food
                    currentScreen = "edit"
                },
                onDelete = { food ->
                    manager.deleteFood(food.id)
                    refresh = !refresh
                },
                onAdd = {
                    selectedFood = null
                    currentScreen = "add"
                }
            )
        }

        "add" -> {
            AddMenuScreen(
                onBack = {
                    currentScreen = "menu"
                },
                onSave = { name, description, price ->

                    val success = manager.addFood(
                        name,
                        description,
                        price
                    )

                    if (success) {
                        refresh = !refresh
                        currentScreen = "menu"
                    }

                    success
                }
            )
        }

        "edit" -> {

            selectedFood?.let { food ->

                EditMenuScreen(
                    food = food,
                    onBack = {
                        currentScreen = "menu"
                    },
                    onSave = { name, description, price ->

                        val success = manager.editFood(
                            food.id,
                            name,
                            description,
                            price
                        )

                        if (success) {
                            refresh = !refresh
                            currentScreen = "menu"
                        }

                        success
                    }
                )
            }
        }

        "order" -> {
            MakeOrderScreen(
                foods = manager.getMenu(),
                onBack = {
                    currentScreen = "home"
                },
                onOrderComplete = { customerName, items ->

                    val success = manager.createOrder(
                        customerName,
                        items
                    )

                    if (success) {
                        currentScreen = "home"
                    }

                    success
                }
            )
        }

        "orders" -> {
            OrdersScreen(
                orders = manager.getOrders(),
                onBack = {
                    currentScreen = "home"
                }
            )
        }
    }
}
@Composable
fun HomeScreen(
    onMakeOrder: () -> Unit,
    onViewOrders: () -> Unit,
    onViewMenu: () -> Unit,
    onAddMenu: () -> Unit
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("ORDER SYSTEM")
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text(
                text = "Restaurant Ordering System",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onMakeOrder
            ) {
                Text("Make Order")
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onViewOrders
            ) {
                Text("View Orders")
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onViewMenu
            ) {
                Text("View Menu")
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onAddMenu
            ) {
                Text("Add Menu")
            }
        }
    }
}
@Composable
fun MenuScreen(
    foods: List<Food>,
    onBack: () -> Unit,
    onEdit: (Food) -> Unit,
    onDelete: (Food) -> Unit,
    onAdd: () -> Unit
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Menu")
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onAdd
            ) {
                Text("Add Menu")
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            if (foods.isEmpty()) {

                Text("No menu items available.")

            } else {

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    items(foods) { food ->

                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {

                                Text(
                                    text = food.name,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(
                                    text = food.description
                                )

                                Text(
                                    text = formatPrice(food.price)
                                )

                                Spacer(
                                    modifier = Modifier.height(10.dp)
                                )

                                Row(
                                    horizontalArrangement =
                                        Arrangement.spacedBy(8.dp)
                                ) {

                                    Button(
                                        onClick = {
                                            onEdit(food)
                                        }
                                    ) {
                                        Text("Edit")
                                    }

                                    Button(
                                        onClick = {
                                            onDelete(food)
                                        }
                                    ) {
                                        Text("Delete")
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = onBack
            ) {
                Text("Back")
            }
        }
    }
}
@Composable
fun AddMenuScreen(
    onBack: () -> Unit,
    onSave: (String, String, Double) -> Boolean
) {

    var name by remember {
        mutableStateOf("")
    }

    var description by remember {
        mutableStateOf("")
    }

    var price by remember {
        mutableStateOf("")
    }

    var errorMessage by remember {
        mutableStateOf("")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Add Menu")
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = name,
                onValueChange = {
                    name = it
                },
                label = {
                    Text("Food Name")
                }
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = description,
                onValueChange = {
                    description = it
                },
                label = {
                    Text("Description")
                }
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = price,
                onValueChange = {
                    price = it
                },
                label = {
                    Text("Price")
                }
            )

            if (errorMessage.isNotEmpty()) {

                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {

                    if (name.isBlank()) {
                        errorMessage =
                            "Food name cannot be empty."
                        return@Button
                    }

                    if (description.isBlank()) {
                        errorMessage =
                            "Description cannot be empty."
                        return@Button
                    }

                    val priceValue =
                        price.toDoubleOrNull()

                    if (priceValue == null) {
                        errorMessage =
                            "Please enter a valid price."
                        return@Button
                    }

                    if (priceValue <= 0) {
                        errorMessage =
                            "Price must be greater than 0."
                        return@Button
                    }

                    val success = onSave(
                        name,
                        description,
                        priceValue
                    )

                    if (!success) {
                        errorMessage =
                            "Failed to add menu."
                    }
                }
            ) {
                Text("Add Menu")
            }

            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = onBack
            ) {
                Text("Cancel")
            }
        }
    }
}
@Composable
fun EditMenuScreen(
    food: Food,
    onBack: () -> Unit,
    onSave: (String, String, Double) -> Boolean
) {

    var name by remember {
        mutableStateOf(food.name)
    }

    var description by remember {
        mutableStateOf(food.description)
    }

    var price by remember {
        mutableStateOf(food.price.toString())
    }

    var errorMessage by remember {
        mutableStateOf("")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Edit Menu")
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = name,
                onValueChange = {
                    name = it
                },
                label = {
                    Text("Food Name")
                }
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = description,
                onValueChange = {
                    description = it
                },
                label = {
                    Text("Description")
                }
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = price,
                onValueChange = {
                    price = it
                },
                label = {
                    Text("Price")
                }
            )

            if (errorMessage.isNotEmpty()) {

                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {

                    if (name.isBlank()) {
                        errorMessage =
                            "Food name cannot be empty."
                        return@Button
                    }

                    if (description.isBlank()) {
                        errorMessage =
                            "Description cannot be empty."
                        return@Button
                    }

                    val priceValue =
                        price.toDoubleOrNull()

                    if (priceValue == null) {
                        errorMessage =
                            "Please enter a valid price."
                        return@Button
                    }

                    if (priceValue <= 0) {
                        errorMessage =
                            "Price must be greater than 0."
                        return@Button
                    }

                    val success = onSave(
                        name,
                        description,
                        priceValue
                    )

                    if (!success) {
                        errorMessage =
                            "Failed to update menu."
                    }
                }
            ) {
                Text("Save Changes")
            }

            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = onBack
            ) {
                Text("Cancel")
            }
        }
    }
}
@Composable
fun MakeOrderScreen(
    foods: List<Food>,
    onBack: () -> Unit,
    onOrderComplete: (
        String,
        List<OrderItem>
    ) -> Boolean
) {

    var customerName by remember {
        mutableStateOf("")
    }

    val orderItems = remember {
        mutableStateListOf<OrderItem>()
    }

    var selectedFood by remember {
        mutableStateOf<Food?>(null)
    }

    var quantity by remember {
        mutableStateOf("1")
    }

    var errorMessage by remember {
        mutableStateOf("")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Make Order")
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = customerName,
                onValueChange = {
                    customerName = it
                },
                label = {
                    Text("Customer Name")
                }
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Text(
                text = "Select Food",
                fontWeight = FontWeight.Bold
            )

            foods.forEach { food ->

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                    onClick = {
                        selectedFood = food
                    }
                ) {
                    Text(
                        "${food.name} - " +
                                formatPrice(food.price)
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            selectedFood?.let { food ->

                Text(
                    text = "Selected: ${food.name}"
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = quantity,
                    onValueChange = {
                        quantity = it
                    },
                    label = {
                        Text("Quantity")
                    }
                )

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {

                        val quantityValue =
                            quantity.toIntOrNull()

                        if (quantityValue == null ||
                            quantityValue <= 0
                        ) {

                            errorMessage =
                                "Quantity must be at least 1."

                            return@Button
                        }

                        val existing =
                            orderItems.find {
                                it.food.id == food.id
                            }

                        if (existing != null) {

                            existing.quantity +=
                                quantityValue

                        } else {

                            orderItems.add(
                                OrderItem(
                                    food = food,
                                    quantity = quantityValue
                                )
                            )
                        }

                        selectedFood = null
                        quantity = "1"
                        errorMessage = ""
                    }
                ) {
                    Text("Add Item")
                }
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Text(
                text = "Current Order",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            orderItems.forEach { item ->

                Text(
                    "${item.food.name} x${item.quantity} = " +
                            formatPrice(item.subtotal)
                )
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            val total =
                orderItems.sumOf { it.subtotal }

            Text(
                text = "TOTAL: ${formatPrice(total)}",
                fontWeight = FontWeight.Bold
            )

            if (errorMessage.isNotEmpty()) {

                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {

                    if (customerName.isBlank()) {

                        errorMessage =
                            "Customer name cannot be empty."

                        return@Button
                    }

                    if (orderItems.isEmpty()) {

                        errorMessage =
                            "Please add at least one item."

                        return@Button
                    }

                    val success =
                        onOrderComplete(
                            customerName,
                            orderItems.toList()
                        )

                    if (!success) {

                        errorMessage =
                            "Failed to create order."
                    }
                }
            ) {
                Text("Place Order")
            }

            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = onBack
            ) {
                Text("Back")
            }
        }
    }
}

@Composable
fun OrdersScreen(
    orders: List<com.example.orderingfood.model.Order>,
    onBack: () -> Unit
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("View Orders")
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            if (orders.isEmpty()) {

                Text("No orders have been made yet.")

            } else {

                LazyColumn(
                    verticalArrangement =
                        Arrangement.spacedBy(10.dp)
                ) {

                    items(orders) { order ->

                        Card(
                            modifier =
                                Modifier.fillMaxWidth()
                        ) {

                            Column(
                                modifier =
                                    Modifier.padding(16.dp)
                            ) {

                                Text(
                                    text =
                                        "Order #${order.id}",
                                    fontWeight =
                                        FontWeight.Bold
                                )

                                Text(
                                    "Customer: " +
                                            order.customerName
                                )

                                Spacer(
                                    modifier =
                                        Modifier.height(8.dp)
                                )

                                order.items.forEach { item ->

                                    Text(
                                        "${item.food.name} " +
                                                "x${item.quantity} " +
                                                "= " +
                                                formatPrice(
                                                    item.subtotal
                                                )
                                    )
                                }

                                Spacer(
                                    modifier =
                                        Modifier.height(8.dp)
                                )

                                Text(
                                    text =
                                        "TOTAL: " +
                                                formatPrice(
                                                    order.totalPrice
                                                ),
                                    fontWeight =
                                        FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            Spacer(
                modifier =
                    Modifier.height(10.dp)
            )

            OutlinedButton(
                modifier =
                    Modifier.fillMaxWidth(),
                onClick = onBack
            ) {
                Text("Back")
            }
        }
    }
}

fun formatPrice(price: Double): String {

    return String.format(
        Locale.US,
        "Rp%,.0f",
        price
    ).replace(",", ".")
}


