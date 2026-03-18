package com.litvy.litvysales.ui.purchases.purchaseCreate

import androidx.lifecycle.ViewModel
import com.litvy.litvysales.ui.components.dialog.AddProductDialogEvent
import com.litvy.litvysales.ui.components.dialog.AddProductDialogState
import com.litvy.litvysales.ui.util.model.CatalogOptionUi
import com.litvy.litvysales.ui.util.model.ProductUi
import com.litvy.litvysales.ui.util.model.ProviderUi
import com.litvy.litvysales.ui.util.model.PurchaseItemUi
import com.litvy.litvysales.ui.util.model.PurchaseOrderItemUi
import com.litvy.litvysales.ui.util.model.PurchaseOrderUi
import java.util.UUID
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PurchaseCreateViewModel : ViewModel() {

    private val mockProviders = mockProviders()
    private val mockInvoiceTypes = mockInvoiceTypes()
    private val mockPaymentMethods = mockPaymentMethods()
    private val mockCatalog = mockCatalog()
    private val mockPurchaseOrders = mockPurchaseOrders()

    private val _state = MutableStateFlow(
        PurchaseCreateState(
            providers = emptyList(),
            invoiceTypeOptions = emptyList(),
            paymentMethodOptions = emptyList(),
            purchaseOrders = emptyList(),
            addProductState = AddProductDialogState()
        )
    )
    val state: StateFlow<PurchaseCreateState> = _state.asStateFlow()

    init {
        loadInitialData()
    }

    fun onEvent(event: PurchaseCreateEvent) {
        when (event) {
            PurchaseCreateEvent.Cancel -> clearFeedback("Compra cancelada.")
            PurchaseCreateEvent.Confirm -> confirmPurchase()
            PurchaseCreateEvent.DismissFeedback -> clearFeedback()
            PurchaseCreateEvent.OpenAddProductDialog -> openAddProductDialog()
            PurchaseCreateEvent.CloseAddProductDialog -> closeAddProductDialog()
            PurchaseCreateEvent.ApplyPurchaseOrderByMerge -> applyPendingPurchaseOrder(merge = true)
            PurchaseCreateEvent.ApplyPurchaseOrderByOverwrite -> applyPendingPurchaseOrder(merge = false)
            PurchaseCreateEvent.DismissPurchaseOrderConflict -> dismissPurchaseOrderConflict()
            is PurchaseCreateEvent.RemoveItem -> removeItem(event.itemId)
            is PurchaseCreateEvent.UpdateQuantity -> updateItemQuantity(event.itemId, event.quantity)
            is PurchaseCreateEvent.UpdateUnitPrice -> updateItemUnitPrice(event.itemId, event.price)
            is PurchaseCreateEvent.SelectProvider -> selectProvider(event.provider)
            is PurchaseCreateEvent.SelectInvoiceType -> {
                _state.update {
                    it.copy(invoiceType = event.invoiceType, invoiceTypeError = null)
                }
            }

            is PurchaseCreateEvent.SelectPaymentMethod -> {
                _state.update {
                    it.copy(paymentMethod = event.paymentMethod, paymentMethodError = null)
                }
            }

            is PurchaseCreateEvent.SelectPurchaseOrder -> handlePurchaseOrderSelection(event.purchaseOrder)
            is PurchaseCreateEvent.AddProductDialog -> handleAddProductDialogEvent(event.event)
        }
    }

    private fun loadInitialData() {
        val providers = loadProviders()
        val invoiceTypes = loadInvoiceTypes()
        val paymentMethods = loadPaymentMethods()
        val purchaseOrders = loadPurchaseOrders()

        _state.update {
            it.copy(
                providers = providers,
                invoiceTypeOptions = invoiceTypes,
                paymentMethodOptions = paymentMethods,
                purchaseOrders = purchaseOrders,
                addProductState = createAddProductDialogState()
            )
        }
    }

    private fun loadProviders(): List<ProviderUi> {
        // TODO connect UI with ProviderRepository/GetProvidersUseCase and map domain Provider -> ProviderUi
        return mockProviders
    }

    private fun loadInvoiceTypes(): List<String> {
        // TODO connect UI with InvoiceType use case/repository when the domain contract is available
        return mockInvoiceTypes
    }

    private fun loadPaymentMethods(): List<String> {
        // TODO connect UI with PaymentMethodRepository/GetPaymentMethodsUseCase and map domain PaymentMethod -> UI option
        return mockPaymentMethods
    }

    private fun loadPurchaseOrders(): List<PurchaseOrderUi> {
        // TODO connect UI with purchase order use case/repository and map PurchaseOrder aggregate -> PurchaseOrderUi
        return mockPurchaseOrders
    }

    private fun loadCategories(): List<CatalogOptionUi> {
        // TODO connect UI with GetCategoriesUseCase and map Category -> CatalogOptionUi
        return mockCatalog
            .map { it.categoryId to it.categoryName }
            .distinct()
            .map { (id, name) -> CatalogOptionUi(id = id, name = name) }
            .sortedBy { it.name }
    }

    private fun loadSubCategories(categoryId: Int?): List<CatalogOptionUi> {
        // TODO connect UI with GetSubCategoriesByCategoryUseCase using the selected category id
        if (categoryId == null) return emptyList()

        return mockCatalog
            .filter { it.categoryId == categoryId }
            .map { it.subCategoryId to it.subCategoryName }
            .distinct()
            .map { (id, name) -> CatalogOptionUi(id = id, name = name) }
            .sortedBy { it.name }
    }

    private fun loadBrands(subCategoryId: Int?): List<CatalogOptionUi> {
        // TODO connect UI with GetBrandBySubCategoryUseCase using the selected subcategory id
        if (subCategoryId == null) return emptyList()

        return mockCatalog
            .filter { it.subCategoryId == subCategoryId }
            .map { it.brandId to it.brandName }
            .distinct()
            .map { (id, name) -> CatalogOptionUi(id = id, name = name) }
            .sortedBy { it.name }
    }

    private fun loadProductsByBrand(brandId: Int?): List<ProductUi> {
        // TODO connect UI with GetProductByBrandUseCase and map Product -> ProductUi
        if (brandId == null) return emptyList()
        return mockCatalog.filter { it.brandId == brandId && it.active }.sortedBy { it.name }
    }

    private fun searchProducts(query: String): List<ProductUi> {
        // TODO connect UI with SearchProductsUseCase when replacing the in-memory catalog source
        if (query.isBlank()) return emptyList()

        return mockCatalog.filter { product ->
            product.active && (
                product.name.contains(query, ignoreCase = true) ||
                    product.barcode?.contains(query, ignoreCase = true) == true
                )
        }.sortedBy { it.name }
    }

    private fun createAddProductDialogState(
        selectedCategoryId: Int? = null,
        selectedSubCategoryId: Int? = null,
        selectedBrandId: Int? = null,
        searchQuery: String = "",
        selectedProduct: ProductUi? = null,
        quantity: String = "1",
        unitPrice: String = selectedProduct?.purchasePrice?.toString().orEmpty(),
        showResults: Boolean = true,
        searchError: String? = null,
        quantityError: String? = null,
        unitPriceError: String? = null
    ): AddProductDialogState {
        val categories = loadCategories()
        val subCategories = loadSubCategories(selectedCategoryId)
        val brands = loadBrands(selectedSubCategoryId)

        val products = when {
            searchQuery.isNotBlank() -> searchProducts(searchQuery)
            selectedBrandId != null -> loadProductsByBrand(selectedBrandId)
            else -> emptyList()
        }

        return AddProductDialogState(
            categories = categories,
            subCategories = subCategories,
            brands = brands,
            selectedCategoryId = selectedCategoryId,
            selectedSubCategoryId = selectedSubCategoryId,
            selectedBrandId = selectedBrandId,
            searchQuery = searchQuery,
            products = products,
            selectedProduct = selectedProduct,
            quantity = quantity,
            unitPrice = unitPrice,
            showResults = showResults,
            searchError = searchError,
            quantityError = quantityError,
            unitPriceError = unitPriceError
        )
    }

    private fun selectProvider(provider: ProviderUi) {
        _state.update { currentState ->
            val selectedOrder = currentState.selectedPurchaseOrder
            val shouldClearOrder = selectedOrder != null && selectedOrder.providerId != provider.id

            currentState.copy(
                provider = provider,
                providerError = null,
                selectedPurchaseOrder = if (shouldClearOrder) null else selectedOrder,
                feedbackMessage = if (shouldClearOrder) {
                    "La orden vinculada se desvinculo porque pertenece a otro proveedor."
                } else {
                    currentState.feedbackMessage
                }
            )
        }
    }

    private fun handlePurchaseOrderSelection(purchaseOrder: PurchaseOrderUi?) {
        if (purchaseOrder == null) {
            _state.update {
                it.copy(
                    selectedPurchaseOrder = null,
                    pendingPurchaseOrder = null,
                    showOrderConflictDialog = false
                )
            }
            return
        }

        val currentItems = _state.value.items
        if (currentItems.isNotEmpty()) {
            _state.update {
                it.copy(
                    pendingPurchaseOrder = purchaseOrder,
                    showOrderConflictDialog = true
                )
            }
            return
        }

        applyPurchaseOrder(purchaseOrder = purchaseOrder, merge = false)
    }

    private fun applyPendingPurchaseOrder(merge: Boolean) {
        val pendingOrder = _state.value.pendingPurchaseOrder ?: return
        applyPurchaseOrder(purchaseOrder = pendingOrder, merge = merge)
    }

    private fun dismissPurchaseOrderConflict() {
        _state.update {
            it.copy(
                pendingPurchaseOrder = null,
                showOrderConflictDialog = false
            )
        }
    }

    private fun applyPurchaseOrder(
        purchaseOrder: PurchaseOrderUi,
        merge: Boolean
    ) {
        val provider = _state.value.providers.firstOrNull { it.id == purchaseOrder.providerId }
            ?: ProviderUi(id = purchaseOrder.providerId, name = purchaseOrder.providerName)

        val orderItems = purchaseOrder.items.mapNotNull { orderItem ->
            val product = mockCatalog.firstOrNull { it.id == orderItem.productId } ?: return@mapNotNull null
            PurchaseItemUi(
                uiId = UUID.randomUUID().toString(),
                productId = product.id,
                productName = product.name,
                quantity = orderItem.quantity,
                unitPrice = orderItem.suggestedUnitPriceInCents ?: product.purchasePrice,
                total = (orderItem.quantity * (orderItem.suggestedUnitPriceInCents ?: product.purchasePrice)).toLong()
            )
        }

        _state.update { currentState ->
            val items = if (merge) {
                mergeItems(currentState.items, orderItems)
            } else {
                orderItems
            }

            currentState.withRecalculatedTotals(items = items, itemsError = null).copy(
                provider = provider,
                providerError = null,
                selectedPurchaseOrder = purchaseOrder,
                pendingPurchaseOrder = null,
                showOrderConflictDialog = false,
                feedbackMessage = if (merge) {
                    "Orden de compra fusionada con los items actuales."
                } else {
                    "Orden de compra cargada en el registro."
                }
            )
        }
    }

    private fun openAddProductDialog() {
        _state.update {
            it.copy(
                showAddProductDialog = true,
                addProductState = createAddProductDialogState()
            )
        }
    }

    private fun closeAddProductDialog() {
        _state.update {
            it.copy(
                showAddProductDialog = false,
                addProductState = createAddProductDialogState()
            )
        }
    }

    private fun removeItem(itemId: String) {
        _state.update { currentState ->
            val updatedItems = currentState.items.filterNot { it.uiId == itemId }
            currentState.withRecalculatedTotals(
                items = updatedItems,
                itemsError = if (updatedItems.isEmpty()) "Agrega al menos un producto." else null
            )
        }
    }

    private fun updateItemQuantity(itemId: String, rawQuantity: String) {
        val quantity = rawQuantity.toDoubleOrNull()
        if (quantity == null || quantity <= 0.0) {
            _state.update { it.copy(itemsError = "La cantidad debe ser mayor a cero.") }
            return
        }

        _state.update { currentState ->
            val updatedItems = currentState.items.map { item ->
                if (item.uiId == itemId) {
                    item.copy(quantity = quantity, total = (quantity * item.unitPrice).toLong())
                } else {
                    item
                }
            }

            currentState.withRecalculatedTotals(items = updatedItems, itemsError = null)
        }
    }

    private fun updateItemUnitPrice(itemId: String, rawPrice: String) {
        val unitPrice = rawPrice.toLongOrNull()
        if (unitPrice == null || unitPrice <= 0L) {
            _state.update { it.copy(itemsError = "El precio unitario debe ser mayor a cero.") }
            return
        }

        _state.update { currentState ->
            val updatedItems = currentState.items.map { item ->
                if (item.uiId == itemId) {
                    item.copy(unitPrice = unitPrice, total = (item.quantity * unitPrice).toLong())
                } else {
                    item
                }
            }

            currentState.withRecalculatedTotals(items = updatedItems, itemsError = null)
        }
    }

    private fun handleAddProductDialogEvent(event: AddProductDialogEvent) {
        when (event) {
            AddProductDialogEvent.Cancel -> closeAddProductDialog()
            AddProductDialogEvent.Confirm -> confirmAddProduct()
            AddProductDialogEvent.ScanBarcode -> {
                // TODO wire barcode scan trigger from UI when the scanner flow is available
            }

            is AddProductDialogEvent.SelectCategory -> {
                _state.update { currentState ->
                    currentState.copy(
                        addProductState = createAddProductDialogState(
                            selectedCategoryId = event.categoryId,
                            searchQuery = "",
                            showResults = true
                        )
                    )
                }
            }

            is AddProductDialogEvent.SelectSubCategory -> {
                val categoryId = _state.value.addProductState.selectedCategoryId
                _state.update { currentState ->
                    currentState.copy(
                        addProductState = createAddProductDialogState(
                            selectedCategoryId = categoryId,
                            selectedSubCategoryId = event.subCategoryId,
                            searchQuery = "",
                            showResults = true
                        )
                    )
                }
            }

            is AddProductDialogEvent.SelectBrand -> {
                val dialog = _state.value.addProductState
                _state.update { currentState ->
                    currentState.copy(
                        addProductState = createAddProductDialogState(
                            selectedCategoryId = dialog.selectedCategoryId,
                            selectedSubCategoryId = dialog.selectedSubCategoryId,
                            selectedBrandId = event.brandId,
                            searchQuery = "",
                            showResults = true
                        )
                    )
                }
            }

            is AddProductDialogEvent.SearchChanged -> {
                val dialog = _state.value.addProductState
                _state.update { currentState ->
                    currentState.copy(
                        addProductState = createAddProductDialogState(
                            selectedCategoryId = dialog.selectedCategoryId,
                            selectedSubCategoryId = dialog.selectedSubCategoryId,
                            selectedBrandId = dialog.selectedBrandId,
                            searchQuery = event.query,
                            selectedProduct = if (event.query.isBlank()) dialog.selectedProduct else null,
                            quantity = dialog.quantity,
                            unitPrice = dialog.unitPrice,
                            showResults = true
                        )
                    )
                }
            }

            is AddProductDialogEvent.SelectProduct -> {
                val dialog = _state.value.addProductState
                _state.update { currentState ->
                    currentState.copy(
                        addProductState = createAddProductDialogState(
                            selectedCategoryId = event.product.categoryId,
                            selectedSubCategoryId = event.product.subCategoryId,
                            selectedBrandId = event.product.brandId,
                            searchQuery = dialog.searchQuery,
                            selectedProduct = event.product,
                            quantity = "1",
                            unitPrice = event.product.purchasePrice.toString(),
                            showResults = false
                        )
                    )
                }
            }

            is AddProductDialogEvent.QuantityChanged -> {
                _state.update { currentState ->
                    currentState.copy(
                        addProductState = currentState.addProductState.copy(
                            quantity = event.value,
                            quantityError = null
                        )
                    )
                }
            }

            is AddProductDialogEvent.PriceChanged -> {
                _state.update { currentState ->
                    currentState.copy(
                        addProductState = currentState.addProductState.copy(
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

        val searchError = if (selectedProduct == null) "Selecciona un producto." else null
        val quantityError = when {
            dialogState.quantity.isBlank() -> "Ingresa una cantidad."
            quantity == null || quantity <= 0.0 -> "La cantidad debe ser mayor a cero."
            else -> null
        }
        val unitPriceError = when {
            dialogState.unitPrice.isBlank() -> "Ingresa un precio."
            unitPrice == null || unitPrice <= 0L -> "El precio debe ser mayor a cero."
            else -> null
        }

        if (searchError != null || quantityError != null || unitPriceError != null) {
            _state.update { currentState ->
                currentState.copy(
                    addProductState = currentState.addProductState.copy(
                        searchError = searchError,
                        quantityError = quantityError,
                        unitPriceError = unitPriceError
                    )
                )
            }
            return
        }

        val item = PurchaseItemUi(
            uiId = UUID.randomUUID().toString(),
            productId = selectedProduct!!.id,
            productName = selectedProduct.name,
            quantity = quantity!!,
            unitPrice = unitPrice!!,
            total = (quantity * unitPrice).toLong()
        )

        _state.update { currentState ->
            currentState.withRecalculatedTotals(
                items = mergeItems(currentState.items, listOf(item)),
                itemsError = null
            ).copy(
                showAddProductDialog = false,
                addProductState = createAddProductDialogState()
            )
        }
    }

    private fun confirmPurchase() {
        val currentState = _state.value
        val providerError = if (currentState.provider == null) "Selecciona un proveedor." else null
        val invoiceTypeError = if (currentState.invoiceType.isBlank()) "Selecciona el tipo de factura." else null
        val paymentMethodError = if (currentState.paymentMethod.isBlank()) "Selecciona el medio de pago." else null
        val itemsError = if (currentState.items.isEmpty()) "Agrega al menos un producto." else null

        if (providerError != null || invoiceTypeError != null || paymentMethodError != null || itemsError != null) {
            _state.update {
                it.copy(
                    providerError = providerError,
                    invoiceTypeError = invoiceTypeError,
                    paymentMethodError = paymentMethodError,
                    itemsError = itemsError,
                    feedbackMessage = "Faltan completar datos obligatorios."
                )
            }
            return
        }

        // TODO map PurchaseCreateState to the parameter object required by RegisterPurchaseUseCase
        // TODO invoke domain validation/result handling and propagate field errors back into this UI state
        // TODO if there is a linked purchase order, update its status to RECEIVED after a successful registration
        _state.update {
            it.copy(
                providerError = null,
                invoiceTypeError = null,
                paymentMethodError = null,
                itemsError = null,
                feedbackMessage = "Formulario listo para conectar con el caso de uso de registro."
            )
        }
    }

    private fun clearFeedback(message: String? = null) {
        _state.update { it.copy(feedbackMessage = message) }
    }

    private fun mergeItems(
        currentItems: List<PurchaseItemUi>,
        incomingItems: List<PurchaseItemUi>
    ): List<PurchaseItemUi> {
        var merged = currentItems

        incomingItems.forEach { newItem ->
            val existingItem = merged.firstOrNull {
                it.productId == newItem.productId && it.unitPrice == newItem.unitPrice
            }

            merged = if (existingItem == null) {
                merged + newItem
            } else {
                merged.map { item ->
                    if (item.uiId == existingItem.uiId) {
                        val newQuantity = item.quantity + newItem.quantity
                        item.copy(
                            quantity = newQuantity,
                            total = (newQuantity * item.unitPrice).toLong()
                        )
                    } else {
                        item
                    }
                }
            }
        }

        return merged
    }

    private fun PurchaseCreateState.withRecalculatedTotals(
        items: List<PurchaseItemUi>,
        itemsError: String? = this.itemsError
    ): PurchaseCreateState {
        val subtotal = items.sumOf { item -> item.total }
        val tax = 0L
        val total = subtotal + tax

        return copy(
            items = items,
            subtotalInCents = subtotal,
            taxInCents = tax,
            totalInCents = total,
            itemsError = itemsError
        )
    }

    // Datos simulados
    private fun mockProviders(): List<ProviderUi> = listOf(
        ProviderUi(id = 1, name = "Distribuidora Centro", cuit = "30-12345678-9", phone = "3415550101"),
        ProviderUi(id = 2, name = "Bebidas del Litoral", cuit = "30-87654321-0", phone = "3415550102"),
        ProviderUi(id = 3, name = "Mayorista San Martin", cuit = "30-11223344-5", phone = "3415550103")
    )

    private fun mockInvoiceTypes(): List<String> = listOf("A", "B", "C", "Ticket")

    private fun mockPaymentMethods(): List<String> = listOf(
        "Efectivo",
        "Transferencia",
        "Cuenta corriente",
        "Tarjeta"
    )

    private fun mockCatalog(): List<ProductUi> = listOf(
        ProductUi(1, "Yerba 1kg", "779000000001", 425000, 1, "Almacen", 1, "Infusiones", 1, "Playadito"),
        ProductUi(2, "Te en saquitos", "779000000002", 195000, 1, "Almacen", 1, "Infusiones", 2, "La Virginia"),
        ProductUi(3, "Azucar 1kg", "779000000003", 138500, 1, "Almacen", 2, "Endulzantes", 3, "Ledesma"),
        ProductUi(4, "Harina 000 1kg", "779000000004", 126000, 1, "Almacen", 3, "Harinas", 4, "Blancaflor"),
        ProductUi(5, "Arroz largo fino 1kg", "779000000005", 119900, 1, "Almacen", 4, "Arroces", 5, "Gallo"),
        ProductUi(6, "Gaseosa cola 2.25L", "779000000006", 310000, 2, "Bebidas", 5, "Gaseosas", 6, "Coca Cola"),
        ProductUi(7, "Gaseosa lima limon 2.25L", "779000000007", 285000, 2, "Bebidas", 5, "Gaseosas", 7, "Sprite"),
        ProductUi(8, "Agua mineral 1.5L", "779000000008", 145000, 2, "Bebidas", 6, "Aguas", 8, "Villa del Sur"),
        ProductUi(9, "Aceite 900ml", "779000000009", 289900, 1, "Almacen", 7, "Aceites", 9, "Cocinero"),
        ProductUi(10, "Pure de tomate 520g", "779000000010", 99000, 1, "Almacen", 8, "Conservas", 10, "Arcor")
    )

    private fun mockPurchaseOrders(): List<PurchaseOrderUi> = listOf(
        PurchaseOrderUi(
            id = 101,
            providerId = 1,
            providerName = "Distribuidora Centro",
            status = "PENDING",
            expectedDeliveryLabel = "20/03/2026",
            items = listOf(
                PurchaseOrderItemUi(productId = 1, productName = "Yerba 1kg", quantity = 8.0, suggestedUnitPriceInCents = 425000),
                PurchaseOrderItemUi(productId = 3, productName = "Azucar 1kg", quantity = 12.0, suggestedUnitPriceInCents = 138500)
            )
        ),
        PurchaseOrderUi(
            id = 102,
            providerId = 2,
            providerName = "Bebidas del Litoral",
            status = "SENT",
            expectedDeliveryLabel = "19/03/2026",
            items = listOf(
                PurchaseOrderItemUi(productId = 6, productName = "Gaseosa cola 2.25L", quantity = 10.0, suggestedUnitPriceInCents = 310000),
                PurchaseOrderItemUi(productId = 8, productName = "Agua mineral 1.5L", quantity = 12.0, suggestedUnitPriceInCents = 145000)
            )
        )
    )
}
