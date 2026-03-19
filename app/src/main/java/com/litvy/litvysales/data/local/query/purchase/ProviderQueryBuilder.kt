package com.litvy.litvysales.data.local.query.purchase

import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.litvy.litvysales.data.local.query.appendOrderBy
import com.litvy.litvysales.data.local.query.appendPagination
import com.litvy.litvysales.domain.filter.purchases.ProviderFilter

object ProviderQueryBuilder {

    fun build(filter: ProviderFilter): SupportSQLiteQuery{
        val sql = StringBuilder()
        val args = mutableListOf<Any>()

        sql.append("SELECT * FROM provider WHERE 1 = 1")

        fun add(condition: String, vararg values: Any){
            sql.append(" AND $condition")
            args.addAll(values)
        }

        filter.name?.takeIf { it.isNotBlank() }?.let {
            add("LOWER(name) LIKE LOWER(?)", "%$it%")
        }

        filter.cuit?.takeIf { it.isNotBlank() }?.let {
            add("cuit LIKE ?", "%$it%")
        }

        filter.telephoneNumber?.takeIf { it.isNotBlank() }?.let {
            add("telephoneNumber LIKE ?", "%$it%")
        }

        filter.address?.takeIf { it.isNotBlank() }?.let {
            add("LOWER(address) LIKE LOWER(?)", "%$it%")
        }

        filter.email?.takeIf { it.isNotBlank() }?.let {
            add("LOWER(email) LIKE LOWER(?)", "%$it%")
        }

        val orderColumn = when (filter.sortBy) {
            com.litvy.litvysales.domain.filter.purchases.ProviderSortBy.NAME -> "name"
            com.litvy.litvysales.domain.filter.purchases.ProviderSortBy.CUIT -> "cuit"
            com.litvy.litvysales.domain.filter.purchases.ProviderSortBy.EMAIL -> "email"
        }

        sql.appendOrderBy(orderColumn, filter.sortDirection)
        sql.appendPagination(args, filter.limit, filter.offset)

        return SimpleSQLiteQuery(sql.toString(), args.toTypedArray())
    }
}
