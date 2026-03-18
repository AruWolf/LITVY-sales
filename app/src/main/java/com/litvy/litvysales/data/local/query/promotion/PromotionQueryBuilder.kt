package com.litvy.litvysales.data.local.query.promotion

import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.litvy.litvysales.domain.filter.promotion.PromotionFilter

object PromotionQueryBuilder {

    fun build(filter: PromotionFilter): SupportSQLiteQuery{

        val sql = StringBuilder()
        val args = mutableListOf<Any>()

        sql.append("SELECT * FROM promotion WHERE 1 = 1")

        fun add(condition: String, vararg values: Any){
            sql.append(" AND $condition")
            args.addAll(values)
        }

        filter.name?.takeIf { it.isNotBlank() }.let {
            add("LOWER(name) LIKE LOWER(?)", "%$it%")
        }

        filter.priority?.let {
            add("priority = ?", it)
        }

        filter.stackable?.let {
            add("stackable = ?", it)
        }

        filter.active?.let {
            add("condition = ?", it)
        }

        filter.clearStock?.let {
            add("clearStock = ?", it)
        }

        filter.startDate?.let {
            add("startDate = ?", it)
        }

        filter.endDate?.let {
            add("endDate = ?", it)
        }

        filter.createdAt?.let {
            add("createdAt = ?", it)
        }

        sql.append(" ORDER BY createdAt DESC")

        return SimpleSQLiteQuery(sql.toString(), args.toTypedArray())
    }
}