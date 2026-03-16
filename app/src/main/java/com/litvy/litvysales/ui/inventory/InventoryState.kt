package com.litvy.litvysales.ui.inventory

import com.litvy.litvysales.ui.inventory.model.InventoryProductItem
import com.litvy.litvysales.ui.inventory.model.InventoryScreens
import com.litvy.litvysales.ui.inventory.model.StockBatchItem

data class InventoryState(

    val products: List<InventoryProductItem> = emptyList(),
    val expandedProducts: Set<Int> = emptySet(),

    val isLoading: Boolean = false,

    val currentScreen: InventoryScreens = InventoryScreens.PRODUCTS,

    val searchQuery: String = "",

    val batchesByProduct: Map<Int, List<StockBatchItem>> = emptyMap(),

    // Navegación
    val selectedCategoryId: Int? = null,
    val selectedSubCategoryId: Int? = null,
    val selectedBrandId: Int? = null,
    val selectedProductId: Int? = null

    )