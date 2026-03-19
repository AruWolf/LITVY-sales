package com.litvy.litvysales.data.local.query.sales

import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.litvy.litvysales.data.local.query.appendOrderBy
import com.litvy.litvysales.data.local.query.appendPagination
import com.litvy.litvysales.domain.filter.sales.CashSessionFilter

object CashSessionQueryBuilder {

    fun build(filter: CashSessionFilter): SupportSQLiteQuery {

        val sql = StringBuilder()
        val args = mutableListOf<Any>()

        sql.append("SELECT * FROM cashSession WHERE 1 = 1")

        fun add(condition: String, vararg values: Any) {
            sql.append(" AND $condition")
            args.addAll(values)
        }

        filter.registerId?.let {
            add("cashRegisterId = ?", it)
        }

        filter.status?.let {
            add("status = ?", it.name)
        }

        filter.openedBy?.let {
            add("openedBy = ?", it)
        }

        filter.closedBy?.let {
            add("closedBy = ?", it)
        }

        filter.startDate?.let {
            add("startedAt >= ?", it)
        }

        filter.endDate?.let {
            add("startedAt <= ?", it)
        }

        filter.isOpen?.let {
            if (it) {
                add("closedAt IS NULL")
            } else {
                add("closedAt IS NOT NULL")
            }
        }

        val orderColumn = when (filter.sortBy) {
            com.litvy.litvysales.domain.filter.sales.CashSessionSortBy.STARTED_AT -> "startedAt"
            com.litvy.litvysales.domain.filter.sales.CashSessionSortBy.CLOSED_AT -> "closedAt"
            com.litvy.litvysales.domain.filter.sales.CashSessionSortBy.STATUS -> "status"
        }

        sql.appendOrderBy(orderColumn, filter.sortDirection)
        sql.appendPagination(args, filter.limit, filter.offset)

        return SimpleSQLiteQuery(sql.toString(), args.toTypedArray())
    }
}
