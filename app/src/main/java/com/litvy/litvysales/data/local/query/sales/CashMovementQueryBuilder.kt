package com.litvy.litvysales.data.local.query.sales

import androidx.sqlite.db.SimpleSQLiteQuery
import com.litvy.litvysales.data.local.query.appendOrderBy
import com.litvy.litvysales.data.local.query.appendPagination
import com.litvy.litvysales.domain.filter.sales.CashMovementFilter

object CashMovementQueryBuilder {
    fun build(filter: CashMovementFilter): SimpleSQLiteQuery{
        val sql = StringBuilder()
        val args = mutableListOf<Any>()

        sql.append("SELECT * FROM cashMovement WHERE 1 = 1")

        fun add(condition: String, vararg values: Any){
            sql.append(" AND $condition")
            args.addAll(values)
        }

        filter.cashSessionId?.let {
            add("cashSessionId = ?", it)
        }

        filter.type?.let {
            add("type = ?", it)
        }

        filter.createdAtFrom?.let{
            add("createdAt >= ?", it)
        }

        filter.createdAtTo?.let{
            add("createdAt <= ?", it)
        }

        filter.createdBy?.let {
            add("createdBy = ?", it)
        }

        val orderColumn = when (filter.sortBy) {
            com.litvy.litvysales.domain.filter.sales.CashMovementSortBy.CREATED_AT -> "createdAt"
            com.litvy.litvysales.domain.filter.sales.CashMovementSortBy.AMOUNT_IN_CENTS -> "amountInCents"
            com.litvy.litvysales.domain.filter.sales.CashMovementSortBy.TYPE -> "type"
        }

        sql.appendOrderBy(orderColumn, filter.sortDirection)
        sql.appendPagination(args, filter.limit, filter.offset)

        return SimpleSQLiteQuery(sql.toString(), args.toTypedArray())
    }
}
