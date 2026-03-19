package com.litvy.litvysales.data.local.query.inventory

import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.litvy.litvysales.data.local.query.appendOrderBy
import com.litvy.litvysales.data.local.query.appendPagination
import com.litvy.litvysales.domain.filter.inventory.StockMovementFilter

object StockMovementQueryBuilder {

    fun build(filter: StockMovementFilter): SupportSQLiteQuery{

        val sql = StringBuilder()
        val args = mutableListOf<Any>()

        sql.append("SELECT * FROM stockMovement WHERE 1 = 1")

        fun add(condition: String, vararg values: Any){
            sql.append(" AND $condition")
            args.addAll(values)
        }

        filter.productId?.let {
            add("productId = ?", it)
        }

        filter.batchId?.let {
            add("batchId = ?", it)
        }

        filter.type?.let {
            add("type = ?", it.name)
        }

        filter.createdAtFrom?.let {
            add("createdAt >= ?", it)
        }

        filter.createdAtTo?.let {
            add("createdAt <= ?", it)
        }

        filter.referenceId?.let {
            add("referenceId = ?", it)
        }

        filter.referenceType?.let {
            add("referenceType = ?", it)
        }

        filter.createdBy?.let {
            add("createdBy = ?", it)
        }

        val orderColumn = when (filter.sortBy) {
            com.litvy.litvysales.domain.filter.inventory.StockMovementSortBy.CREATED_AT -> "createdAt"
            com.litvy.litvysales.domain.filter.inventory.StockMovementSortBy.PRODUCT_ID -> "productId"
            com.litvy.litvysales.domain.filter.inventory.StockMovementSortBy.TYPE -> "type"
        }

        sql.appendOrderBy(orderColumn, filter.sortDirection)
        sql.appendPagination(args, filter.limit, filter.offset)

        return SimpleSQLiteQuery(sql.toString(), args.toTypedArray())
    }
}
