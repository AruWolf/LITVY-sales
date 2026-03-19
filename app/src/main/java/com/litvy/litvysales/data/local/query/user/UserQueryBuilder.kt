package com.litvy.litvysales.data.local.query.user

import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.litvy.litvysales.data.local.query.appendOrderBy
import com.litvy.litvysales.domain.filter.user.UserFilter

object UserQueryBuilder {

    fun build(filter: UserFilter): SupportSQLiteQuery {

        val sql = StringBuilder()
        val args = mutableListOf<Any>()

        sql.append("SELECT * FROM user WHERE 1 = 1")

        fun add(condition: String, vararg values: Any) {
            sql.append(" AND $condition")
            args.addAll(values)
        }

        filter.name?.takeIf { it.isNotBlank() }?.let {
            add("LOWER(name) LIKE LOWER(?)", "%$it%")
        }

        filter.lastName?.takeIf { it.isNotBlank() }?.let {
            add("LOWER(lastname) LIKE LOWER(?)", "%$it%")
        }

        filter.telephoneNumber?.takeIf { it.isNotBlank() }?.let {
            add("telephoneNumber LIKE ?", "%$it%")
        }

        filter.dni?.takeIf { it.isNotBlank() }?.let {
            add("dni = ?", it)
        }

        filter.birthDate?.let {
            add("birthDate = ?", it)
        }

        filter.address?.takeIf { it.isNotBlank() }?.let {
            add("LOWER(address) LIKE LOWER(?)", "%$it%")
        }

        filter.email?.takeIf { it.isNotBlank() }?.let {
            add("LOWER(email) LIKE LOWER(?)", "%$it%")
        }

        filter.roleId?.let {
            add("roleId = ?", it)
        }

        filter.active?.let {
            add("active = ?", it)
        }

        filter.createdAtFrom?.let {
            add("createdAt >= ?", it)
        }

        filter.createdAtTo?.let {
            add("createdAt <= ?", it)
        }

        filter.updatedAtFrom?.let {
            add("updatedAt >= ?", it)
        }

        filter.updatedAtTo?.let {
            add("updatedAt <= ?", it)
        }

        val orderColumn = when (filter.sortBy) {
            com.litvy.litvysales.domain.filter.user.UserSortBy.CREATED_AT -> "createdAt"
            com.litvy.litvysales.domain.filter.user.UserSortBy.UPDATED_AT -> "updatedAt"
            com.litvy.litvysales.domain.filter.user.UserSortBy.NAME -> "name"
            com.litvy.litvysales.domain.filter.user.UserSortBy.LASTNAME -> "lastname"
        }

        sql.appendOrderBy(orderColumn, filter.sortDirection)

        return SimpleSQLiteQuery(sql.toString(), args.toTypedArray())
    }
}
