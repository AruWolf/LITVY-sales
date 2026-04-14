package com.litvy.litvysales.ui.sales

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.litvy.litvysales.domain.model.catalog.Brand
import com.litvy.litvysales.domain.model.catalog.Category
import com.litvy.litvysales.domain.model.catalog.Product
import com.litvy.litvysales.domain.model.catalog.SubCategory
import com.litvy.litvysales.domain.model.enums.SaleStatus
import com.litvy.litvysales.domain.model.sales.Sale
import com.litvy.litvysales.domain.model.sales.SaleItem
import com.litvy.litvysales.domain.model.sales.SalePayment
import com.litvy.litvysales.domain.model.util.PaymentMethod
import com.litvy.litvysales.domain.useCase.catalog.brand.GetBrandBySubCategoryUseCase
import com.litvy.litvysales.domain.useCase.catalog.category.GetCategoriesUseCase
import com.litvy.litvysales.domain.useCase.catalog.product.GetActiveProductsUseCase
import com.litvy.litvysales.domain.useCase.catalog.subCategory.GetSubCategoriesByCategoryUseCase
import com.litvy.litvysales.domain.useCase.sales.CreateSaleUseCase
import com.litvy.litvysales.domain.useCase.sales.CreateSaleWithCashSessionUseCase
import com.litvy.litvysales.domain.useCase.sales.ValidateSaleUseCase
import com.litvy.litvysales.domain.useCase.sales.paymentmethod.GetPaymentMethodsUseCase
import com.litvy.litvysales.domain.validation.ValidationResult
import com.litvy.litvysales.ui.components.dialog.AddProductDialogEvent
import com.litvy.litvysales.ui.components.dialog.AddProductDialogState
import com.litvy.litvysales.ui.sales.model.EditSaleItemState
import com.litvy.litvysales.ui.sales.model.SaleDraft
import com.litvy.litvysales.ui.sales.model.SaleItemDraft
import com.litvy.litvysales.ui.sales.model.SalePaymentDraft
import com.litvy.litvysales.ui.util.model.CatalogOptionUi
import com.litvy.litvysales.ui.util.model.ProductUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.roundToLong

