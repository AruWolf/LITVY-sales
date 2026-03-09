package com.litvy.litvysales.data.local.database

import androidx.room.TypeConverter
import com.litvy.litvysales.data.local.entity.enums.*

class Converters {

    @TypeConverter
    fun fromSaleStatus(value: SaleStatus): String = value.name

    @TypeConverter
    fun toSaleStatus(value: String): SaleStatus = SaleStatus.valueOf(value)

    @TypeConverter
    fun fromCashSessionStatus(value: CashSessionStatus): String = value.name

    @TypeConverter
    fun toCashSessionStatus(value: String): CashSessionStatus = CashSessionStatus.valueOf(value)

    @TypeConverter
    fun fromStockMovementType(value: StockMovementType): String = value.name

    @TypeConverter
    fun toStockMovementType(value: String): StockMovementType = StockMovementType.valueOf(value)

    @TypeConverter
    fun fromPurchaseOrderStatus(value: PurchaseOrderStatus): String = value.name

    @TypeConverter
    fun toPurchaseOrderStatus(value: String): PurchaseOrderStatus = PurchaseOrderStatus.valueOf(value)

    @TypeConverter
    fun fromConditionType(value: PromotionConditionType): String {
        return value.name
    }

    @TypeConverter
    fun toConditionType(value: String): PromotionConditionType {
        return PromotionConditionType.valueOf(value)
    }
}