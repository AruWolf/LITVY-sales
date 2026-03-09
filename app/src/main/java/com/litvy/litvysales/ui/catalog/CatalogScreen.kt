package com.litvy.litvysales.ui.catalog

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.litvy.litvysales.LitvySalesApplication

@Composable
fun CatalogScreen() {

    val application = LocalContext.current.applicationContext
            as LitvySalesApplication

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
            container.createProductUseCase
        )
    }

    val viewModel: CatalogViewModel = viewModel(factory = factory)

    val state by viewModel.state.observeAsState(CatalogState())

    when(state.level){

        CatalogLevel.CATEGORIES -> {

            CatalogListScreen(
                title = "Categorías",
                items = state.categories.map { it.id!! to it.name },
                emptyMessage = "No hay categorías registradas",

                onItemClick = {
                    viewModel.onEvent(
                        CatalogEvent.SelectCategory(it)
                    )
                },

                onCreate = {
                    viewModel.onEvent(
                        CatalogEvent.CreateCategory(it)
                    )
                }
            )

        }

        CatalogLevel.SUBCATEGORIES -> {

            CatalogListScreen(
                title = "Subcategorías",

                items = state.subCategories.map { it.id!! to it.name },

                emptyMessage = "No hay subcategorías",

                onItemClick = {
                    viewModel.onEvent(
                        CatalogEvent.SelectSubCategory(it))
                },

                onCreate = {
                    viewModel.onEvent(
                        CatalogEvent.CreateSubCategory(
                            it,
                            state.selectedCategoryId!!
                        )
                    )
                },

                onBack = {
                    viewModel.onEvent(
                        CatalogEvent.NavigateBack
                    )
                }
            )

        }

        CatalogLevel.BRANDS -> {

            CatalogListScreen(
                title = "Marcas",

                items = state.brands.map { it.id!! to it.name },

                emptyMessage = "No hay marcas",

                onItemClick = {
                    viewModel.onEvent(
                        CatalogEvent.SelectBrand(it)
                    )
                },

                onCreate = {
                    viewModel.onEvent(
                        CatalogEvent.CreateBrand(
                            it,
                            state.selectedSubCategoryId!!
                        )
                    )
                },

                onBack = {
                    viewModel.onEvent(
                        CatalogEvent.NavigateBack
                    )
                }
            )

        }

        CatalogLevel.PRODUCTS -> {

            Column {

                Button(
                    onClick = {
                        viewModel.onEvent(
                            CatalogEvent.NavigateBack
                        )
                    }
                ){
                    Text("← Volver")
                }

                ProductTable(state = state)

            }

        }

    }
}