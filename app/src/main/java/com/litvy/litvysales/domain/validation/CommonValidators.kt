package com.litvy.litvysales.domain.validation

// Validaciones comunes/genericas
object CommonValidators {

    // Validación campo vacio
    fun notBlank(
        field: String,
        value: String
    ): FieldIssue? {

        return if (value.isBlank()) {

            FieldIssue(
                field,
                "El campo no puede estar vacío"
            )

        } else null
    }

    // Validación numeros positivos
    fun positive(
        field: String,
        value: Long
    ): FieldIssue? {

        return if (value <= 0) {

            FieldIssue(
                field,
                "El valor debe ser mayor a 0"
            )

        } else null
    }

}