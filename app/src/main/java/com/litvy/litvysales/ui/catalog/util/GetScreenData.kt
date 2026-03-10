package com.litvy.litvysales.ui.catalog.util

import com.litvy.litvysales.ui.catalog.util.CatalogEvent
import com.litvy.litvysales.ui.catalog.util.CatalogState

fun getTitle(level: CatalogLevel): String {

    return when(level){

        CatalogLevel.CATEGORIES -> "Categorías"

        CatalogLevel.SUBCATEGORIES -> "Subcategorías"

        CatalogLevel.BRANDS -> "Marcas"

        CatalogLevel.PRODUCTS -> "Productos"

    }

}

fun getItems(state: CatalogState): List<Pair<Int,String>> {

    return when(state.level){

        CatalogLevel.CATEGORIES ->
            state.categories.map { it.id!! to it.name }

        CatalogLevel.SUBCATEGORIES ->
            state.subCategories.map { it.id!! to it.name }

        CatalogLevel.BRANDS ->
            state.brands.map { it.id!! to it.name }

        else -> emptyList()

    }

}

fun getSelectEvent(level: CatalogLevel, id: Int): CatalogEvent {

    return when(level){

        CatalogLevel.CATEGORIES ->
            CatalogEvent.SelectCategory(id)

        CatalogLevel.SUBCATEGORIES ->
            CatalogEvent.SelectSubCategory(id)

        CatalogLevel.BRANDS ->
            CatalogEvent.SelectBrand(id)

        else -> throw IllegalStateException()

    }

}