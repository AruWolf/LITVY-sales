package com.litvy.litvysales.data.local.query.purchase

import androidx.sqlite.db.SimpleSQLiteQuery
import com.litvy.litvysales.data.local.query.appendOrderBy
import com.litvy.litvysales.data.local.query.appendPagination
import com.litvy.litvysales.domain.filter.purchases.PurchaseFilter

object PurchaseQueryBuilder {

    fun build(filter: PurchaseFilter): SimpleSQLiteQuery{

        val sql = StringBuilder()
        val args = mutableListOf<Any>()

        sql.append("SELECT * FROM purchase WHERE 1 = 1")

        fun add(condition: String, vararg values: Any){
            sql.append(" AND $condition")
            args.addAll(values)
        }

        filter.providerId?.let {
            add("providerId = ?", it)
        }

        filter.invoiceTypeId?.let {
            add("invoiceTypeId = ?", it)
        }

        filter.paymentMethodId?.let {
            add("paymentMethodId = ?", it)
        }

        filter.createdAtFrom?.let {
            add("createdAt >= ?", it)
        }

        filter.createdAtTo?.let {
            add("createdAt <= ?", it)
        }

        filter.createdBy?.let {
            add("createdBy = ?", it)
        }

        val orderColumn = when (filter.sortBy) {
            com.litvy.litvysales.domain.filter.purchases.PurchaseSortBy.CREATED_AT -> "createdAt"
            com.litvy.litvysales.domain.filter.purchases.PurchaseSortBy.TOTAL_IN_CENTS -> "totalInCents"
            com.litvy.litvysales.domain.filter.purchases.PurchaseSortBy.PROVIDER_ID -> "providerId"
        }

        sql.appendOrderBy(orderColumn, filter.sortDirection)
        sql.appendPagination(args, filter.limit, filter.offset)

        return SimpleSQLiteQuery(sql.toString(), args.toTypedArray())
    }
}
