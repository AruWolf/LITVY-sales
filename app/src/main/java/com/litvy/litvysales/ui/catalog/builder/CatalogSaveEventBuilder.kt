package com.litvy.litvysales.ui.catalog.builder

import com.litvy.litvysales.ui.catalog.util.CatalogEvent
import com.litvy.litvysales.ui.catalog.util.CatalogLevel

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

        CatalogLevel.CATEGORIES ->

            if(editingId == null)
                CatalogEvent.CreateCategory(name)
            else
                CatalogEvent.UpdateCategory(editingId, name)


        CatalogLevel.SUBCATEGORIES ->

            if(editingId == null)
                CatalogEvent.CreateSubCategory(
                    name,
                    selectedCategoryId!!
                )
            else
                CatalogEvent.UpdateSubCategory(
                    editingId,
                    name,
                    selectedCategoryId!!
                )


        CatalogLevel.BRANDS ->

            if(editingId == null)
                CatalogEvent.CreateBrand(
                    name,
                    selectedSubCategoryId!!
                )
            else
                CatalogEvent.UpdateBrand(
                    editingId,
                    name,
                    selectedSubCategoryId!!
                )


        CatalogLevel.PRODUCTS ->

            if(editingId == null)
                CatalogEvent.CreateProduct(
                    name,
                    selectedBrandId!!,
                    purchaseCents,
                    saleCents,
                    hasExpiration,
                    isWeighable
                )
            else
                CatalogEvent.UpdateProduct(
                    editingId,
                    name,
                    selectedBrandId!!,
                    purchaseCents,
                    saleCents,
                    hasExpiration,
                    isWeighable
                )

    }

}