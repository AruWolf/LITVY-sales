package com.litvy.litvysales.domain.validation

import androidx.room.RoomWarnings

// Posibles resultados de la validación
sealed class ValidationResult {

    // Resultado exitoso, en cuanto a la validación realizada.
    object Success : ValidationResult()

    // Acumulador de errores que almacena los valores que no han cumplido con las validaciones.
    data class Failure(
        val errors: List<FieldIssue>,
        val warnings: List<FieldIssue>
    ) : ValidationResult()

}