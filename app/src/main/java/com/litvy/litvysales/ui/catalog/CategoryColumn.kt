package com.litvy.litvysales.ui.catalog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
fun CategoryColumn(
    state: CatalogState,
    onClick: (Int) -> Unit,
    onCreateCategory: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    var showDialog by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxHeight()) {

        Button(
            onClick = { showDialog = true }
        ) {
            Text("+ Categoria")
        }

        LazyColumn(
            modifier = Modifier.fillMaxHeight()
        ) {

            items(state.categories) { category ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    onClick = { onClick(category.id!!) }
                ) {

                    Text(
                        category.name,
                        modifier = Modifier.padding(16.dp)
                    )

                }

            }

        }

    }

    if(showDialog){

        var name by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {

                Button(
                    onClick = {
                        onCreateCategory(name)
                        showDialog = false
                    }
                ){
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