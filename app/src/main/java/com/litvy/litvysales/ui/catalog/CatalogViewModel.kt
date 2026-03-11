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
import com.litvy.litvysales.domain.validation.ValidationResult
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

            // __________________
            // --- NAVEGACIÓN ---
            // __________________

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

            // __________________
            // --- CATEGORIAS ---
            // __________________

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

            is CatalogEvent.CreateCategory -> {

                viewModelScope.launch {

                    val result = createCategory(event.name)

                    if(handleValidationResult(result)) {

                        val categories = getCategories()

                        _state.value = _state.value.copy(
                            categories = categories
                        )

                    }

                }

            }

            is CatalogEvent.UpdateCategory -> {
                viewModelScope.launch {

                    val result = updateCategory(
                        event.id,
                        event.name
                    )

                    if(handleValidationResult(result)) {
                        val categories = getCategories()

                        _state.value = _state.value.copy(
                            categories = categories
                        )
                    }
                }
            }

            // _____________________
            // --- SUBCATEGORIAS ---
            // _____________________

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

            is CatalogEvent.CreateSubCategory -> {

                viewModelScope.launch {

                    val result = createSubCategory(
                        event.name,
                        event.categoryId
                    )

                    if(handleValidationResult(result)) {
                        val subs = getSubCategories(event.categoryId)

                        _state.value = _state.value.copy(
                            subCategories = subs
                        )
                    }
                }
            }

            is CatalogEvent.UpdateSubCategory -> {
                viewModelScope.launch {

                    val result = updateSubCategory(
                        event.id,
                        event.name
                    )

                    if(handleValidationResult(result)) {
                        val subCategories = getSubCategories(event.categoryId)

                        _state.value = _state.value.copy(
                            subCategories = subCategories
                        )
                    }
                }
            }

            // ______________
            // --- MARCAS ---
            // ______________

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

            is CatalogEvent.CreateBrand -> {

                viewModelScope.launch {

                    val result = createBrand(
                        event.name,
                        event.subCategoryId
                    )

                    if(handleValidationResult(result)) {
                        val brands =
                            getBrands(event.subCategoryId)

                        _state.value = _state.value.copy(
                            brands = brands
                        )
                    }
                }
            }

            is CatalogEvent.UpdateBrand -> {
                viewModelScope.launch {

                    val result = updateBrand(
                        event.id,
                        event.name
                    )

                    if(handleValidationResult(result)) {
                        val brands = getBrands(event.subCategoryId)

                        _state.value = _state.value.copy(
                            brands = brands
                        )
                    }
                }
            }

            // _________________
            // --- PRODUCTOS ---
            // _________________

            is CatalogEvent.CreateProduct -> {

                viewModelScope.launch {

                    val result = createProduct(
                        event.name,
                        event.brandId,
                        event.purchasePrice,
                        event.salePrice,
                        event.hasExpiration,
                        event.isWeighable
                    )

                    if(handleValidationResult(result)){

                        getProducts(event.brandId).collect { products ->
                            _state.value = _state.value.copy(
                                products = products
                            )
                        }
                    }
                }
            }

            is CatalogEvent.UpdateProduct -> {

                viewModelScope.launch {

                    val result = updateProduct(
                        event.id,
                        event.name,
                        event.brandId,
                        event.purchasePrice,
                        event.salePrice,
                        event.hasExpiration,
                        event.isWeighable
                    )

                    if(handleValidationResult(result)){

                    }
                }
            }

            else -> {}
        }
    }

    private fun handleValidationResult(
        result: ValidationResult
    ): Boolean {

        return when(result){

            is ValidationResult.Success -> {

                _state.value = _state.value.copy(
                    formErrors = emptyMap(),
                    formWarnings = emptyMap(),
                    operationSuccess = true
                )

                true
            }

            is ValidationResult.Failure -> {

                val errors = result.errors.associate {
                    it.field to it.message
                }

                val warnings = result.warnings.associate {
                    it.field to it.message
                }

                _state.value = _state.value.copy(
                    formErrors = errors,
                    formWarnings = warnings
                )

                // Solo bloquea si hay errores
                errors.isEmpty()
            }

        }
    }

    fun resetOperationSuccess(){

        _state.value = _state.value.copy(
            operationSuccess = false
        )

    }

    fun clearFormValidation() {

        _state.value = _state.value.copy(
            formErrors = emptyMap(),
            formWarnings = emptyMap()
        )

    }
}