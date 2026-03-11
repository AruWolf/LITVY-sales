package com.litvy.litvysales.ui.catalog

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.litvy.litvysales.LitvySalesApplication
import com.litvy.litvysales.ui.catalog.body.CatalogBody
import com.litvy.litvysales.ui.catalog.builder.buildCatalogSaveEvent
import com.litvy.litvysales.ui.catalog.dialogs.CatalogDialogs
import com.litvy.litvysales.ui.catalog.builder.buildEditFormState
import com.litvy.litvysales.ui.catalog.dialogs.CatalogFormMode
import com.litvy.litvysales.ui.catalog.header.CatalogHeader
import com.litvy.litvysales.ui.catalog.util.CatalogLevel
import com.litvy.litvysales.ui.catalog.header.buildCatalogBreadcrumb
import com.litvy.litvysales.ui.catalog.model.ProductFormState
import com.litvy.litvysales.ui.catalog.util.CatalogEvent
import com.litvy.litvysales.ui.catalog.util.getSelectEvent
import com.litvy.litvysales.ui.catalog.util.getTitle

@Composable
fun CatalogScreen() {

    val application =
        LocalContext.current.applicationContext as LitvySalesApplication

    val container = application.container

    val factory = remember {
        CatalogViewModelFactory(
            container.getCategoriesUseCase,
            container.getSubCategoriesByCategoryUseCase,
            container.getBrandBySubCategoryUseCase,
            container.getProductByBrandUseCase,

            container.createCategoryUseCase,
            container.createSubCategoryUseCase,
            container.createBrandUseCase,
            container.createProductUseCase,

            container.updateCategoryUseCase,
            container.updateSubCategoryUseCase,
            container.updateBrandUseCase,
            container.updateProductUseCase
        )
    }

    val viewModel: CatalogViewModel = viewModel(factory = factory)

    val state by viewModel.state.collectAsStateWithLifecycle()


    // ---------------- UI STATE ----------------

    var formMode by remember { mutableStateOf(CatalogFormMode.CREATE) }

    var showCreateDialog by remember { mutableStateOf(false) }

    var editingId by remember { mutableStateOf<Int?>(null) }

    var nameInput by remember { mutableStateOf("") }
    var purchaseInput by remember { mutableStateOf("") }
    var saleInput by remember { mutableStateOf("") }

    var hasExpiration by remember { mutableStateOf(false) }
    var isWeighable by remember { mutableStateOf(false) }

    LaunchedEffect(state.operationSuccess) {

        if(state.operationSuccess){

            showCreateDialog = false
            editingId = null

            viewModel.resetOperationSuccess()

        }

    }

    LaunchedEffect(showCreateDialog) {

        if (showCreateDialog) {

            viewModel.clearFormValidation()

        }

    }


    // ---------------- BREADCRUMB ----------------

    val breadcrumbItems = buildCatalogBreadcrumb(

        state = state,

        onNavigateCategories = {
            viewModel.onEvent(CatalogEvent.NavigateToCategories)
        },

        onNavigateSubCategories = {
            viewModel.onEvent(CatalogEvent.NavigateToSubCategories)
        },

        onNavigateBrands = {
            viewModel.onEvent(CatalogEvent.NavigateToBrands)
        }

    )

    // ---------------- LAYOUT ----------------

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {

        CatalogHeader(

            breadcrumb = breadcrumbItems,

            title = getTitle(state.level),

            canGoBack = state.level != CatalogLevel.CATEGORIES,

            onBack = {
                viewModel.onEvent(CatalogEvent.NavigateBack)
            },

            onCreate = {

                editingId = null
                formMode = CatalogFormMode.CREATE

                nameInput = ""
                purchaseInput = ""
                saleInput = ""
                hasExpiration = false
                isWeighable = false

                showCreateDialog = true
            },

            onSearch = {
                // futura búsqueda
            }

        )

        Spacer(Modifier.height(8.dp))

        CatalogBody(

            state = state,

            onItemClick = { id ->
                viewModel.onEvent(
                    getSelectEvent(state.level, id)
                )
            },

            onEdit = { id ->

                val form = buildEditFormState(id, state)

                nameInput = form.name
                purchaseInput = form.purchase
                saleInput = form.sale
                hasExpiration = form.hasExpiration
                isWeighable = form.isWeighable

                editingId = id
                formMode = CatalogFormMode.EDIT
                showCreateDialog = true
            },

            onInspect = { }

        )

    }

    // ---------------- DIALOGS ----------------

    CatalogDialogs(

        state = state,

        showCreateDialog = showCreateDialog,

        mode = formMode,

        name = nameInput,
        purchase = purchaseInput,
        sale = saleInput,

        hasExpiration = hasExpiration,
        isWeighable = isWeighable,

        onNameChange = { nameInput = it },
        onPurchaseChange = { purchaseInput = it },
        onSaleChange = { saleInput = it },

        onExpirationChange = { hasExpiration = it },
        onWeighableChange = { isWeighable = it },

        onConfirmCreate = {

            viewModel.onEvent(

                buildCatalogSaveEvent(

                    level = state.level,
                    editingId = editingId,

                    name = nameInput,
                    purchase = purchaseInput,
                    sale = saleInput,

                    hasExpiration = hasExpiration,
                    isWeighable = isWeighable,

                    selectedCategoryId = state.selectedCategoryId,
                    selectedSubCategoryId = state.selectedSubCategoryId,
                    selectedBrandId = state.selectedBrandId

                )

            )
        },

        onDismissCreate = {
            showCreateDialog = false
            editingId = null
            formMode = CatalogFormMode.CREATE
        }



    )


}