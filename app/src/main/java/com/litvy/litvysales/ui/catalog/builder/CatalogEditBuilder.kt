package com.litvy.litvysales.ui.catalog.builder

import com.litvy.litvysales.ui.catalog.model.ProductFormState
import com.litvy.litvysales.ui.catalog.util.CatalogLevel
import com.litvy.litvysales.ui.catalog.util.CatalogState

// Metodo constructor de elementos necesarios para el formulario de edición,
// dependiendo del nivel en el que se genere el mismo
fun buildEditFormState(
    id: Int,
    state: CatalogState
): ProductFormState {

    return when(state.level){

        // NIVEL CATEGORIA
        CatalogLevel.CATEGORIES -> {

            // Asignación de id de la categoria seleccionada
            val item = state.categories.first { it.id == id }

            // Se puede modificar el nombre
            ProductFormState(
                name = item.name
            )
        }

        // NIVEL SUBCATEGORIA
        CatalogLevel.SUBCATEGORIES -> {

            // Asignación de id de la subCategoria seleccionada
            val item = state.subCategories.first { it.id == id }

            // Se puede modificar el nombre
            ProductFormState(
                name = item.name
            )
        }

        // NIVEL MARCA
        CatalogLevel.BRANDS -> {

            // Asignación de id de la marca seleccionada
            val item = state.brands.first { it.id == id }

            // Se puede modificar el nombre
            ProductFormState(
                name = item.name
            )
        }

        // NIVEL PRODUCTO
        CatalogLevel.PRODUCTS -> {

            // Asignación de id del producto seleccionado
            val item = state.products.first { it.id == id }

            // Se puede modificar el...
            ProductFormState(
                name = item.name, // Nombre
                purchase = (item.purchasePriceInCents / 100.0).toString(), // Precio de compra
                sale = (item.salePriceInCents / 100.0).toString(), // Precio de venta
                hasExpiration = item.hasExpiration, // Si tiene vencimiento o no
                isWeighable = item.isWeighable // Si se comercializa por peso.
            )
        }

    }

}