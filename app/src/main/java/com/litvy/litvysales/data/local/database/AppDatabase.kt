package com.litvy.litvysales.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.litvy.litvysales.data.local.dao.catalog.BrandDao
import com.litvy.litvysales.data.local.dao.catalog.CategoryDao
import com.litvy.litvysales.data.local.dao.catalog.ProductDao
import com.litvy.litvysales.data.local.dao.catalog.SubCategoryDao
import com.litvy.litvysales.data.local.dao.inventory.InventoryDao
import com.litvy.litvysales.data.local.dao.inventory.StockBatchDao
import com.litvy.litvysales.data.local.dao.inventory.StockMovementDao
import com.litvy.litvysales.data.local.dao.purchases.ProviderDao
import com.litvy.litvysales.data.local.dao.purchases.ProviderVisitDayDao
import com.litvy.litvysales.data.local.dao.user.RoleDao
import com.litvy.litvysales.data.local.dao.user.UserDao
import com.litvy.litvysales.data.local.entity.catalog.BrandEntity
import com.litvy.litvysales.data.local.entity.catalog.CategoryEntity
import com.litvy.litvysales.data.local.entity.catalog.ProductEntity
import com.litvy.litvysales.data.local.entity.catalog.ProductTaxEntity
import com.litvy.litvysales.data.local.entity.catalog.SubCategoryEntity
import com.litvy.litvysales.data.local.entity.inventory.InventoryEntity
import com.litvy.litvysales.data.local.entity.inventory.StockBatchEntity
import com.litvy.litvysales.data.local.entity.inventory.StockMovementEntity
import com.litvy.litvysales.data.local.entity.promotion.PromotionBenefitEntity
import com.litvy.litvysales.data.local.entity.promotion.PromotionConditionEntity
import com.litvy.litvysales.data.local.entity.promotion.PromotionEntity
import com.litvy.litvysales.data.local.entity.promotion.PromotionTargetEntity
import com.litvy.litvysales.data.local.entity.purchases.InvoiceTypeEntity
import com.litvy.litvysales.data.local.entity.purchases.ProviderEntity
import com.litvy.litvysales.data.local.entity.purchases.ProviderVisitDayEntity
import com.litvy.litvysales.data.local.entity.purchases.PurchaseEntity
import com.litvy.litvysales.data.local.entity.purchases.PurchaseItemEntity
import com.litvy.litvysales.data.local.entity.purchases.PurchaseOrderEntity
import com.litvy.litvysales.data.local.entity.purchases.PurchaseOrderItemEntity
import com.litvy.litvysales.data.local.entity.sales.CashMovementEntity
import com.litvy.litvysales.data.local.entity.sales.CashRegisterEntity
import com.litvy.litvysales.data.local.entity.sales.CashSessionEntity
import com.litvy.litvysales.data.local.entity.sales.CustomerEntity
import com.litvy.litvysales.data.local.entity.sales.SaleEntity
import com.litvy.litvysales.data.local.entity.sales.SaleItemEntity
import com.litvy.litvysales.data.local.entity.sales.SalePaymentEntity
import com.litvy.litvysales.data.local.entity.sales.SalePromotionEntity
import com.litvy.litvysales.data.local.entity.sales.TaxItemEntity
import com.litvy.litvysales.data.local.entity.user.RoleEntity
import com.litvy.litvysales.data.local.entity.user.UserEntity
import com.litvy.litvysales.data.local.entity.util.PaymentMethodEntity

@Database(
    entities = [
        CategoryEntity::class,
        SubCategoryEntity::class,
        BrandEntity::class,
        ProductEntity::class,
        RoleEntity::class,
        UserEntity::class,
        CustomerEntity::class,
        ProviderEntity::class,
        ProviderVisitDayEntity::class,
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
    version = 2
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `provider_visit_day` (
                        `providerId` INTEGER NOT NULL,
                        `dayOfWeek` INTEGER NOT NULL,
                        PRIMARY KEY(`providerId`, `dayOfWeek`),
                        FOREIGN KEY(`providerId`) REFERENCES `provider`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE
                    )
                    """.trimIndent()
                )
                database.execSQL(
                    "CREATE INDEX IF NOT EXISTS `index_provider_visit_day_providerId` ON `provider_visit_day` (`providerId`)"
                )
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "litvy_sales_db"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
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
    abstract fun providerDao(): ProviderDao
    abstract fun providerVisitDayDao(): ProviderVisitDayDao
}
