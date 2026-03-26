package com.litvy.litvysales.ui.purchases.shoppingList

import com.litvy.litvysales.ui.purchases.shoppingList.components.ShoppingListItem

data class ShoppingListUiState(
    val isLoading: Boolean = false,
    val search: String = "",
    val items: List<ShoppingListItem> = emptyList(),

    val groupedByBrand: Map<String, List<ShoppingListItem>> = emptyMap(),

    val groupedByProvider: Map<String, Map<String, List<ShoppingListItem>>> = emptyMap()
)