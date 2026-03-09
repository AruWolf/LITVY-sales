package com.litvy.litvysales.ui.catalog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CatalogListScreen(
    title: String,
    items: List<Pair<Int, String>>,
    emptyMessage: String,
    onItemClick: (Int) -> Unit,
    onCreate: (String) -> Unit,
    onBack: (() -> Unit)? = null
) {

    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        if (onBack != null) {
            Button(onClick = onBack) {
                Text("← Volver")
            }
        }

        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { showDialog = true }) {
            Text("Crear")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (items.isEmpty()) {

            Text(emptyMessage)

        } else {

            LazyColumn {

                items(items) { item ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        onClick = { onItemClick(item.first) }
                    ) {

                        Text(
                            text = item.second,
                            modifier = Modifier.padding(16.dp)
                        )

                    }

                }

            }

        }

    }

    if (showDialog) {

        var name by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showDialog = false },

            confirmButton = {

                Button(
                    onClick = {
                        onCreate(name)
                        showDialog = false
                    }
                ) {
                    Text("Crear")
                }

            },

            text = {

                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre") }
                )

            }
        )
    }

}