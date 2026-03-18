package com.litvy.litvysales.data.local.query.inventory

import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
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

        filter.expirationDate?.let {
            add("expirationDate = ?", it)
        }

        filter.createdAt?.let {
            add("createdAt = ?", it)
        }

        sql.append(" ORDER BY createdAt DESC")

        return SimpleSQLiteQuery(sql.toString(), args.toTypedArray())
    }
}