package com.litvy.litvysales.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.litvy.litvysales.data.local.entity.*

@Database(
    entities = [ // Tables instances
        CategoryEntity::class,
        SubCategoryEntity::class,
        BrandEntity::class,
        ProductEntity::class,
        RoleEntity::class,
        UserEntity::class,
        CustomerEntity::class,
        ProviderEntity::class,
        PaymentMethodEntity::class,
        TaxItemEntity::class,
        ProductTaxEntity::class,
        CashRegisterEntity::class,
        CashSessionEntity::class,
        CashMovementEntity::class,
        SaleEntity::class,
        SaleItemEntity::class,
        SalePaymentEntity::class,
        PurchaseEntity::class,
        PurchaseItemEntity::class,
        PurchaseOrderEntity::class,
        PurchaseOrderItemEntity::class,
        StockBatchEntity::class,
        StockMovementEntity::class
    ],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase(){

    companion object{
    @Volatile
    private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "litvy_sales_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
}
}