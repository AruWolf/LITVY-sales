package com.litvy.litvysales.domain.useCase.catalog.brand

import com.litvy.litvysales.domain.interfaces.catalog.BrandRepository

class UpdateBrandUseCase(
    private val repository: BrandRepository
) {

    suspend operator fun invoke(
        id: Int,
        newName: String
    ){

        val cleanName = newName.trim()

        require(cleanName.isNotEmpty()){
            "Brand name cannot be empty"
        }
        //TODO: Terminar cuando este definido el metodo getById

        //val existing = repository.
    }
}