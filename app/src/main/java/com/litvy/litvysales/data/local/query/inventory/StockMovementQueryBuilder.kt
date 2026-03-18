package com.litvy.litvysales.data.local.query.inventory

import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
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
            add("type = ?", it)
        }

        filter.createdAt?.let {
            add("createdAt = ?", it)
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

        sql.append(" ORDER BY createdAt DESC")

        return SimpleSQLiteQuery(sql.toString(), args.toTypedArray())
    }
}