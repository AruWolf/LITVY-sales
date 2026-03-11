package com.litvy.litvysales.domain.validation

// Interfaz de contrato por la cual se accede a los servicios de validación
fun interface Validator<T> {
    // Servicio de validacioón, requiere de un valor propiedadad como parametro a evaluar.
    fun validate(value: T): FieldError?
}