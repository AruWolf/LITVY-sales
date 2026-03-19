package com.litvy.litvysales.ui.purchases.purchaseCreate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.litvy.litvysales.domain.model.catalog.Brand
import com.litvy.litvysales.domain.model.catalog.Category
import com.litvy.litvysales.domain.model.catalog.Product
import com.litvy.litvysales.domain.model.catalog.SubCategory
import com.litvy.litvysales.domain.model.enums.PurchaseOrderStatus
import com.litvy.litvysales.domain.model.inventory.StockBatch
import com.litvy.litvysales.domain.model.purchases.InvoiceType
import com.litvy.litvysales.domain.model.purchases.Purchase
import com.litvy.litvysales.domain.model.purchases.PurchaseItem
import com.litvy.litvysales.domain.model.purchases.PurchaseOrder
import com.litvy.litvysales.domain.model.purchases.PurchaseOrderItem
import com.litvy.litvysales.domain.model.util.PaymentMethod
import com.litvy.litvysales.domain.useCase.catalog.brand.GetBrandBySubCategoryUseCase
import com.litvy.litvysales.domain.useCase.catalog.category.GetCategoriesUseCase
import com.litvy.litvysales.domain.useCase.catalog.product.GetActiveProductsUseCase
import com.litvy.litvysales.domain.useCase.catalog.subCategory.GetSubCategoriesByCategoryUseCase
import com.litvy.litvysales.domain.useCase.purchases.GetInvoiceTypesUseCase
import com.litvy.litvysales.domain.useCase.purchases.RegisterPurchaseUseCase
import com.litvy.litvysales.domain.useCase.purchases.provider.GetProvidersWithVisitDaysUseCase
import com.litvy.litvysales.domain.useCase.purchases.purchaseOrder.GetPurchaseOrderItemsUseCase
import com.litvy.litvysales.domain.useCase.purchases.purchaseOrder.GetPurchaseOrdersUseCase
import com.litvy.litvysales.domain.useCase.purchases.purchaseOrder.UpdatePurchaseOrderUseCase
import com.litvy.litvysales.domain.useCase.sales.GetPaymentMethodsUseCase
import com.litvy.litvysales.domain.validation.ValidationResult
import com.litvy.litvysales.ui.components.dialog.AddProductDialogEvent
import com.litvy.litvysales.ui.components.dialog.AddProductDialogState
import com.litvy.litvysales.ui.util.model.CatalogOptionUi
import com.litvy.litvysales.ui.util.model.ProductUi
import com.litvy.litvysales.ui.util.model.ProviderUi
import com.litvy.litvysales.ui.util.model.PurchaseItemUi
import com.litvy.litvysales.ui.util.model.PurchaseOrderItemUi
import com.litvy.litvysales.ui.util.model.PurchaseOrderUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.UUID

