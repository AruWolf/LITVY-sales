package com.litvy.litvysales.ui.catalog.builder

import com.litvy.litvysales.ui.catalog.model.ProductFormState
import com.litvy.litvysales.ui.catalog.util.CatalogLevel
import com.litvy.litvysales.ui.catalog.util.CatalogState

fun buildEditFormState(
    id: Int,
    state: CatalogState
): ProductFormState {

    return when(state.level){

        CatalogLevel.CATEGORIES -> {

            val item = state.categories.first { it.id == id }

            ProductFormState(
                name = item.name
            )
        }

        CatalogLevel.SUBCATEGORIES -> {

            val item = state.subCategories.first { it.id == id }

            ProductFormState(
                name = item.name
            )
        }

        CatalogLevel.BRANDS -> {

            val item = state.brands.first { it.id == id }

            ProductFormState(
                name = item.name
            )
        }

        CatalogLevel.PRODUCTS -> {

            val item = state.products.first { it.id == id }

            ProductFormState(
                name = item.name,
                purchase = (item.purchasePriceInCents / 100.0).toString(),
                sale = (item.salePriceInCents / 100.0).toString(),
                hasExpiration = item.hasExpiration,
                isWeighable = item.isWeighable
            )
        }

    }

}