package com.litvy.litvysales.ui.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import com.litvy.litvysales.domain.filter.common.QuerySortDirection

@Composable
public fun SortIcon(
    active: Boolean,
    direction: QuerySortDirection
) {
    val rotation = if (direction == QuerySortDirection.ASC) 0f else 180f

    Icon(
        imageVector = Icons.Default.ArrowUpward,
        contentDescription = null,
        modifier = Modifier.rotate(if (active) rotation else 0f),
        tint = if (active) MaterialTheme.colorScheme.primary else Color.Gray
    )
}