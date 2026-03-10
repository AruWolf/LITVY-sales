package com.litvy.litvysales.ui.catalog.header

import com.litvy.litvysales.ui.catalog.util.CatalogState
import com.litvy.litvysales.ui.components.BreadcrumbItem

fun buildCatalogBreadcrumb(
    state: CatalogState,
    onNavigateCategories: () -> Unit,
    onNavigateSubCategories: () -> Unit,
    onNavigateBrands: () -> Unit
): List<BreadcrumbItem> {

    return buildList {

        state.selectedCategoryName?.let {
            add(
                BreadcrumbItem(
                    label = it,
                    onClick = onNavigateCategories
                )
            )
        }

        state.selectedSubCategoryName?.let {
            add(
                BreadcrumbItem(
                    label = it,
                    onClick = onNavigateSubCategories
                )
            )
        }

        state.selectedBrandName?.let {
            add(
                BreadcrumbItem(
                    label = it,
                    onClick = onNavigateBrands
                )
            )
        }
    }
}