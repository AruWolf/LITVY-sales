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
import com.litvy.litvysales.data.local.dao.purchases.InvoiceTypeDao
import com.litvy.litvysales.data.local.dao.purchases.ProviderDao
import com.litvy.litvysales.data.local.dao.purchases.ProviderVisitDayDao
import com.litvy.litvysales.data.local.dao.purchases.PurchaseDao
import com.litvy.litvysales.data.local.dao.purchases.PurchaseItemDao
import com.litvy.litvysales.data.local.dao.purchases.PurchaseOrderDao
import com.litvy.litvysales.data.local.dao.purchases.PurchaseOrderItemDao
import com.litvy.litvysales.data.local.dao.sales.CashMovementDao
import com.litvy.litvysales.data.local.dao.sales.CashRegisterDao
import com.litvy.litvysales.data.local.dao.sales.CashSessionDao
import com.litvy.litvysales.data.local.dao.sales.CashSessionScheduleDao
import com.litvy.litvysales.data.local.dao.sales.CustomerDao
import com.litvy.litvysales.data.local.dao.sales.SaleDao
import com.litvy.litvysales.data.local.dao.sales.SaleItemDao
import com.litvy.litvysales.data.local.dao.sales.SalePaymentDao
import com.litvy.litvysales.data.local.dao.sales.SalePromotionDao
import com.litvy.litvysales.data.local.dao.sales.TaxItemDao
import com.litvy.litvysales.data.local.dao.user.RoleDao
import com.litvy.litvysales.data.local.dao.user.UserDao
import com.litvy.litvysales.data.local.dao.util.PaymentMethodDao
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
import com.litvy.litvysales.data.local.entity.sales.CashSessionScheduleEntity
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
        CashSessionScheduleEntity::class,
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
    version = 7
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

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "ALTER TABLE `purchase` ADD COLUMN `salesRepName` TEXT"
                )
            }
        }

        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `user_new` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `name` TEXT NOT NULL,
                        `lastname` TEXT NOT NULL,
                        `telephoneNumber` TEXT,
                        `dni` TEXT,
                        `birthDate` INTEGER,
                        `address` TEXT,
                        `email` TEXT,
                        `passwordHash` TEXT NOT NULL,
                        `roleId` INTEGER NOT NULL,
                        `active` INTEGER NOT NULL DEFAULT 0,
                        `createdAt` INTEGER NOT NULL,
                        `updatedAt` INTEGER NOT NULL,
                        FOREIGN KEY(`roleId`) REFERENCES `role`(`id`) ON UPDATE NO ACTION ON DELETE RESTRICT
                    )
                    """.trimIndent()
                )
                database.execSQL(
                    """
                    INSERT INTO `user_new` (
                        `id`,
                        `name`,
                        `lastname`,
                        `telephoneNumber`,
                        `dni`,
                        `birthDate`,
                        `address`,
                        `email`,
                        `passwordHash`,
                        `roleId`,
                        `active`,
                        `createdAt`,
                        `updatedAt`
                    )
                    SELECT
                        `id`,
                        `name`,
                        `lastname`,
                        `telephoneNumber`,
                        `dni`,
                        `birthDate`,
                        `address`,
                        `email`,
                        `passwordHash`,
                        `roleId`,
                        COALESCE(`active`, 0),
                        `createdAt`,
                        `updatedAt`
                    FROM `user`
                    """.trimIndent()
                )
                database.execSQL("DROP TABLE `user`")
                database.execSQL("ALTER TABLE `user_new` RENAME TO `user`")
                database.execSQL(
                    "CREATE UNIQUE INDEX IF NOT EXISTS `index_user_email` ON `user` (`email`)"
                )
                database.execSQL(
                    "CREATE INDEX IF NOT EXISTS `index_user_roleId` ON `user` (`roleId`)"
                )
                database.execSQL(
                    "CREATE UNIQUE INDEX IF NOT EXISTS `index_user_telephoneNumber` ON `user` (`telephoneNumber`)"
                )
            }
        }

        private val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(database: SupportSQLiteDatabase) {

                // 1. Crear nueva tabla sin paymentMethodId
                database.execSQL("""
            CREATE TABLE IF NOT EXISTS `saleItem_new` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `saleId` INTEGER NOT NULL,
                `productId` INTEGER NOT NULL,
                `quantity` REAL NOT NULL,
                `unitPriceInCents` INTEGER NOT NULL,
                `discountAppliedInCents` INTEGER NOT NULL,
                `originalUnitPriceInCents` INTEGER NOT NULL,
                `totalInCents` INTEGER NOT NULL,
                FOREIGN KEY(`saleId`) REFERENCES `sale`(`id`) ON DELETE CASCADE,
                FOREIGN KEY(`productId`) REFERENCES `product`(`id`) ON DELETE RESTRICT
            )
        """.trimIndent())

                // 2. Copiar datos (sin paymentMethodId)
                database.execSQL("""
            INSERT INTO saleItem_new (
                id, saleId, productId, quantity,
                unitPriceInCents, discountAppliedInCents,
                originalUnitPriceInCents, totalInCents
            )
            SELECT
                id, saleId, productId, quantity,
                unitPriceInCents, discountAppliedInCents,
                originalUnitPriceInCents, totalInCents
            FROM saleItem
        """.trimIndent())

                // 3. Borrar vieja
                database.execSQL("DROP TABLE saleItem")

                // 4. Renombrar
                database.execSQL("ALTER TABLE saleItem_new RENAME TO saleItem")

                // 5. Índices
                database.execSQL("CREATE INDEX index_saleItem_saleId ON saleItem(saleId)")
                database.execSQL("CREATE INDEX index_saleItem_productId ON saleItem(productId)")
            }
        }

        private val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "ALTER TABLE `cashSession` ADD COLUMN `differenceJustification` TEXT"
                )

                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `cashSessionSchedule` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `dayOfWeek` INTEGER NOT NULL,
                        `openMinuteOfDay` INTEGER NOT NULL,
                        `closeMinuteOfDay` INTEGER NOT NULL,
                        `graceMinutes` INTEGER NOT NULL,
                        `active` INTEGER NOT NULL
                    )
                    """.trimIndent()
                )
            }
        }

        private val MIGRATION_6_7 = object : Migration(6, 7) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "ALTER TABLE `cashSessionSchedule` ADD COLUMN `title` TEXT"
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
                    .addMigrations(
                        MIGRATION_1_2,
                        MIGRATION_2_3,
                        MIGRATION_3_4,
                        MIGRATION_4_5,
                        MIGRATION_5_6,
                        MIGRATION_6_7
                    )
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
    abstract fun invoiceTypeDao(): InvoiceTypeDao
    abstract fun purchaseDao(): PurchaseDao
    abstract fun purchaseItemDao(): PurchaseItemDao
    abstract fun purchaseOrderDao(): PurchaseOrderDao
    abstract fun purchaseOrderItemDao(): PurchaseOrderItemDao
    abstract fun paymentMethodDao(): PaymentMethodDao
    abstract fun saleDao(): SaleDao
    abstract fun cashMovementDao(): CashMovementDao
    abstract fun cashRegisterDao(): CashRegisterDao
    abstract fun cashSessionDao(): CashSessionDao
    abstract fun cashSessionScheduleDao(): CashSessionScheduleDao
    abstract fun customerDao(): CustomerDao
    abstract fun saleItemDao(): SaleItemDao
    abstract fun salePaymentDao(): SalePaymentDao
    abstract fun salePromotionDao(): SalePromotionDao
    abstract fun taxItemDao(): TaxItemDao
}
