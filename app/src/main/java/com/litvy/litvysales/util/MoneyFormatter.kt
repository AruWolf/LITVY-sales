package com.litvy.litvysales.util

import java.text.NumberFormat
import java.util.Locale

object MoneyFormatter {

    private val formatter =
        NumberFormat.getCurrencyInstance(Locale("es", "AR"))

    fun formatFromInput(value: String): String {

        if (value.isBlank()) return ""

        val number = value.toLong()

        return formatter.format(number)

    }

    fun formatFromCents(cents: Long): String {

        val value = cents / 100.0

        return formatter.format(value)

    }

}