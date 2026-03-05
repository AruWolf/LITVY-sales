package com.litvy.litvysales.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.litvy.litvysales.data.local.dao.catalog.BrandDao
import com.litvy.litvysales.data.local.dao.catalog.CategoryDao
import com.litvy.litvysales.data.local.dao.catalog.ProductDao
import com.litvy.litvysales.data.local.dao.catalog.SubCategoryDao
import com.litvy.litvysales.data.local.dao.inventory.InventoryDao
import com.litvy.litvysales.data.local.dao.inventory.StockBatchDao
import com.litvy.litvysales.data.local.dao.inventory.StockMovementDao
import com.litvy.litvysales.data.local.dao.user.RoleDao
import com.litvy.litvysales.data.local.dao.user.UserDao
import com.litvy.litvysales.data.local.entity.catalog.*
import com.litvy.litvysales.data.local.entity.inventory.*
import com.litvy.litvysales.data.local.entity.purchases.*
import com.litvy.litvysales.data.local.entity.sales.*
import com.litvy.litvysales.data.local.entity.user.*
import com.litvy.litvysales.data.local.entity.util.*
import com.litvy.litvysales.data.local.entity.promotion.*

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
        InventoryEntity::class,
        StockMovementEntity::class,
        PromotionEntity::class,
        PromotionConditionEntity::class,
        PromotionTargetEntity::class,
        PromotionBenefitEntity::class,
        SalePromotionEntity::class,
        InvoiceTypeEntity::class
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

    abstract fun inventoryDao(): InventoryDao
    abstract fun stockBatchDao(): StockBatchDao
    abstract fun stockMovementDao(): StockMovementDao
    abstract fun categoryDao(): CategoryDao
    abstract fun subCategoryDao(): SubCategoryDao
    abstract fun brandDao(): BrandDao
    abstract fun productDao(): ProductDao
    abstract fun roleDao(): RoleDao
    abstract fun userDao(): UserDao
}