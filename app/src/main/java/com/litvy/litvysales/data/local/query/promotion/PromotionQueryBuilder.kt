package com.litvy.litvysales.data.local.query.promotion

import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.litvy.litvysales.data.local.query.appendOrderBy
import com.litvy.litvysales.data.local.query.appendPagination
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

        filter.name?.takeIf { it.isNotBlank() }?.let {
            add("LOWER(name) LIKE LOWER(?)", "%$it%")
        }

        filter.priority?.let {
            add("priority = ?", it)
        }

        filter.stackable?.let {
            add("stackable = ?", it)
        }

        filter.active?.let {
            add("active = ?", it)
        }

        filter.clearStock?.let {
            add("clearStock = ?", it)
        }

        filter.startDateFrom?.let {
            add("startDate >= ?", it)
        }

        filter.startDateTo?.let {
            add("startDate <= ?", it)
        }

        filter.endDateFrom?.let {
            add("endDate >= ?", it)
        }

        filter.endDateTo?.let {
            add("endDate <= ?", it)
        }

        filter.createdAtFrom?.let {
            add("createdAt >= ?", it)
        }

        filter.createdAtTo?.let {
            add("createdAt <= ?", it)
        }

        val orderColumn = when (filter.sortBy) {
            com.litvy.litvysales.domain.filter.promotion.PromotionSortBy.CREATED_AT -> "createdAt"
            com.litvy.litvysales.domain.filter.promotion.PromotionSortBy.NAME -> "name"
            com.litvy.litvysales.domain.filter.promotion.PromotionSortBy.PRIORITY -> "priority"
            com.litvy.litvysales.domain.filter.promotion.PromotionSortBy.START_DATE -> "startDate"
            com.litvy.litvysales.domain.filter.promotion.PromotionSortBy.END_DATE -> "endDate"
        }

        sql.appendOrderBy(orderColumn, filter.sortDirection)
        sql.appendPagination(args, filter.limit, filter.offset)

        return SimpleSQLiteQuery(sql.toString(), args.toTypedArray())
    }
}