class SalesViewModel(
    private val createSaleUseCase: CreateSaleWithCashSessionUseCase,
    private val validateSaleUseCase: ValidateSaleUseCase,
    private val getPaymentMethodsUseCase: GetPaymentMethodsUseCase,
    private val getProductsUseCase: GetActiveProductsUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getSubCategoriesByCategoryUseCase: GetSubCategoriesByCategoryUseCase,
    private val getBrandBySubCategoryUseCase: GetBrandBySubCategoryUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SalesState())
    val state: StateFlow<SalesState> = _state.asStateFlow()

    private val paymentMethodsById = mutableMapOf<Int, PaymentMethod>()

    private var categories: List<Category> = emptyList()
    private var subCategories: List<SubCategory> = emptyList()
    private var brands: List<Brand> = emptyList()
    private var products: List<ProductUi> = emptyList()

    private val categoryById = mutableMapOf<Int, Category>()
    private val subCategoryById = mutableMapOf<Int, SubCategory>()
    private val brandById = mutableMapOf<Int, Brand>()

    init {
        loadPaymentMethods()
        observeCatalog()
    }

    fun onEvent(event: SalesEvent) {
        when (event) {
            SalesEvent.OpenAddProductDialog -> openAddProductDialog()
            SalesEvent.CloseAddProductDialog -> closeAddProductDialog()
            is SalesEvent.AddProductDialog -> handleAddProductDialogEvent(event.event)
            is SalesEvent.OpenEditItemDialog -> openEditItemDialog(event.productId)
            SalesEvent.CloseEditItemDialog -> closeEditItemDialog()
            is SalesEvent.UpdateEditItemQuantity -> updateEditItemQuantity(event.value)
            is SalesEvent.UpdateEditItemUnitPrice -> updateEditItemUnitPrice(event.value)
            SalesEvent.SaveEditedItem -> saveEditedItem()
            is SalesEvent.DeleteItem -> deleteItem(event.productId)
            is SalesEvent.SelectPaymentMethod -> selectPaymentMethod(event.method)
            SalesEvent.ConfirmSale -> confirmSale()
            SalesEvent.DismissFeedback -> dismissFeedback()
        }
    }

    private fun loadPaymentMethods() {
        viewModelScope.launch {
            val methods = getPaymentMethodsUseCase()
            paymentMethodsById.clear()
            paymentMethodsById.putAll(methods.associateBy { it.id })

            _state.update { current ->
                current.copy(
                    paymentMethods = methods
                ).withRecalculatedDraft(current.draft.items, current.selectedPaymentMethodId)
            }
        }
    }

    private fun observeCatalog() {
        viewModelScope.launch {
            categories = getCategoriesUseCase()
            subCategories = categories.flatMap { category ->
                val categoryId = category.id ?: return@flatMap emptyList()
                getSubCategoriesByCategoryUseCase(categoryId)
            }
            brands = subCategories.flatMap { subCategory ->
                val subCategoryId = subCategory.id ?: return@flatMap emptyList()
                getBrandBySubCategoryUseCase(subCategoryId)
            }

            categoryById.clear()
            categoryById.putAll(categories.mapNotNull { category -> category.id?.let { it to category } })
            subCategoryById.clear()
            subCategoryById.putAll(subCategories.mapNotNull { subCategory -> subCategory.id?.let { it to subCategory } })
            brandById.clear()
            brandById.putAll(brands.mapNotNull { brand -> brand.id?.let { it to brand } })

            getProductsUseCase().collect { activeProducts ->
                products = activeProducts.mapNotNull { product -> product.toUiOrNull() }

                _state.update { current ->
                    current.copy(
                        addProductState = current.addProductState.copy(
                            categories = categories.toCategoryOptions(),
                            subCategories = current.addProductState.selectedCategoryId
                                ?.let { selected -> subCategories.filter { it.categoryId == selected }.toSubCategoryOptions() }
                                ?: emptyList(),
                            brands = current.addProductState.selectedSubCategoryId
                                ?.let { selected -> brands.filter { it.subCategoryId == selected }.toBrandOptions() }
                                ?: emptyList()
                        )
                    ).refreshDialogProducts()
                }
            }
        }
    }

    private fun openAddProductDialog() {
        _state.update {
            it.copy(
                showAddProductDialog = true,
                addProductState = AddProductDialogState(
                    categories = categories.toCategoryOptions(),
                    products = products,
                    showResults = true
                )
            )
        }
    }

    private fun closeAddProductDialog() {
        _state.update {
            it.copy(
                showAddProductDialog = false,
                addProductState = AddProductDialogState(
                    categories = categories.toCategoryOptions()
                )
            )
        }
    }

    private fun handleAddProductDialogEvent(event: AddProductDialogEvent) {
        when (event) {
            AddProductDialogEvent.Cancel -> closeAddProductDialog()
            AddProductDialogEvent.Confirm -> confirmAddProduct()
            AddProductDialogEvent.ScanBarcode -> {
                _state.update {
                    it.copy(
                        addProductState = it.addProductState.copy(
                            searchError = "El escaneo de codigo de barras queda pendiente para una siguiente iteracion."
                        )
                    )
                }
            }
            is AddProductDialogEvent.SearchChanged -> {
                _state.update {
                    it.copy(
                        addProductState = it.addProductState.copy(
                            searchQuery = event.query,
                            searchError = null
                        )
                    ).refreshDialogProducts()
                }
            }
            is AddProductDialogEvent.SelectCategory -> {
                _state.update {
                    val filteredSubCategories = event.categoryId
                        ?.let { selected -> subCategories.filter { it.categoryId == selected }.toSubCategoryOptions() }
                        ?: emptyList()

                    it.copy(
                        addProductState = it.addProductState.copy(
                            selectedCategoryId = event.categoryId,
                            selectedSubCategoryId = null,
                            selectedBrandId = null,
                            subCategories = filteredSubCategories,
                            brands = emptyList(),
                            selectedProduct = null,
                            quantity = "1",
                            unitPrice = ""
                        )
                    ).refreshDialogProducts()
                }
            }
            is AddProductDialogEvent.SelectSubCategory -> {
                _state.update {
                    val filteredBrands = event.subCategoryId
                        ?.let { selected -> brands.filter { it.subCategoryId == selected }.toBrandOptions() }
                        ?: emptyList()

                    it.copy(
                        addProductState = it.addProductState.copy(
                            selectedSubCategoryId = event.subCategoryId,
                            selectedBrandId = null,
                            brands = filteredBrands,
                            selectedProduct = null,
                            quantity = "1",
                            unitPrice = ""
                        )
                    ).refreshDialogProducts()
                }
            }
            is AddProductDialogEvent.SelectBrand -> {
                _state.update {
                    it.copy(
                        addProductState = it.addProductState.copy(
                            selectedBrandId = event.brandId,
                            selectedProduct = null,
                            quantity = "1",
                            unitPrice = ""
                        )
                    ).refreshDialogProducts()
                }
            }
            is AddProductDialogEvent.SelectProduct -> {
                val suggestedPrice = event.product.salePrice ?: event.product.purchasePrice
                _state.update {
                    it.copy(
                        addProductState = it.addProductState.copy(
                            selectedProduct = event.product,
                            unitPrice = suggestedPrice.toString(),
                            searchError = null,
                            quantityError = null,
                            unitPriceError = null
                        )
                    )
                }
            }
            is AddProductDialogEvent.QuantityChanged -> {
                _state.update {
                    it.copy(
                        addProductState = it.addProductState.copy(
                            quantity = event.value,
                            quantityError = null
                        )
                    )
                }
            }
            is AddProductDialogEvent.PriceChanged -> {
                _state.update {
                    it.copy(
                        addProductState = it.addProductState.copy(
                            unitPrice = event.value,
                            unitPriceError = null
                        )
                    )
                }
            }
        }
    }

    private fun confirmAddProduct() {
        val dialogState = _state.value.addProductState
        val selectedProduct = dialogState.selectedProduct
        val quantity = dialogState.quantity.toDoubleOrNull()
        val unitPrice = dialogState.unitPrice.toLongOrNull()

        val searchError = if (selectedProduct == null) {
            "Selecciona un producto antes de continuar."
        } else {
            null
        }
        val quantityError = if (quantity == null || quantity <= 0.0) {
            "Ingresa una cantidad valida."
        } else {
            null
        }
        val unitPriceError = if (unitPrice == null || unitPrice <= 0L) {
            "Ingresa un precio valido en centavos."
        } else {
            null
        }

        if (searchError != null || quantityError != null || unitPriceError != null) {
            _state.update {
                it.copy(
                    addProductState = dialogState.copy(
                        searchError = searchError,
                        quantityError = quantityError,
                        unitPriceError = unitPriceError
                    )
                )
            }
            return
        }

        val newItem = SaleItemDraft(
            productId = selectedProduct!!.id,
            name = selectedProduct.name,
            quantity = quantity!!,
            unitPrice = unitPrice!!,
            total = (quantity * unitPrice).roundToLong()
        )

        _state.update {
            it.copy(
                showAddProductDialog = false,
                addProductState = AddProductDialogState(
                    categories = categories.toCategoryOptions()
                ),
                itemsError = null,
                feedbackMessage = null
            ).withRecalculatedDraft(
                items = mergeItems(it.draft.items, newItem),
                paymentMethodId = it.selectedPaymentMethodId
            )
        }
    }

    private fun openEditItemDialog(productId: Int) {
        val item = _state.value.draft.items.firstOrNull { it.productId == productId } ?: return
        _state.update {
            it.copy(
                showEditItemDialog = true,
                editItemState = EditSaleItemState(
                    productId = item.productId,
                    name = item.name,
                    quantity = item.quantity.toDisplayString(),
                    unitPrice = item.unitPrice.toString()
                )
            )
        }
    }

    private fun closeEditItemDialog() {
        _state.update {
            it.copy(
                showEditItemDialog = false,
                editItemState = null
            )
        }
    }

    private fun updateEditItemQuantity(value: String) {
        _state.update { current ->
            current.copy(
                editItemState = current.editItemState?.copy(
                    quantity = value,
                    quantityError = null
                )
            )
        }
    }

    private fun updateEditItemUnitPrice(value: String) {
        _state.update { current ->
            current.copy(
                editItemState = current.editItemState?.copy(
                    unitPrice = value,
                    unitPriceError = null
                )
            )
        }
    }

    private fun saveEditedItem() {
        val editState = _state.value.editItemState ?: return
        val quantity = editState.quantity.toDoubleOrNull()
        val unitPrice = editState.unitPrice.toLongOrNull()

        val quantityError = if (quantity == null || quantity <= 0.0) {
            "Ingresa una cantidad valida."
        } else {
            null
        }
        val unitPriceError = if (unitPrice == null || unitPrice <= 0L) {
            "Ingresa un precio valido en centavos."
        } else {
            null
        }

        if (quantityError != null || unitPriceError != null) {
            _state.update {
                it.copy(
                    editItemState = editState.copy(
                        quantityError = quantityError,
                        unitPriceError = unitPriceError
                    )
                )
            }
            return
        }

        _state.update { current ->
            val updatedItems = current.draft.items.map { item ->
                if (item.productId != editState.productId) {
                    item
                } else {
                    item.copy(
                        quantity = quantity!!,
                        unitPrice = unitPrice!!,
                        total = (quantity * unitPrice).roundToLong()
                    )
                }
            }

            current.copy(
                showEditItemDialog = false,
                editItemState = null,
                feedbackMessage = null
            ).withRecalculatedDraft(updatedItems, current.selectedPaymentMethodId)
        }
    }

    private fun deleteItem(productId: Int) {
        _state.update { current ->
            val editingSameItem = current.editItemState?.productId == productId
            current.copy(
                itemsError = null,
                feedbackMessage = null,
                showEditItemDialog = if (editingSameItem) false else current.showEditItemDialog,
                editItemState = if (editingSameItem) null else current.editItemState
            ).withRecalculatedDraft(
                items = current.draft.items.filterNot { it.productId == productId },
                paymentMethodId = current.selectedPaymentMethodId
            )
        }
    }

    private fun selectPaymentMethod(method: PaymentMethod) {
        _state.update {
            it.copy(
                paymentMethodError = null,
                feedbackMessage = null
            ).withRecalculatedDraft(it.draft.items, method.id)
        }
    }

    private fun confirmSale() {
        val current = _state.value
        val selectedPaymentMethodId = current.selectedPaymentMethodId

        val itemsError = if (current.draft.items.isEmpty()) {
            "Debe agregar al menos un producto."
        } else {
            null
        }
        val paymentMethodError = if (selectedPaymentMethodId == null) {
            "Debe seleccionar un metodo de pago."
        } else {
            null
        }

        if (itemsError != null || paymentMethodError != null) {
            _state.update {
                it.copy(
                    itemsError = itemsError,
                    paymentMethodError = paymentMethodError,
                    feedbackMessage = "Revisa los datos de la venta."
                )
            }
            return
        }

        val sale = Sale(
            cashSessionId = 0,
            sellerId = 1,
            customerId = null,
            totalDiscountInCents = 0,
            totalInCents = current.draft.total,
            status = SaleStatus.CONFIRMED,
            cancellationReason = null,
            createdAt = System.currentTimeMillis()
        )

        val items = current.draft.items.map {
            SaleItem(
                saleId = 0,
                productId = it.productId,
                quantity = it.quantity,
                unitPriceInCents = it.unitPrice,
                totalInCents = it.total,
                originalUnitPriceInCents = it.unitPrice,
                discountAppliedInCents = 0
            )
        }

        val payments = current.draft.payments.map {
            SalePayment(
                saleId = 0,
                paymentMethodId = it.paymentMethodId,
                amountInCents = it.amount
            )
        }

        when (val validation = validateSaleUseCase(sale, items, payments)) {
            ValidationResult.Success -> submitSale(sale, items, payments)
            is ValidationResult.Failure -> {
                val errors = validation.errors.associate { issue -> issue.field to issue.message }
                _state.update {
                    it.copy(
                        itemsError = errors["items"],
                        paymentMethodError = errors["payments"],
                        feedbackMessage = "Revisa los datos de la venta."
                    )
                }
            }
        }
    }

    private fun submitSale(
        sale: Sale,
        items: List<SaleItem>,
        payments: List<SalePayment>
    ) {
        _state.update { it.copy(isSubmitting = true, feedbackMessage = null) }

        viewModelScope.launch {
            try {
                createSaleUseCase(sale, items, payments)
                _state.update { current ->
                    current.copy(
                        draft = SaleDraft(),
                        paymentMethodError = null,
                        itemsError = null,
                        feedbackMessage = "Venta registrada correctamente.",
                        isSubmitting = false,
                        showAddProductDialog = false,
                        showEditItemDialog = false,
                        editItemState = null,
                        addProductState = AddProductDialogState(
                            categories = categories.toCategoryOptions()
                        )
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isSubmitting = false,
                        feedbackMessage = e.message ?: "Error al crear la venta."
                    )
                }
            }
        }
    }

    private fun dismissFeedback() {
        _state.update { it.copy(feedbackMessage = null) }
    }

    private fun SalesState.withRecalculatedDraft(
        items: List<SaleItemDraft>,
        paymentMethodId: Int?
    ): SalesState {
        val paymentMethod = paymentMethodId?.let(paymentMethodsById::get)
        val subtotal = items.sumOf { it.total }
        val surcharge = paymentMethod.calculateSurcharge(subtotal)
        val total = subtotal + surcharge
        val payments = if (paymentMethod == null) {
            emptyList()
        } else {
            listOf(
                SalePaymentDraft(
                    paymentMethodId = paymentMethod.id,
                    name = paymentMethod.name,
                    amount = total
                )
            )
        }

        return copy(
            draft = SaleDraft(
                items = items,
                payments = payments,
                subtotal = subtotal,
                surcharge = surcharge,
                total = total
            )
        )
    }

    private fun SalesState.refreshDialogProducts(): SalesState {
        val filteredProducts = products.filter { product ->
            (addProductState.selectedCategoryId == null || product.categoryId == addProductState.selectedCategoryId) &&
                (addProductState.selectedSubCategoryId == null || product.subCategoryId == addProductState.selectedSubCategoryId) &&
                (addProductState.selectedBrandId == null || product.brandId == addProductState.selectedBrandId) &&
                (
                    addProductState.searchQuery.isBlank() ||
                        product.name.contains(addProductState.searchQuery, ignoreCase = true) ||
                        product.barcode?.contains(addProductState.searchQuery, ignoreCase = true) == true
                    )
        }

        return copy(
            addProductState = addProductState.copy(
                products = filteredProducts,
                showResults = true
            )
        )
    }

    private fun mergeItems(
        currentItems: List<SaleItemDraft>,
        incomingItem: SaleItemDraft
    ): List<SaleItemDraft> {
        val existing = currentItems.firstOrNull { it.productId == incomingItem.productId }

        if (existing == null) {
            return (currentItems + incomingItem).sortedBy { it.name }
        }

        val mergedQuantity = existing.quantity + incomingItem.quantity
        val mergedUnitPrice = incomingItem.unitPrice

        return currentItems.map { item ->
            if (item.productId != incomingItem.productId) {
                item
            } else {
                item.copy(
                    quantity = mergedQuantity,
                    unitPrice = mergedUnitPrice,
                    total = (mergedQuantity * mergedUnitPrice).roundToLong()
                )
            }
        }.sortedBy { it.name }
    }

    private fun PaymentMethod?.calculateSurcharge(subtotal: Long): Long {
        if (this == null || surchargePercentage <= 0.0 || subtotal <= 0L) {
            return 0L
        }
        return (subtotal * (surchargePercentage / 100.0)).roundToLong()
    }

    private fun Double.toDisplayString(): String {
        return if (this % 1.0 == 0.0) {
            toInt().toString()
        } else {
            toString()
        }
    }

    private fun Product.toUiOrNull(): ProductUi? {
        val brand = brandById[brandId] ?: return null
        val subCategory = subCategoryById[brand.subCategoryId] ?: return null
        val category = categoryById[subCategory.categoryId] ?: return null

        return ProductUi(
            id = id ?: return null,
            name = name,
            barcode = null,
            purchasePrice = purchasePriceInCents,
            salePrice = salePriceInCents,
            categoryId = category.id ?: return null,
            categoryName = category.name,
            subCategoryId = subCategory.id ?: return null,
            subCategoryName = subCategory.name,
            brandId = brand.id ?: return null,
            brandName = brand.name,
            active = active
        )
    }

    private fun List<Category>.toCategoryOptions(): List<CatalogOptionUi> =
        mapNotNull { category -> category.id?.let { CatalogOptionUi(it, category.name) } }

    private fun List<SubCategory>.toSubCategoryOptions(): List<CatalogOptionUi> =
        mapNotNull { subCategory -> subCategory.id?.let { CatalogOptionUi(it, subCategory.name) } }

    private fun List<Brand>.toBrandOptions(): List<CatalogOptionUi> =
        mapNotNull { brand -> brand.id?.let { CatalogOptionUi(it, brand.name) } }
}

class SalesViewModelFactory(
    private val createSaleUseCase: CreateSaleWithCashSessionUseCase,
    private val validateSaleUseCase: ValidateSaleUseCase,
    private val getPaymentMethodsUseCase: GetPaymentMethodsUseCase,
    private val getProductsUseCase: GetActiveProductsUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getSubCategoriesByCategoryUseCase: GetSubCategoriesByCategoryUseCase,
    private val getBrandBySubCategoryUseCase: GetBrandBySubCategoryUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SalesViewModel(
            createSaleUseCase = createSaleUseCase,
            validateSaleUseCase = validateSaleUseCase,
            getPaymentMethodsUseCase = getPaymentMethodsUseCase,
            getProductsUseCase = getProductsUseCase,
            getCategoriesUseCase = getCategoriesUseCase,
            getSubCategoriesByCategoryUseCase = getSubCategoriesByCategoryUseCase,
            getBrandBySubCategoryUseCase = getBrandBySubCategoryUseCase
        ) as T
    }
}
