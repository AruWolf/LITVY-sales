package com.litvy.litvysales.ui.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.litvy.litvysales.domain.useCase.catalog.brand.*
import com.litvy.litvysales.domain.useCase.catalog.category.*
import com.litvy.litvysales.domain.useCase.catalog.product.*
import com.litvy.litvysales.domain.useCase.catalog.subCategory.*
import com.litvy.litvysales.ui.catalog.util.CatalogEvent
import com.litvy.litvysales.ui.catalog.util.CatalogLevel
import com.litvy.litvysales.ui.catalog.util.CatalogState
import kotlinx.coroutines.launch

class CatalogViewModel(

    private val getCategories: GetCategoriesUseCase,
    private val getSubCategories: GetSubCategoriesByCategoryUseCase,
    private val getBrands: GetBrandBySubCategoryUseCase,
    private val getProducts: GetProductByBrandUseCase,
    private val createCategory: CreateCategoryUseCase,
    private val createSubCategory: CreateSubCategoryUseCase,
    private val createBrand: CreateBrandUseCase,
    private val createProduct: CreateProductUseCase,
    private val updateCategory: UpdateCategoryUseCase,
    private val updateSubCategory: UpdateSubCategoryUseCase,
    private val updateBrand: UpdateBrandUseCase,
    private val updateProduct: UpdateProductUseCase

) : ViewModel() {

    private val _state = MutableStateFlow(CatalogState())
    val state: StateFlow<CatalogState> = _state.asStateFlow()

    init {
        loadCategories()
    }

    private fun loadCategories() {

        viewModelScope.launch {

            val categories = getCategories()

            _state.value = _state.value.copy(
                categories = categories
            )
        }
    }

    fun onEvent(event: CatalogEvent) {

        when(event){

            is CatalogEvent.NavigateToCategories -> {

                _state.value = _state.value.copy(
                    level = CatalogLevel.CATEGORIES,
                    subCategories = emptyList(),
                    brands = emptyList(),
                    products = emptyList(),
                    selectedCategoryId = null,
                    selectedSubCategoryId = null,
                    selectedBrandId = null,
                    selectedCategoryName = null,
                    selectedSubCategoryName = null,
                    selectedBrandName = null
                )

            }

            is CatalogEvent.NavigateToSubCategories -> {

                val categoryId = _state.value.selectedCategoryId ?: return

                viewModelScope.launch {

                    val subs = getSubCategories(categoryId)

                    _state.value = _state.value.copy(
                        level = CatalogLevel.SUBCATEGORIES,
                        subCategories = subs,
                        brands = emptyList(),
                        products = emptyList(),
                        selectedSubCategoryId = null,
                        selectedBrandId = null,
                        selectedSubCategoryName = null,
                        selectedBrandName = null
                    )
                }
            }

            is CatalogEvent.NavigateToBrands -> {

                val subCategoryId = _state.value.selectedSubCategoryId ?: return

                viewModelScope.launch {

                    val brands = getBrands(subCategoryId)

                    _state.value = _state.value.copy(
                        level = CatalogLevel.BRANDS,
                        brands = brands,
                        products = emptyList(),
                        selectedBrandId = null,
                        selectedBrandName = null
                    )
                }
            }

            is CatalogEvent.SelectCategory -> {

                viewModelScope.launch {

                    val subCategories =
                        getSubCategories(event.categoryId)

                    val categoryName = _state.value.categories
                        .first { it.id == event.categoryId }
                        .name

                    _state.value = _state.value.copy(
                        level = CatalogLevel.SUBCATEGORIES,
                        selectedCategoryId = event.categoryId,
                        selectedCategoryName = categoryName,
                        subCategories = subCategories,
                        brands = emptyList(),
                        products = emptyList()
                    )
                }
            }

            is CatalogEvent.SelectSubCategory -> {

                viewModelScope.launch {

                    val brands =
                        getBrands(event.subCategoryId)

                    val subCategoryName = _state.value.subCategories
                        .first { it.id == event.subCategoryId }
                        .name

                    _state.value = _state.value.copy(
                        level = CatalogLevel.BRANDS,
                        selectedSubCategoryId = event.subCategoryId,
                        selectedSubCategoryName = subCategoryName,
                        brands = brands,
                        products = emptyList()
                    )
                }
            }

            is CatalogEvent.SelectBrand -> {

                viewModelScope.launch {

                    val brandName = _state.value.brands
                        .first { it.id == event.brandId }
                        .name

                    getProducts(event.brandId).collect { products ->
                        _state.value = _state.value.copy(
                            level = CatalogLevel.PRODUCTS,
                            selectedBrandId = event.brandId,
                            selectedBrandName = brandName,
                            products = products
                        )
                    }
                }
            }

            is CatalogEvent.CreateCategory -> {

                viewModelScope.launch {

                    createCategory(event.name)

                    val categories = getCategories()

                    _state.value = _state.value.copy(
                        categories = categories
                    )

                }
            }

            is CatalogEvent.CreateSubCategory -> {

                viewModelScope.launch {

                    createSubCategory(
                        event.name,
                        event.categoryId
                    )

                    val subs = getSubCategories(event.categoryId)

                    _state.value = _state.value.copy(
                        subCategories = subs
                    )

                }
            }

            is CatalogEvent.CreateBrand -> {

                viewModelScope.launch {

                    createBrand(
                        event.name,
                        event.subCategoryId
                    )

                    val brands =
                        getBrands(event.subCategoryId)

                    _state.value = _state.value.copy(
                        brands = brands
                    )

                }
            }

            is CatalogEvent.CreateProduct -> {

                viewModelScope.launch {

                    createProduct.invoke(
                        event.name,
                        event.brandId,
                        event.purchasePrice,
                        event.salePrice,
                        false,
                        false
                    )

                }
            }

            is CatalogEvent.UpdateCategory -> {
                viewModelScope.launch {

                    updateCategory(
                        event.id,
                        event.name
                    )

                    val categories = getCategories()

                    _state.value = _state.value.copy(
                        categories = categories
                    )
                }
            }

            is CatalogEvent.UpdateSubCategory -> {
                viewModelScope.launch {
                    updateSubCategory(
                        event.id,
                        event.name
                    )

                    val subCategories = getSubCategories(event.categoryId)

                    _state.value = _state.value.copy(
                        subCategories = subCategories
                    )
                }
            }

            is CatalogEvent.UpdateBrand -> {
                viewModelScope.launch {

                    updateBrand(
                        event.id,
                        event.name
                    )

                    val brands = getBrands(event.subCategoryId)

                    _state.value = _state.value.copy(
                        brands = brands
                    )
                }
            }

            is CatalogEvent.UpdateProduct -> {

                viewModelScope.launch {

                    updateProduct(
                        event.id,
                        event.name,
                        event.brandId,
                        event.purchasePrice,
                        event.salePrice,
                        event.hasExpiration,
                        event.isWeighable
                    )
                }
            }

            is CatalogEvent.NavigateBack -> {

                val current = _state.value

                when(current.level){

                    CatalogLevel.PRODUCTS -> {
                        _state.value = current.copy(
                            level = CatalogLevel.BRANDS,
                            products = emptyList(),
                            selectedBrandId = null,
                            selectedBrandName = null
                        )
                    }

                    CatalogLevel.BRANDS -> {
                        _state.value = current.copy(
                            level = CatalogLevel.SUBCATEGORIES,
                            brands = emptyList(),
                            selectedSubCategoryId = null,
                            selectedSubCategoryName = null
                        )
                    }

                    CatalogLevel.SUBCATEGORIES -> {
                        _state.value = current.copy(
                            level = CatalogLevel.CATEGORIES,
                            subCategories = emptyList(),
                            selectedCategoryId = null,
                            selectedCategoryName = null
                        )
                    }

                    else -> {}
                }

            }

            else -> {}
        }
    }
}