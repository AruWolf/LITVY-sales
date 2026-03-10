package com.litvy.litvysales.util

import java.text.NumberFormat
import java.util.*

fun formatPrice(value: String): String {

    if (value.isEmpty()) return ""

    val number = value.toLong()

    val formatter = NumberFormat.getNumberInstance(Locale("es", "AR"))

    return formatter.format(number)

}