package com.litvy.litvysales.data.local.query

import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.litvy.litvysales.domain.filter.CashSessionFilter

object CashSessionQueryBuilder {

    fun build(filter: CashSessionFilter): SupportSQLiteQuery {

        val sql = StringBuilder()
        val args = mutableListOf<Any>()

        sql.append("SELECT * FROM cashSession WHERE 1=1")

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

        sql.append(" ORDER BY startedAt DESC")

        return SimpleSQLiteQuery(sql.toString(), args.toTypedArray())
    }
}