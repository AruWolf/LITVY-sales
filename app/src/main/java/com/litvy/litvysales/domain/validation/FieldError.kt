package com.litvy.litvysales.domain.validation

// Clase auxiliar para indicar el campo que genera error e indicar el mensaje del error correspondiente
data class FieldError(
    val field: String,
    val message: String
)