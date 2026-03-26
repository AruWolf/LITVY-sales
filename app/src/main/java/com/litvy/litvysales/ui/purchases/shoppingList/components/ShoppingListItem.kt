package com.litvy.litvysales.ui.purchases.shoppingList.components

data class ShoppingListItem(
    val id: Int,
    val name: String,
    val currentStock: Double,
    val suggestedQuantity: Double,
    val provider: String?,
    val brand: String
)
