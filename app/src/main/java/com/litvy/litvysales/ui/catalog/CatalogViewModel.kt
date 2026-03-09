package com.litvy.litvysales.ui.catalog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.litvy.litvysales.domain.useCase.catalog.brand.*
import com.litvy.litvysales.domain.useCase.catalog.category.*
import com.litvy.litvysales.domain.useCase.catalog.product.*
import com.litvy.litvysales.domain.useCase.catalog.subCategory.*
import kotlinx.coroutines.launch

class CatalogViewModel(

    private val getCategories: GetCategoriesUseCase,
    private val getSubCategories: GetSubCategoriesByCategoryUseCase,
    private val getBrands: GetBrandBySubCategoryUseCase,
    private val getProducts: GetProductByBrandUseCase,
    private val createCategory: CreateCategoryUseCase,
    private val createSubCategory: CreateSubCategoryUseCase,
    private val createBrand: CreateBrandUseCase,
    private val createProduct: CreateProductUseCase

) : ViewModel() {

    private val _state = MutableLiveData(CatalogState())
    val state: LiveData<CatalogState> = _state

    init {
        loadCategories()
    }

    private fun loadCategories() {

        viewModelScope.launch {

            val categories = getCategories()

            _state.value = _state.value!!.copy(
                categories = categories
            )
        }
    }

    fun onEvent(event: CatalogEvent) {

        when(event){

            is CatalogEvent.SelectCategory -> {

                viewModelScope.launch {

                    val subCategories =
                        getSubCategories(event.categoryId)

                    _state.value = _state.value!!.copy(
                        selectedCategoryId = event.categoryId,
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

                    _state.value = _state.value!!.copy(
                        selectedSubCategoryId = event.subCategoryId,
                        brands = brands,
                        products = emptyList()
                    )
                }
            }

            is CatalogEvent.SelectBrand -> {

                viewModelScope.launch {

                    getProducts(event.brandId)
                        .collect { products ->

                            _state.postValue(
                                _state.value!!.copy(
                                    selectedBrandId = event.brandId,
                                    products = products
                                )
                            )
                        }
                }
            }

            is CatalogEvent.CreateCategory -> {

                viewModelScope.launch {

                    createCategory(event.name)

                    val categories = getCategories()

                    _state.value = _state.value!!.copy(
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

                    _state.value = _state.value!!.copy(
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

                    _state.value = _state.value!!.copy(
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

            is CatalogEvent.NavigateBack -> {

                val current = _state.value!!

                when(current.level){

                    CatalogLevel.PRODUCTS -> {
                        _state.value = current.copy(
                            level = CatalogLevel.BRANDS,
                            products = emptyList(),
                            selectedBrandId = null
                        )
                    }

                    CatalogLevel.BRANDS -> {
                        _state.value = current.copy(
                            level = CatalogLevel.SUBCATEGORIES,
                            brands = emptyList(),
                            selectedSubCategoryId = null
                        )
                    }

                    CatalogLevel.SUBCATEGORIES -> {
                        _state.value = current.copy(
                            level = CatalogLevel.CATEGORIES,
                            subCategories = emptyList(),
                            selectedCategoryId = null
                        )
                    }

                    else -> {}
                }

            }

            else -> {}
        }
    }
}