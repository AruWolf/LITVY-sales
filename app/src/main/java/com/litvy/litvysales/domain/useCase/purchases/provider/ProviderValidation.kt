package com.litvy.litvysales.domain.useCase.purchases.provider

import android.util.Patterns
import com.litvy.litvysales.domain.model.purchases.Provider
import com.litvy.litvysales.domain.validation.CommonValidators
import com.litvy.litvysales.domain.validation.ValidationBuilder

internal fun Provider.normalizeForPersistence(id: Int? = this.id): Provider =
    copy(
        id = id,
        name = name.trim(),
        cuit = cuit.normalizeOptionalField(),
        telephoneNumber = telephoneNumber.normalizeOptionalField(),
        address = address.normalizeOptionalField(),
        email = email.normalizeOptionalField()
    )

internal fun Collection<Int>.normalizeVisitDays(): Set<Int> = toSet()

internal fun validateProviderData(
    validator: ValidationBuilder,
    provider: Provider,
    visitDays: Set<Int>
) {
    validator.add(CommonValidators.notBlank("name", provider.name))

    provider.telephoneNumber?.let { phone ->
        validator.check(
            phone.length in 10..11,
            "telephoneNumber",
            "Numero de telefono invalido"
        )
        validator.check(
            phone.all { it.isDigit() },
            "telephoneNumber",
            "Solo se permiten numeros"
        )
    }

    provider.email?.let { email ->
        validator.check(
            Patterns.EMAIL_ADDRESS.matcher(email).matches(),
            "email",
            "Correo invalido"
        )
    }

    provider.cuit?.let { cuit ->
        validator.check(
            cuit.all { it.isDigit() } && cuit.length == 11,
            "cuit",
            "CUIT invalido"
        )
    }

    validator.check(
        visitDays.all { it in 1..7 },
        "visitDays",
        "Los dias de visita deben estar entre 1 y 7"
    )
}

private fun String?.normalizeOptionalField(): String? =
    this
        ?.trim()
        ?.takeIf { it.isNotEmpty() }
