package com.litvy.litvysales.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.litvy.litvysales.data.local.entity.catalog.BrandEntity
import com.litvy.litvysales.data.local.entity.catalog.ProductEntity

data class ProductWithBrand(

    @Embedded
    val product: ProductEntity,

    @Relation(
        parentColumn = "brandId",
        entityColumn = "id"
    )
    val brand: BrandEntity
)