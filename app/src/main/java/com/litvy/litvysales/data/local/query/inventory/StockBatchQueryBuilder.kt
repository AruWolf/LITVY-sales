package com.litvy.litvysales.data.local.query.inventory

import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.litvy.litvysales.data.local.query.appendOrderBy
import com.litvy.litvysales.data.local.query.appendPagination
import com.litvy.litvysales.domain.filter.inventory.StockBatchFilter

object StockBatchQueryBuilder {

    fun build(filter: StockBatchFilter): SupportSQLiteQuery{

        val sql = StringBuilder()
        val args = mutableListOf<Any>()

        sql.append("SELECT * FROM stockBatch WHERE 1 = 1")

        fun add(condition: String, vararg values: Any){
            sql.append(" AND $condition")
            args.addAll(values)
        }

        filter.productId?.let {
            add("productId = ?", it)
        }

        filter.purchaseItemId?.let {
            add("purchaseItemId = ?", it)
        }

        filter.expirationDateFrom?.let {
            add("expirationDate >= ?", it)
        }

        filter.expirationDateTo?.let {
            add("expirationDate <= ?", it)
        }

        filter.createdAtFrom?.let {
            add("createdAt >= ?", it)
        }

        filter.createdAtTo?.let {
            add("createdAt <= ?", it)
        }

        val orderColumn = when (filter.sortBy) {
            com.litvy.litvysales.domain.filter.inventory.StockBatchSortBy.CREATED_AT -> "createdAt"
            com.litvy.litvysales.domain.filter.inventory.StockBatchSortBy.EXPIRATION_DATE -> "expirationDate"
            com.litvy.litvysales.domain.filter.inventory.StockBatchSortBy.PRODUCT_ID -> "productId"
        }

        sql.appendOrderBy(orderColumn, filter.sortDirection)
        sql.appendPagination(args, filter.limit, filter.offset)

        return SimpleSQLiteQuery(sql.toString(), args.toTypedArray())
    }
}
