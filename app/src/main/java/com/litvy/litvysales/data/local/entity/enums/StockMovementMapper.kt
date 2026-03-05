package com.litvy.litvysales.data.local.entity.enums

fun StockMovementType.toDomain(): com.litvy.litvysales.domain.model.enums.StockMovementType {
    return when (this) {
        StockMovementType.SALE -> com.litvy.litvysales.domain.model.enums.StockMovementType.SALE
        StockMovementType.PURCHASE -> com.litvy.litvysales.domain.model.enums.StockMovementType.PURCHASE
        StockMovementType.ADJUSTMENT -> com.litvy.litvysales.domain.model.enums.StockMovementType.ADJUSTMENT
        StockMovementType.LOSS -> com.litvy.litvysales.domain.model.enums.StockMovementType.LOSS
    }
}