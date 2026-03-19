package com.litvy.litvysales.data.local.query.sales

import androidx.sqlite.db.SimpleSQLiteQuery
import com.litvy.litvysales.data.local.query.appendOrderBy
import com.litvy.litvysales.data.local.query.appendPagination
import com.litvy.litvysales.domain.filter.sales.SaleFilter

object SaleQueryBuilder {
    fun build(filter: SaleFilter): SimpleSQLiteQuery{
        val sql = StringBuilder()
        val args = mutableListOf<Any>()

        sql.append("SELECT * FROM sale WHERE 1 = 1")

        fun add(condition: String, vararg values: Any){
            sql.append(" AND $condition")
            args.addAll(values)
        }

        filter.cashSessionId?.let {
            add("cashSessionId = ?", it)
        }

        filter.sellerId?.let {
            add("sellerId = ?", it)
        }

        filter.customerId?.let {
            add("customerId = ?", it)
        }

        filter.status?.let {
            add("status = ?", it.name)
        }

        filter.createdAtFrom?.let {
            add("createdAt >= ?", it)
        }

        filter.createdAtTo?.let {
            add("createdAt <= ?", it)
        }

        val orderColumn = when (filter.sortBy) {
            com.litvy.litvysales.domain.filter.sales.SaleSortBy.CREATED_AT -> "createdAt"
            com.litvy.litvysales.domain.filter.sales.SaleSortBy.TOTAL_IN_CENTS -> "totalInCents"
            com.litvy.litvysales.domain.filter.sales.SaleSortBy.STATUS -> "status"
        }

        sql.appendOrderBy(orderColumn, filter.sortDirection)
        sql.appendPagination(args, filter.limit, filter.offset)

        return SimpleSQLiteQuery(sql.toString(), args.toTypedArray())
    }
}
