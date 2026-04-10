package com.litvy.litvysales.di

import android.content.Context
import com.litvy.litvysales.data.local.database.AppDatabase
import com.litvy.litvysales.data.repository.catalog.BrandRepositoryImpl
import com.litvy.litvysales.data.repository.catalog.CategoryRepositoryImpl
import com.litvy.litvysales.data.repository.catalog.ProductRepositoryImpl
import com.litvy.litvysales.data.repository.catalog.SubCategoryRepositoryImpl
import com.litvy.litvysales.data.repository.inventory.InventoryRepositoryImpl
import com.litvy.litvysales.data.repository.inventory.StockBatchRepositoryImpl
import com.litvy.litvysales.data.repository.inventory.StockMovementRepositoryImpl
import com.litvy.litvysales.data.repository.purchases.InvoiceTypeRepositoryImpl
import com.litvy.litvysales.data.repository.purchases.ProviderRepositoryImpl
import com.litvy.litvysales.data.repository.purchases.PurchaseOrderRepositoryImpl
import com.litvy.litvysales.data.repository.purchases.PurchaseRepositoryImpl
import com.litvy.litvysales.data.repository.sales.CashRegisterRepositoryImpl
import com.litvy.litvysales.data.repository.sales.CashSessionRepositoryImpl
import com.litvy.litvysales.data.repository.sales.SaleItemRepositoryImpl
import com.litvy.litvysales.data.repository.sales.SalePaymentRepositoryImpl
import com.litvy.litvysales.data.repository.sales.SaleRepositoryImpl
import com.litvy.litvysales.data.repository.user.RoleRepositoryImpl
import com.litvy.litvysales.data.repository.user.UserRepositoryImpl
import com.litvy.litvysales.data.repository.util.PaymentMethodRepositoryImpl
import com.litvy.litvysales.domain.useCase.catalog.brand.CreateBrandUseCase
import com.litvy.litvysales.domain.useCase.catalog.brand.GetBrandBySubCategoryUseCase
import com.litvy.litvysales.domain.useCase.catalog.brand.UpdateBrandUseCase
import com.litvy.litvysales.domain.useCase.catalog.category.CreateCategoryUseCase
import com.litvy.litvysales.domain.useCase.catalog.category.GetCategoriesUseCase
import com.litvy.litvysales.domain.useCase.catalog.category.UpdateCategoryUseCase
import com.litvy.litvysales.domain.useCase.catalog.product.CreateProductUseCase
import com.litvy.litvysales.domain.useCase.catalog.product.GetActiveProductsUseCase
import com.litvy.litvysales.domain.useCase.catalog.product.GetActiveProductsWithBrandUseCase
import com.litvy.litvysales.domain.useCase.catalog.product.GetProductByBrandUseCase
import com.litvy.litvysales.domain.useCase.catalog.product.SearchProductsUseCase
import com.litvy.litvysales.domain.useCase.catalog.product.UpdateProductUseCase
import com.litvy.litvysales.domain.useCase.catalog.subCategory.CreateSubCategoryUseCase
import com.litvy.litvysales.domain.useCase.catalog.subCategory.GetSubCategoriesByCategoryUseCase
import com.litvy.litvysales.domain.useCase.catalog.subCategory.UpdateSubCategoryUseCase
import com.litvy.litvysales.domain.useCase.purchases.GetInvoiceTypesUseCase
import com.litvy.litvysales.domain.useCase.purchases.GetPurchasesUseCase
import com.litvy.litvysales.domain.useCase.purchases.RegisterPurchaseUseCase
import com.litvy.litvysales.domain.useCase.purchases.provider.CreateProviderUseCase
import com.litvy.litvysales.domain.useCase.purchases.provider.GetProviderUseCase
import com.litvy.litvysales.domain.useCase.purchases.provider.GetProvidersWithVisitDaysUseCase
import com.litvy.litvysales.domain.useCase.purchases.provider.UpdateProviderUseCase
import com.litvy.litvysales.domain.useCase.purchases.purchaseOrder.CreatePurchaseOrderUseCase
import com.litvy.litvysales.domain.useCase.purchases.purchaseOrder.GetPurchaseOrderItemsUseCase
import com.litvy.litvysales.domain.useCase.purchases.purchaseOrder.GetPurchaseOrdersUseCase
import com.litvy.litvysales.domain.useCase.purchases.purchaseOrder.UpdatePurchaseOrderUseCase
import com.litvy.litvysales.domain.useCase.sales.CreateSaleUseCase
import com.litvy.litvysales.domain.useCase.sales.ValidateSaleUseCase
import com.litvy.litvysales.domain.useCase.sales.paymentmethod.CreatePaymentMethodUseCase
import com.litvy.litvysales.domain.useCase.sales.paymentmethod.DeletePaymentMethodUseCase
import com.litvy.litvysales.domain.useCase.sales.paymentmethod.GetPaymentMethodsUseCase
import com.litvy.litvysales.domain.useCase.sales.paymentmethod.UpdatePaymentMethodUseCase

class AppContainer(context: Context) {

    val database: AppDatabase = AppDatabase.getDatabase(context)

