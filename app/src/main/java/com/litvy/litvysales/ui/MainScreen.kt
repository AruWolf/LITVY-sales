package com.litvy.litvysales.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val navController = rememberNavController()

    ModalNavigationDrawer(

        drawerState = drawerState,

        drawerContent = {

            ModalDrawerSheet {

                Text(
                    text = "Litvy Sales",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(16.dp)
                )

                NavigationDrawerItem(
                    label = { Text("Ventas") },
                    selected = false,
                    onClick = {
                        navController.navigate("sales")
                        scope.launch {
                            drawerState.close()
                        }
                    },
                    icon = {
                        Icon(Icons.Default.PointOfSale, null)
                    }
                )

                NavigationDrawerItem(
                    label = { Text("Catálogo") },
                    selected = false,
                    onClick = {
                        navController.navigate("catalog")
                        scope.launch {
                            drawerState.close()
                        }
                    },
                    icon = {
                        Icon(Icons.Default.Inventory, null)
                    }
                )

                NavigationDrawerItem(
                    label = { Text("Configuración") },
                    selected = false,
                    onClick = { },
                    icon = {
                        Icon(Icons.Default.Settings, null)
                    }
                )

            }

        }

    ) {

        Scaffold(

            topBar = {

                TopAppBar(

                    title = { Text("Litvy Sales") },

                    navigationIcon = {

                        IconButton(
                            onClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            }
                        ) {

                            Icon(Icons.Default.Menu, null)

                        }

                    }

                )

            }

        ) { padding ->

            NavigationHost(
                navController = navController,
                modifier = Modifier.padding(padding)
            )

        }

    }

}