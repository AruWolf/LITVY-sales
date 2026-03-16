package com.litvy.litvysales.ui.catalog.builder

import com.litvy.litvysales.ui.catalog.util.CatalogEvent
import com.litvy.litvysales.ui.catalog.util.CatalogLevel

// Constructor de eventos de almacenamiento
fun buildCatalogSaveEvent(

    level: CatalogLevel,
    editingId: Int?,

    name: String,
    purchase: String,
    sale: String,
    hasExpiration: Boolean,
    isWeighable: Boolean,

    selectedCategoryId: Int?,
    selectedSubCategoryId: Int?,
    selectedBrandId: Int?

): CatalogEvent {

    val purchaseCents =
        if (purchase.isNotBlank())
            (purchase.toDouble() * 100).toLong()
        else 0L

    val saleCents =
        if (sale.isNotBlank())
            (sale.toDouble() * 100).toLong()
        else 0L


    return when(level){

        // NIVEL CATEGORIA
        CatalogLevel.CATEGORIES ->

            // Validación para determinar si se trata de un evento de creación o edición
            // Condición: Si el valor editingId esta vació Crear categoria, si no editarla, proveyendo dicho id.
            if(editingId == null)
                CatalogEvent.CreateCategory(name)
            else
                CatalogEvent.UpdateCategory(editingId, name)

        // NIVEL SUBCATEGORIA
        CatalogLevel.SUBCATEGORIES ->

            // Validación para determinar si se trata de un evento de creación o edición
            // Condición: Si el valor editingId esta vació Crear categoria, si no editarla, proveyendo dicho id.
            if(editingId == null)
                CatalogEvent.CreateSubCategory(
                    name,
                    selectedCategoryId!! // Recibe el id de la categoria a la que pertenece
                )
            else
                CatalogEvent.UpdateSubCategory(
                    editingId,
                    name,
                    selectedCategoryId!! // Recibe el id de la categoria a la que pertenece
                )

        // NIVEL MARCA
        CatalogLevel.BRANDS ->

            // Validación para determinar si se trata de un evento de creación o edición
            // Condición: Si el valor editingId esta vació Crear categoria, si no editarla, proveyendo dicho id.
            if(editingId == null)
                CatalogEvent.CreateBrand(
                    name,
                    selectedSubCategoryId!! // Recibe el id de la subCategoria a la que pertenece
                )
            else
                CatalogEvent.UpdateBrand(
                    editingId,
                    name,
                    selectedSubCategoryId!! // Recibe el id de la subCategoria a la que pertenece
                )

        // NIVEL PRODUCTO
        CatalogLevel.PRODUCTS ->

            // Validación para determinar si se trata de un evento de creación o edición
            // Condición: Si el valor editingId esta vació Crear categoria, si no editarla, proveyendo dicho id.
            if(editingId == null)
                CatalogEvent.CreateProduct(
                    name,
                    selectedBrandId!!, // Id de la marca a la que pertenece
                    purchaseCents,
                    saleCents,
                    hasExpiration,
                    isWeighable
                )
            else
                CatalogEvent.UpdateProduct(
                    editingId,
                    name,
                    selectedBrandId!!, // Id de la marca a la que pertenece
                    purchaseCents,
                    saleCents,
                    hasExpiration,
                    isWeighable
                )

    }

}