    val categoryDao = database.categoryDao()
    val subCategoryDao = database.subCategoryDao()
    val brandDao = database.brandDao()
    val productDao = database.productDao()
    val providerDao = database.providerDao()
    val providerVisitDayDao = database.providerVisitDayDao()
    val invoiceTypeDao = database.invoiceTypeDao()
    val purchaseDao = database.purchaseDao()
    val purchaseItemDao = database.purchaseItemDao()
    val purchaseOrderDao = database.purchaseOrderDao()
    val purchaseOrderItemDao = database.purchaseOrderItemDao()
    val stockBatchDao = database.stockBatchDao()
    val stockMovementDao = database.stockMovementDao()
    val paymentMethodDao = database.paymentMethodDao()
    val userDao = database.userDao()
    val roleDao = database.roleDao()
    val saleDao = database.saleDao()
    val cashRegisterDao = database.cashRegisterDao()
    val cashSessionDao = database.cashSessionDao()
    val inventoryDao = database.inventoryDao()
    val saleItemDao = database.saleItemDao()
    val salePaymentDao = database.salePaymentDao()

    val categoryRepository = CategoryRepositoryImpl(categoryDao, subCategoryDao)
    val subCategoryRepository = SubCategoryRepositoryImpl(subCategoryDao, brandDao)
    val brandRepository = BrandRepositoryImpl(brandDao, productDao)
    val productRepository = ProductRepositoryImpl(productDao)
    val providerRepository = ProviderRepositoryImpl(providerDao, providerVisitDayDao)
    val invoiceTypeRepository = InvoiceTypeRepositoryImpl(invoiceTypeDao)
    val purchaseRepository = PurchaseRepositoryImpl(purchaseDao, purchaseItemDao)
    val purchaseOrderRepository = PurchaseOrderRepositoryImpl(purchaseOrderDao, purchaseOrderItemDao)
    val stockBatchRepository = StockBatchRepositoryImpl(stockBatchDao)
    val stockMovementRepository = StockMovementRepositoryImpl(stockMovementDao)
    val paymentMethodRepository = PaymentMethodRepositoryImpl(paymentMethodDao)
    val userRepository = UserRepositoryImpl(userDao)
    val roleRepository = RoleRepositoryImpl(roleDao)
    val saleRepository = SaleRepositoryImpl(saleDao)
    val cashRegisterRepository = CashRegisterRepositoryImpl(cashRegisterDao)
    val cashSessionRepository = CashSessionRepositoryImpl(cashSessionDao)
    val inventoryRepository = InventoryRepositoryImpl(inventoryDao)
    val saleItemRepository = SaleItemRepositoryImpl(saleItemDao)
    val salePaymentRepository = SalePaymentRepositoryImpl(salePaymentDao)

    val getCategoriesUseCase = GetCategoriesUseCase(categoryRepository)
    val getSubCategoriesByCategoryUseCase = GetSubCategoriesByCategoryUseCase(subCategoryRepository)
    val getBrandBySubCategoryUseCase = GetBrandBySubCategoryUseCase(brandRepository)
    val getProductByBrandUseCase = GetProductByBrandUseCase(productRepository)
    val getActiveProductsWithBrandUseCase = GetActiveProductsWithBrandUseCase(productRepository)
    val getActiveProductsUseCase = GetActiveProductsUseCase(productRepository)
    val searchProductsUseCase = SearchProductsUseCase(productRepository)
    val getProvidersWithVisitDaysUseCase = GetProvidersWithVisitDaysUseCase(providerRepository)
    val getProviderUseCase = GetProviderUseCase(providerRepository)
    val getInvoiceTypesUseCase = GetInvoiceTypesUseCase(invoiceTypeRepository)
    val getPaymentMethodsUseCase = GetPaymentMethodsUseCase(paymentMethodRepository)
    val getPurchasesUseCase = GetPurchasesUseCase(purchaseRepository)
    val getPurchaseOrdersUseCase = GetPurchaseOrdersUseCase(purchaseOrderRepository)
    val getPurchaseOrderItemsUseCase = GetPurchaseOrderItemsUseCase(purchaseOrderRepository)

    val createCategoryUseCase = CreateCategoryUseCase(categoryRepository)
    val createSubCategoryUseCase = CreateSubCategoryUseCase(subCategoryRepository, categoryRepository)
    val createBrandUseCase = CreateBrandUseCase(brandRepository)
    val createProductUseCase = CreateProductUseCase(productRepository, brandRepository)
    val createProviderUseCase = CreateProviderUseCase(providerRepository)
    val createPurchaseOrderUseCase = CreatePurchaseOrderUseCase(purchaseOrderRepository)
    val createPaymentMethodUseCase = CreatePaymentMethodUseCase(paymentMethodRepository)
    val validateSaleUseCase = ValidateSaleUseCase()
    val createSaleUseCase = CreateSaleUseCase(
        saleRepository = saleRepository,
        inventoryRepository = inventoryRepository,
        stockMovementRepository = stockMovementRepository,
        saleItemRepository = saleItemRepository,
        salePaymentRepository = salePaymentRepository,
        validateSaleUseCase = validateSaleUseCase
    )

    val updateCategoryUseCase = UpdateCategoryUseCase(categoryRepository)
    val updateSubCategoryUseCase = UpdateSubCategoryUseCase(subCategoryRepository)
    val updateBrandUseCase = UpdateBrandUseCase(brandRepository)
    val updateProductUseCase = UpdateProductUseCase(productRepository)
    val updateProviderUseCase = UpdateProviderUseCase(providerRepository)
    val updatePurchaseOrderUseCase = UpdatePurchaseOrderUseCase(purchaseOrderRepository)
    val updatePaymentMethodUseCase = UpdatePaymentMethodUseCase(paymentMethodRepository)

    val deletePaymentMethodUseCase = DeletePaymentMethodUseCase(paymentMethodRepository)

    val registerPurchaseUseCase = RegisterPurchaseUseCase(
        purchaseRepository,
        stockBatchRepository,
        stockMovementRepository
    )

}
