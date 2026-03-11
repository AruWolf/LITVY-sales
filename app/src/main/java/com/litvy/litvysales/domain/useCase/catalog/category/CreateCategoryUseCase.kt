package com.litvy.litvysales.domain.useCase.catalog.category

import com.litvy.litvysales.domain.interfaces.catalog.CategoryRepository
import com.litvy.litvysales.domain.model.catalog.Category
import com.litvy.litvysales.domain.validation.CommonValidators
import com.litvy.litvysales.domain.validation.ValidationBuilder
import com.litvy.litvysales.domain.validation.ValidationResult

class CreateCategoryUseCase(
    private val repository: CategoryRepository
) {

    // Invocación de la función de creación de categoria.
    // Requiere de la carga del nombre de la categoria por parte del usuario. Recibe este dato como parametro.
    suspend operator fun invoke(name: String): ValidationResult { // -> Hereda las funciones de validación de ValidationResult,
                                                                  // para capturar y devolver errores en caso de ser necesario
        // Limpieza del nombre. Quita los espacios vacios a los costados.
        val cleanName = name.trim()

        // Instancia de validador
        val validator = ValidationBuilder()

        // Validación de nombre vacio
        validator.add(
            CommonValidators.notBlank(
                "name",
                cleanName
            )
        )

        // Validación de categoria existente, no permite duplicados de categorias, identificadas bajo el mismo nombre.
        if (repository.existsByName(cleanName)) {
            validator.check(
                false,
                "name",
                "La categoria ya existe"
            )
        }

        // Almacenamiento de los resultados de la validación relevada anteriormente
        val result = validator.build()

        // En caso de que la validación haya capturado un error,
        // va a cerrar el caso de uso sin guardar los valores de creación cargados
        if (result is ValidationResult.Failure) return result

        // --- FLUJO NORMAL ---
        // En caso de que el validador no haya detectado valores incorrectos
        val now = System.currentTimeMillis()

        // Creación de la categoria bajo los campos cargados por el usuario, en este caso "name".
        repository.create(
         Category(
            id = null,
            name = cleanName,
            createdAt = now,
            updatedAt = now
        ))

        // Resultado de validación que indica que la misma no ha detectado errores.
        return ValidationResult.Success
    }
}