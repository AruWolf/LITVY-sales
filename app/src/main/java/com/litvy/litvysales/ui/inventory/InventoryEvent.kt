package com.litvy.litvysales.ui.inventory

sealed class InventoryEvent {

    data class SearchQueryChanged( val query: String ) : InventoryEvent()

    data class ProductExpanded( val productId: Int ): InventoryEvent()

    data class ProductCollapsed( val productId: Int): InventoryEvent()

    object RefreshInventory : InventoryEvent()

    object LoadInventory : InventoryEvent()

    object OpenStockMovements : InventoryEvent()

    object OpenInventoryProducts : InventoryEvent()
}