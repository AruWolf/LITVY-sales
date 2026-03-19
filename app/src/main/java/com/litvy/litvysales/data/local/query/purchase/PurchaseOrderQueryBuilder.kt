package com.litvy.litvysales.data.local.query.purchase

import androidx.sqlite.db.SimpleSQLiteQuery
import com.litvy.litvysales.data.local.query.appendOrderBy
import com.litvy.litvysales.data.local.query.appendPagination
import com.litvy.litvysales.domain.filter.purchases.PurchaseOrderFilter

object PurchaseOrderQueryBuilder {
    fun build(filter: PurchaseOrderFilter): SimpleSQLiteQuery{

        val sql = StringBuilder()
        val args = mutableListOf<Any>()

        sql.append("SELECT * FROM purchaseOrder WHERE 1 = 1")

        fun add(condition: String, vararg values: Any){
            sql.append(" AND $condition")
            args.addAll(values)
        }

        filter.providerId?.let {
            add("providerId = ?", it)
        }

        filter.status?.let {
            add("status = ?", it.name)
        }

        filter.expectedDeliveryDateFrom?.let {
            add("expectedDeliveryDate >= ?", it)
        }

        filter.expectedDeliveryDateTo?.let {
            add("expectedDeliveryDate <= ?", it)
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
            com.litvy.litvysales.domain.filter.purchases.PurchaseOrderSortBy.CREATED_AT -> "createdAt"
            com.litvy.litvysales.domain.filter.purchases.PurchaseOrderSortBy.EXPECTED_DELIVERY_DATE -> "expectedDeliveryDate"
            com.litvy.litvysales.domain.filter.purchases.PurchaseOrderSortBy.STATUS -> "status"
        }

        sql.appendOrderBy(orderColumn, filter.sortDirection)
        sql.appendPagination(args, filter.limit, filter.offset)

        return SimpleSQLiteQuery(sql.toString(), args.toTypedArray())
    }
}
