package com.litvy.litvysales.ui.catalog.dialogs

import androidx.compose.runtime.Composable
import com.litvy.litvysales.ui.catalog.util.CatalogState

@Composable
fun CatalogDialogs(
    state: CatalogState,
    showCreateDialog: Boolean,
    mode: CatalogFormMode,

    name: String,
    purchase: String,
    sale: String,
    hasExpiration: Boolean,
    isWeighable: Boolean,

    onNameChange: (String) -> Unit,
    onPurchaseChange: (String) -> Unit,
    onSaleChange: (String) -> Unit,
    onExpirationChange: (Boolean) -> Unit,
    onWeighableChange: (Boolean) -> Unit,

    onConfirmCreate: () -> Unit,
    onDismissCreate: () -> Unit
) {

    if (showCreateDialog) {

        CatalogCreateDialog(

            mode = mode,

            level = state.level,

            name = name,
            purchase = purchase,
            sale = sale,
            hasExpiration = hasExpiration,
            isWeighable = isWeighable,

            onNameChange = onNameChange,
            onPurchaseChange = onPurchaseChange,
            onSaleChange = onSaleChange,
            onExpirationChange = onExpirationChange,
            onWeighableChange = onWeighableChange,

            onConfirm = onConfirmCreate,
            onDismiss = onDismissCreate
        )

    }

}