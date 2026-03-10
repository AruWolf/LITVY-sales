package com.litvy.litvysales.di

import android.content.Context
import androidx.room.Room
import com.litvy.litvysales.data.local.database.AppDatabase
import com.litvy.litvysales.data.repository.catalog.BrandRepositoryImpl
import com.litvy.litvysales.data.repository.catalog.CategoryRepositoryImpl
import com.litvy.litvysales.data.repository.catalog.ProductRepositoryImpl
import com.litvy.litvysales.data.repository.catalog.SubCategoryRepositoryImpl
import com.litvy.litvysales.domain.useCase.catalog.brand.GetBrandBySubCategoryUseCase
import com.litvy.litvysales.domain.useCase.catalog.category.GetCategoriesUseCase
import com.litvy.litvysales.domain.useCase.catalog.product.GetProductByBrandUseCase
import com.litvy.litvysales.domain.useCase.catalog.subCategory.GetSubCategoriesByCategoryUseCase
import com.litvy.litvysales.domain.useCase.catalog.category.CreateCategoryUseCase
import com.litvy.litvysales.domain.useCase.catalog.subCategory.CreateSubCategoryUseCase
import com.litvy.litvysales.domain.useCase.catalog.brand.CreateBrandUseCase
import com.litvy.litvysales.domain.useCase.catalog.brand.UpdateBrandUseCase
import com.litvy.litvysales.domain.useCase.catalog.category.UpdateCategoryUseCase
import com.litvy.litvysales.domain.useCase.catalog.product.CreateProductUseCase
import com.litvy.litvysales.domain.useCase.catalog.product.UpdateProductUseCase
import com.litvy.litvysales.domain.useCase.catalog.subCategory.UpdateSubCategoryUseCase

class AppContainer(context: Context) {

    // DATABASE
    val database: AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "litvy_sales_db"
        ).build()

    // DAO
    val categoryDao = database.categoryDao()
    val subCategoryDao = database.subCategoryDao()
    val brandDao = database.brandDao()
    val productDao = database.productDao()

    // REPOSITORIES
    val categoryRepository = CategoryRepositoryImpl(categoryDao, subCategoryDao)
    val subCategoryRepository = SubCategoryRepositoryImpl(subCategoryDao, brandDao)
    val brandRepository = BrandRepositoryImpl(brandDao, productDao)
    val productRepository = ProductRepositoryImpl(productDao)

    // GET USE CASES
    val getCategoriesUseCase = GetCategoriesUseCase(categoryRepository)

    val getSubCategoriesByCategoryUseCase =
        GetSubCategoriesByCategoryUseCase(subCategoryRepository)

    val getBrandBySubCategoryUseCase =
        GetBrandBySubCategoryUseCase(brandRepository)

    val getProductByBrandUseCase =
        GetProductByBrandUseCase(productRepository)

    // CREATE USE CASES

    val createCategoryUseCase =
        CreateCategoryUseCase(categoryRepository)

    val createSubCategoryUseCase =
        CreateSubCategoryUseCase(
            subCategoryRepository,
            categoryRepository
        )

    val createBrandUseCase =
        CreateBrandUseCase(brandRepository)

    val createProductUseCase =
        CreateProductUseCase(
            productRepository,
            brandRepository
        )

    val updateCategoryUseCase =
        UpdateCategoryUseCase(
            categoryRepository
        )

    val updateSubCategoryUseCase =
        UpdateSubCategoryUseCase(
            subCategoryRepository
        )

    val updateBrandUseCase =
        UpdateBrandUseCase(
            brandRepository
        )

    val updateProductUseCase =
        UpdateProductUseCase(
            productRepository
        )
}