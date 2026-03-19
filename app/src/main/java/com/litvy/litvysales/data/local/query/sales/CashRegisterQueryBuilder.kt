package com.litvy.litvysales.data.local.query.sales

import androidx.sqlite.db.SimpleSQLiteQuery
import com.litvy.litvysales.data.local.query.appendOrderBy
import com.litvy.litvysales.domain.filter.sales.CashRegisterFilter

object CashRegisterQueryBuilder {
    fun build(filter: CashRegisterFilter): SimpleSQLiteQuery{
        val sql = StringBuilder()
        val args = mutableListOf<Any>()

        sql.append("SELECT * FROM cashRegister WHERE 1 = 1")

        fun add(condition: String, vararg values: Any){
            sql.append(" AND $condition")
            args.addAll(values)
        }

        filter.name?.takeIf { it.isNotBlank() }?.let {
            add("LOWER(name) LIKE LOWER(?)", "%$it%")
        }

        filter.location?.takeIf { it.isNotBlank() }?.let {
            add("LOWER(location) LIKE LOWER(?)", "%$it%")
        }

        filter.active?.let {
            add("active = ?", it)
        }

        val orderColumn = when (filter.sortBy) {
            com.litvy.litvysales.domain.filter.sales.CashRegisterSortBy.NAME -> "name"
            com.litvy.litvysales.domain.filter.sales.CashRegisterSortBy.LOCATION -> "location"
            com.litvy.litvysales.domain.filter.sales.CashRegisterSortBy.ACTIVE -> "active"
        }

        sql.appendOrderBy(orderColumn, filter.sortDirection)

        return SimpleSQLiteQuery(sql.toString(), args.toTypedArray())
    }
}
