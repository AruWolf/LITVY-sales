package com.litvy.litvysales.domain.useCase.purchases

data class PurchaseUseCases(

    val registerPurchase: RegisterPurchaseUseCase,

    val getPurchaseById: GetPurchaseByIdUseCase,

    val getPurchasesByProvider: GetPurchasesByProviderUseCase

)