class PurchaseCreateViewModel(
    private val getProvidersWithVisitDaysUseCase: GetProvidersWithVisitDaysUseCase,
    private val getInvoiceTypesUseCase: GetInvoiceTypesUseCase,
    private val getPaymentMethodsUseCase: GetPaymentMethodsUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getSubCategoriesByCategoryUseCase: GetSubCategoriesByCategoryUseCase,
    private val getBrandBySubCategoryUseCase: GetBrandBySubCategoryUseCase,
    private val getActiveProductsUseCase: GetActiveProductsUseCase,
    private val getPurchaseOrdersUseCase: GetPurchaseOrdersUseCase,
    private val getPurchaseOrderItemsUseCase: GetPurchaseOrderItemsUseCase,
    private val registerPurchaseUseCase: RegisterPurchaseUseCase,
    private val updatePurchaseOrderUseCase: UpdatePurchaseOrderUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(PurchaseCreateState())
    val state: StateFlow<PurchaseCreateState> = _state.asStateFlow()

    private val invoiceTypesByLabel = mutableMapOf<String, InvoiceType>()
    private val paymentMethodsByLabel = mutableMapOf<String, PaymentMethod>()
    private val providersById = linkedMapOf<Int, ProviderUi>()
    private val domainOrdersById = mutableMapOf<Int, PurchaseOrder>()
    private val domainOrderItemsById = mutableMapOf<Int, List<PurchaseOrderItem>>()

    private var categories: List<Category> = emptyList()
    private var subCategories: List<SubCategory> = emptyList()
    private var brands: List<Brand> = emptyList()
    private var products: List<ProductUi> = emptyList()

    private val categoryById = mutableMapOf<Int, Category>()
    private val subCategoryById = mutableMapOf<Int, SubCategory>()
    private val brandById = mutableMapOf<Int, Brand>()

    init {
        loadStaticOptions()
        observeProviders()
        observeCatalog()
        observePurchaseOrders()
    }

    fun onEvent(event: PurchaseCreateEvent) {
        when (event) {
            PurchaseCreateEvent.Cancel -> clearFeedback()
            PurchaseCreateEvent.Confirm -> confirmPurchase()
            PurchaseCreateEvent.DismissFeedback -> clearFeedback()
            PurchaseCreateEvent.OpenAddProductDialog -> openAddProductDialog()
            PurchaseCreateEvent.CloseAddProductDialog -> closeAddProductDialog()
            is PurchaseCreateEvent.RemoveItem -> removeItem(event.itemId)
            is PurchaseCreateEvent.UpdateQuantity -> updateItemQuantity(event.itemId, event.quantity)
            is PurchaseCreateEvent.UpdateUnitPrice -> updateItemUnitPrice(event.itemId, event.price)
            is PurchaseCreateEvent.SelectProvider -> selectProvider(event.provider)
            is PurchaseCreateEvent.UpdateSalesRepName -> {
                _state.update {
                    it.copy(
                        salesRepName = event.value,
                        salesRepError = null,
                        feedbackMessage = null
                    )
                }
            }
            is PurchaseCreateEvent.SelectInvoiceType -> {
                _state.update {
                    it.copy(
                        invoiceType = event.invoiceType,
                        invoiceTypeError = null,
                        feedbackMessage = null
                    )
                }
            }
            is PurchaseCreateEvent.SelectPaymentMethod -> {
                _state.update {
                    it.copy(
                        paymentMethod = event.paymentMethod,
                        paymentMethodError = null,
                        feedbackMessage = null
                    )
                }
            }
            is PurchaseCreateEvent.SelectPurchaseOrder -> handlePurchaseOrderSelection(event.purchaseOrder)
            PurchaseCreateEvent.ApplyPurchaseOrderByMerge -> applyPendingPurchaseOrder(merge = true)
            PurchaseCreateEvent.ApplyPurchaseOrderByOverwrite -> applyPendingPurchaseOrder(merge = false)
            PurchaseCreateEvent.DismissPurchaseOrderConflict -> {
                _state.update {
                    it.copy(
                        pendingPurchaseOrder = null,
                        showOrderConflictDialog = false
                    )
                }
            }
            is PurchaseCreateEvent.AddProductDialog -> handleAddProductDialogEvent(event.event)
        }
    }

    private fun loadStaticOptions() {
        viewModelScope.launch {
            val invoiceTypes = getInvoiceTypesUseCase()
            val paymentMethods = getPaymentMethodsUseCase()

            invoiceTypesByLabel.clear()
            invoiceTypes.forEach { invoiceType ->
                invoiceTypesByLabel[invoiceType.toLabel()] = invoiceType
            }

            paymentMethodsByLabel.clear()
            paymentMethods.forEach { paymentMethod ->
                paymentMethodsByLabel[paymentMethod.name] = paymentMethod
            }

            _state.update {
                it.copy(
                    invoiceTypeOptions = invoiceTypesByLabel.keys.toList(),
                    paymentMethodOptions = paymentMethodsByLabel.keys.toList()
                )
            }
        }
    }

    private fun observeProviders() {
        viewModelScope.launch {
            getProvidersWithVisitDaysUseCase().collect { providers ->
                providersById.clear()
                providers.forEach { entry ->
                    val provider = entry.provider
                    val id = provider.id ?: return@forEach
                    providersById[id] = ProviderUi(
                        id = id,
                        name = provider.name,
                        cuit = provider.cuit,
                        phone = provider.telephoneNumber
                    )
                }

                val selectedProviderId = _state.value.provider?.id
                _state.update { current ->
                    current.copy(
                        providers = providersById.values.toList().sortedBy { it.name },
                        provider = selectedProviderId?.let { providersById[it] } ?: current.provider
                    )
                }
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

            getActiveProductsUseCase().collect { activeProducts ->
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

    private fun observePurchaseOrders() {
        viewModelScope.launch {
            getPurchaseOrdersUseCase().collect { orders ->
                domainOrdersById.clear()
                domainOrdersById.putAll(orders.associateBy { it.id })

                val orderItemsById = mutableMapOf<Int, List<PurchaseOrderItem>>()
                orders.forEach { order ->
                    orderItemsById[order.id] = getPurchaseOrderItemsUseCase(order.id).first()
                }
                domainOrderItemsById.clear()
                domainOrderItemsById.putAll(orderItemsById)

                val uiOrders = orders.map { order ->
                    order.toUi(orderItemsById[order.id].orEmpty())
                }

                _state.update { current ->
                    val selectedOrderId = current.selectedPurchaseOrder?.id
                    val pendingOrderId = current.pendingPurchaseOrder?.id
                    current.copy(
                        purchaseOrders = uiOrders,
                        selectedPurchaseOrder = selectedOrderId?.let { id -> uiOrders.firstOrNull { it.id == id } },
                        pendingPurchaseOrder = pendingOrderId?.let { id -> uiOrders.firstOrNull { it.id == id } }
                    )
                }
            }
        }
    }

    private fun selectProvider(provider: ProviderUi) {
        _state.update { current ->
            val selectedOrder = current.selectedPurchaseOrder
            current.copy(
                provider = provider,
                providerError = null,
                feedbackMessage = null,
                selectedPurchaseOrder = selectedOrder?.takeIf { it.providerId == provider.id }
            )
        }
    }

    private fun handlePurchaseOrderSelection(order: PurchaseOrderUi?) {
        if (order == null) {
            _state.update {
                it.copy(
                    selectedPurchaseOrder = null,
                    pendingPurchaseOrder = null,
                    showOrderConflictDialog = false
                )
            }
            return
        }

        if (_state.value.items.isNotEmpty()) {
            _state.update {
                it.copy(
                    pendingPurchaseOrder = order,
                    showOrderConflictDialog = true
                )
            }
            return
        }

        applyPurchaseOrder(order, merge = false)
    }

    private fun applyPendingPurchaseOrder(merge: Boolean) {
        val pending = _state.value.pendingPurchaseOrder ?: return
        applyPurchaseOrder(pending, merge)
    }

    private fun applyPurchaseOrder(order: PurchaseOrderUi, merge: Boolean) {
        val provider = providersById[order.providerId] ?: ProviderUi(
            id = order.providerId,
            name = order.providerName
        )

        val orderItems = order.items.map { item ->
            PurchaseItemUi(
                uiId = "order-${order.id}-${item.productId}",
                productId = item.productId,
                productName = item.productName,
                quantity = item.quantity,
                unitPrice = item.suggestedUnitPriceInCents ?: 0L,
                total = (item.quantity * (item.suggestedUnitPriceInCents ?: 0L)).toLong()
            )
        }

        _state.update { current ->
            val items = if (merge) {
                mergeItems(current.items, orderItems)
            } else {
                orderItems
            }

            current.copy(
                provider = provider,
                selectedPurchaseOrder = order,
                pendingPurchaseOrder = null,
                showOrderConflictDialog = false,
                items = items,
                providerError = null,
                itemsError = null,
                feedbackMessage = null
            ).withRecalculatedTotals()
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

    private fun removeItem(itemId: String) {
        _state.update {
            it.copy(
                items = it.items.filterNot { item -> item.uiId == itemId },
                itemsError = null,
                feedbackMessage = null
            ).withRecalculatedTotals()
        }
    }

    private fun updateItemQuantity(itemId: String, quantity: String) {
        val parsedQuantity = quantity.toDoubleOrNull()
        _state.update { current ->
            val updatedItems = current.items.map { item ->
                if (item.uiId != itemId || parsedQuantity == null || parsedQuantity <= 0.0) {
                    item
                } else {
                    item.copy(
                        quantity = parsedQuantity,
                        total = (parsedQuantity * item.unitPrice).toLong()
                    )
                }
            }

            current.copy(items = updatedItems).withRecalculatedTotals()
        }
    }

    private fun updateItemUnitPrice(itemId: String, price: String) {
        val parsedPrice = price.toLongOrNull()
        _state.update { current ->
            val updatedItems = current.items.map { item ->
                if (item.uiId != itemId || parsedPrice == null || parsedPrice < 0L) {
                    item
                } else {
                    item.copy(
                        unitPrice = parsedPrice,
                        total = (item.quantity * parsedPrice).toLong()
                    )
                }
            }

            current.copy(items = updatedItems).withRecalculatedTotals()
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
                _state.update {
                    it.copy(
                        addProductState = it.addProductState.copy(
                            selectedProduct = event.product,
                            unitPrice = event.product.purchasePrice.toString(),
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

        val newItem = PurchaseItemUi(
            uiId = UUID.randomUUID().toString(),
            productId = selectedProduct!!.id,
            productName = selectedProduct.name,
            quantity = quantity!!,
            unitPrice = unitPrice!!,
            total = (quantity * unitPrice).toLong()
        )

        _state.update {
            it.copy(
                showAddProductDialog = false,
                items = mergeItems(it.items, listOf(newItem)),
                itemsError = null,
                feedbackMessage = null,
                addProductState = AddProductDialogState(
                    categories = categories.toCategoryOptions()
                )
            ).withRecalculatedTotals()
        }
    }

    private fun confirmPurchase() {
        val current = _state.value
        val provider = current.provider
        val invoiceType = invoiceTypesByLabel[current.invoiceType]
        val paymentMethod = paymentMethodsByLabel[current.paymentMethod]

        if (provider == null || invoiceType == null || paymentMethod == null) {
            _state.update {
                it.copy(
                    providerError = if (provider == null) "Selecciona un proveedor." else null,
                    invoiceTypeError = if (invoiceType == null) "Selecciona un tipo de factura." else null,
                    paymentMethodError = if (paymentMethod == null) "Selecciona un metodo de pago." else null,
                    salesRepError = if (it.salesRepName.isBlank()) "Ingresa el vendedor o preventista." else null,
                    itemsError = if (it.items.isEmpty()) "Agrega al menos un producto." else null
                )
            }
            return
        }

        _state.update { it.copy(isSubmitting = true, feedbackMessage = null) }

        viewModelScope.launch {
            val now = System.currentTimeMillis()
            val purchase = Purchase(
                providerId = provider.id,
                salesRepName = current.salesRepName.trim(),
                invoiceTypeId = invoiceType.id,
                paymentMethodId = paymentMethod.id,
                subtotalInCents = current.subtotalInCents,
                totalDiscountInCents = 0L,
                totalTaxInCents = current.taxInCents,
                totalInCents = current.totalInCents,
                createdAt = now,
                createdBy = DEFAULT_USER_ID
            )

            val purchaseItems = current.items.map { item ->
                PurchaseItem(
                    purchaseId = 0,
                    productId = item.productId,
                    quantity = item.quantity,
                    unitPriceInCents = item.unitPrice
                )
            }

            val batches = current.items.map { item ->
                StockBatch(
                    productId = item.productId,
                    quantity = item.quantity,
                    expirationDate = null,
                    purchaseItemId = null,
                    createdAt = now
                )
            }

            when (val result = registerPurchaseUseCase(purchase, purchaseItems, batches)) {
                ValidationResult.Success -> {
                    updateSelectedOrderAsReceived()
                    _state.update {
                        PurchaseCreateState(
                            providers = it.providers,
                            invoiceTypeOptions = it.invoiceTypeOptions,
                            paymentMethodOptions = it.paymentMethodOptions,
                            purchaseOrders = it.purchaseOrders,
                            feedbackMessage = "Compra registrada correctamente."
                        )
                    }
                }
                is ValidationResult.Failure -> {
                    val errors = result.errors.associate { issue -> issue.field to issue.message }
                    _state.update {
                        it.copy(
                            isSubmitting = false,
                            providerError = errors["providerId"] ?: errors["provider"],
                            salesRepError = errors["salesRepName"],
                            invoiceTypeError = errors["invoiceTypeId"] ?: errors["invoiceType"],
                            paymentMethodError = errors["paymentMethodId"] ?: errors["paymentMethod"],
                            itemsError = errors["items"],
                            feedbackMessage = "Revisa los datos de la compra."
                        )
                    }
                }
            }
        }
    }

    private suspend fun updateSelectedOrderAsReceived() {
        val selectedOrder = _state.value.selectedPurchaseOrder ?: return
        val domainOrder = domainOrdersById[selectedOrder.id] ?: return
        val items = domainOrderItemsById[selectedOrder.id].orEmpty()

        if (items.isEmpty() || domainOrder.status == PurchaseOrderStatus.CANCELLED) {
            return
        }

        updatePurchaseOrderUseCase(
            purchaseOrder = domainOrder.copy(status = PurchaseOrderStatus.RECEIVED),
            items = items
        )
    }

    private fun clearFeedback() {
        _state.update { it.copy(feedbackMessage = null) }
    }

    private fun mergeItems(
        currentItems: List<PurchaseItemUi>,
        incomingItems: List<PurchaseItemUi>
    ): List<PurchaseItemUi> {
        val merged = currentItems.associateBy { it.productId }.toMutableMap()

        incomingItems.forEach { incoming ->
            val existing = merged[incoming.productId]
            merged[incoming.productId] = if (existing == null) {
                incoming
            } else {
                val quantity = existing.quantity + incoming.quantity
                val unitPrice = if (incoming.unitPrice > 0L) incoming.unitPrice else existing.unitPrice
                existing.copy(
                    quantity = quantity,
                    unitPrice = unitPrice,
                    total = (quantity * unitPrice).toLong()
                )
            }
        }

        return merged.values.sortedBy { it.productName }
    }

    private fun PurchaseCreateState.withRecalculatedTotals(): PurchaseCreateState {
        val subtotal = items.sumOf { it.total }
        return copy(
            subtotalInCents = subtotal,
            taxInCents = 0L,
            totalInCents = subtotal
        )
    }

    private fun PurchaseCreateState.refreshDialogProducts(): PurchaseCreateState {
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

    private fun Product.toUiOrNull(): ProductUi? {
        val brand = brandById[brandId] ?: return null
        val subCategory = subCategoryById[brand.subCategoryId] ?: return null
        val category = categoryById[subCategory.categoryId] ?: return null

        return ProductUi(
            id = id ?: return null,
            name = name,
            barcode = null,
            purchasePrice = purchasePriceInCents,
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

    private fun InvoiceType.toLabel(): String {
        return if (description.isBlank() || description.equals(code, ignoreCase = true)) {
            code
        } else {
            "$code - $description"
        }
    }

    private fun PurchaseOrder.toUi(items: List<PurchaseOrderItem>): PurchaseOrderUi {
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale("es", "AR"))
        val providerName = providersById[providerId]?.name ?: "Proveedor #$providerId"

        return PurchaseOrderUi(
            id = id,
            providerId = providerId,
            providerName = providerName,
            status = status.name,
            expectedDeliveryLabel = expectedDeliveryDate?.let { formatter.format(it) },
            items = items.map { item ->
                PurchaseOrderItemUi(
                    productId = item.productId,
                    productName = products.firstOrNull { product -> product.id == item.productId }?.name
                        ?: "Producto #${item.productId}",
                    quantity = item.quantity,
                    suggestedUnitPriceInCents = products.firstOrNull { product -> product.id == item.productId }?.purchasePrice
                )
            }
        )
    }

    companion object {
        private const val DEFAULT_USER_ID = 1
    }
}

class PurchaseCreateViewModelFactory(
    private val getProvidersWithVisitDaysUseCase: GetProvidersWithVisitDaysUseCase,
    private val getInvoiceTypesUseCase: GetInvoiceTypesUseCase,
    private val getPaymentMethodsUseCase: GetPaymentMethodsUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getSubCategoriesByCategoryUseCase: GetSubCategoriesByCategoryUseCase,
    private val getBrandBySubCategoryUseCase: GetBrandBySubCategoryUseCase,
    private val getActiveProductsUseCase: GetActiveProductsUseCase,
    private val getPurchaseOrdersUseCase: GetPurchaseOrdersUseCase,
    private val getPurchaseOrderItemsUseCase: GetPurchaseOrderItemsUseCase,
    private val registerPurchaseUseCase: RegisterPurchaseUseCase,
    private val updatePurchaseOrderUseCase: UpdatePurchaseOrderUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PurchaseCreateViewModel(
            getProvidersWithVisitDaysUseCase = getProvidersWithVisitDaysUseCase,
            getInvoiceTypesUseCase = getInvoiceTypesUseCase,
            getPaymentMethodsUseCase = getPaymentMethodsUseCase,
            getCategoriesUseCase = getCategoriesUseCase,
            getSubCategoriesByCategoryUseCase = getSubCategoriesByCategoryUseCase,
            getBrandBySubCategoryUseCase = getBrandBySubCategoryUseCase,
            getActiveProductsUseCase = getActiveProductsUseCase,
            getPurchaseOrdersUseCase = getPurchaseOrdersUseCase,
            getPurchaseOrderItemsUseCase = getPurchaseOrderItemsUseCase,
            registerPurchaseUseCase = registerPurchaseUseCase,
            updatePurchaseOrderUseCase = updatePurchaseOrderUseCase
        ) as T
    }
}
