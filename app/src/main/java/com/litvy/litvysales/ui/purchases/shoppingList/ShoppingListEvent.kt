package com.litvy.litvysales.ui.purchases.shoppingList

sealed class ShoppingListEvent {
    data class OnSearchChange(val value: String): ShoppingListEvent()
    object OnBack: ShoppingListEvent()

    data class OnSelectBrand(val brand: String): ShoppingListEvent()
    data class OnCreateOrderFromBrand(val brand: String): ShoppingListEvent()
}