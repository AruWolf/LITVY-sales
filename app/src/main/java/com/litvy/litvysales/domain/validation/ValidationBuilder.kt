package com.litvy.litvysales.domain.validation

// Motor de validación.
class ValidationBuilder {

    private val errors = mutableListOf<FieldIssue>()
    private val warnings = mutableListOf<FieldIssue>()

    fun error(
        field: String,
        message: String
    ) {
        errors.add(FieldIssue(field, message))
    }

    fun warning(
        field: String,
        message: String
    ) {
        warnings.add(FieldIssue(field, message))
    }

    fun check(
        condition: Boolean,
        field: String,
        message: String
    ) {
        if (!condition) error(field, message)
    }

    fun checkWarning(
        condition: Boolean,
        field: String,
        message: String
    ) {
        if (!condition) warning(field, message)
    }

    fun build(): ValidationResult {

        if (errors.isEmpty() && warnings.isEmpty()) {
            return ValidationResult.Success
        }

        return ValidationResult.Failure(
            errors = errors,
            warnings = warnings
        )
    }

    fun add(issue: FieldIssue?) {

        issue?.let {

            errors.add(it)

        }

    }
}