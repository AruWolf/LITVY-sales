package com.litvy.litvysales.data.local.query

import com.litvy.litvysales.domain.filter.common.QuerySortDirection

internal fun StringBuilder.appendOrderBy(
    column: String,
    direction: QuerySortDirection
) {
    append(" ORDER BY $column ${direction.name}")
}

internal fun StringBuilder.appendPagination(
    args: MutableList<Any>,
    limit: Int?,
    offset: Int?
) {
    limit?.takeIf { it > 0 }?.let {
        append(" LIMIT ?")
        args.add(it)
    }

    offset?.takeIf { it >= 0 }?.let {
        if (limit == null || limit <= 0) {
            append(" LIMIT -1")
        }

        append(" OFFSET ?")
        args.add(it)
    }
}
