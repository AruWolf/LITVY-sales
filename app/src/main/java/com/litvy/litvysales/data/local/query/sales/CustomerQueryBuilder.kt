package com.litvy.litvysales.data.local.query.sales

import androidx.sqlite.db.SimpleSQLiteQuery
import com.litvy.litvysales.data.local.query.appendOrderBy
import com.litvy.litvysales.data.local.query.appendPagination
import com.litvy.litvysales.domain.filter.sales.CustomerFilter

object CustomerQueryBuilder {
    fun build(filter: CustomerFilter): SimpleSQLiteQuery{
        val sql = StringBuilder()
        val args = mutableListOf<Any>()

        sql.append("SELECT * FROM customer WHERE 1 = 1")

        fun add(condition: String, vararg values: Any){
            sql.append(" AND $condition")
            args.addAll(values)
        }

        filter.name?.takeIf { it.isNotBlank() }?.let {
            add("LOWER(name) LIKE LOWER(?)", "%$it%")
        }

        filter.lastname?.takeIf { it.isNotBlank() }?.let {
            add("LOWER(lastname) LIKE LOWER(?)", "%$it%")
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

        filter.createdAtFrom?.let {
            add("createdAt >= ?", it)
        }

        filter.createdAtTo?.let {
            add("createdAt <= ?", it)
        }

        val orderColumn = when (filter.sortBy) {
            com.litvy.litvysales.domain.filter.sales.CustomerSortBy.CREATED_AT -> "createdAt"
            com.litvy.litvysales.domain.filter.sales.CustomerSortBy.NAME -> "name"
            com.litvy.litvysales.domain.filter.sales.CustomerSortBy.LASTNAME -> "lastname"
        }

        sql.appendOrderBy(orderColumn, filter.sortDirection)
        sql.appendPagination(args, filter.limit, filter.offset)

        return SimpleSQLiteQuery(sql.toString(), args.toTypedArray())
    }
}
