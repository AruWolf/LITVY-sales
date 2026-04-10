package com.litvy.litvysales.data.mapper.sales

import com.litvy.litvysales.data.local.entity.sales.*
import com.litvy.litvysales.domain.model.sales.*

/*
========================
CashMovement
========================
*/

fun CashMovement.toEntity(): CashMovementEntity =
    CashMovementEntity(
        id = id,
        cashSessionId = cashSessionId,
        type = type,
        amountInCents = amountInCents,
        reason = reason,
        createdAt = createdAt,
        createdBy = createdBy
    )

fun CashMovementEntity.toDomain(): CashMovement =
    CashMovement(
        id = id,
        cashSessionId = cashSessionId,
        type = type,
        amountInCents = amountInCents,
        reason = reason,
        createdAt = createdAt,
        createdBy = createdBy
    )


/*
========================
CashRegister
========================
*/

fun CashRegister.toEntity(): CashRegisterEntity =
    CashRegisterEntity(
        id = id,
        name = name,
        location = location,
        active = active
    )

fun CashRegisterEntity.toDomain(): CashRegister =
    CashRegister(
        id = id,
        name = name,
        location = location,
        active = active
    )


/*
========================
CashSession
========================
*/

fun CashSession.toEntity(): CashSessionEntity =
    CashSessionEntity(
        id = id,
        cashRegisterId = cashRegisterId,
        startedAt = startedAt,
        closedAt = closedAt,
        openingAmountInCents = openingAmountInCents,
        closingAmountInCents = closingAmountInCents,
        expectedAmountInCents = expectedAmountInCents,
        differenceInCents = differenceInCents,
        status = status,
        openedBy = openedBy,
        closedBy = closedBy
    )

fun CashSessionEntity.toDomain(): CashSession =
    CashSession(
        id = id,
        cashRegisterId = cashRegisterId,
        startedAt = startedAt,
        closedAt = closedAt,
        openingAmountInCents = openingAmountInCents,
        closingAmountInCents = closingAmountInCents,
        expectedAmountInCents = expectedAmountInCents,
        differenceInCents = differenceInCents,
        status = status,
        openedBy = openedBy,
        closedBy = closedBy
    )


/*
========================
Customer
========================
*/

fun Customer.toEntity(): CustomerEntity =
    CustomerEntity(
        id = id,
        name = name,
        lastname = lastname,
        cuit = cuit,
        telephoneNumber = telephoneNumber,
        address = address,
        email = email,
        createdAt = createdAt
    )

fun CustomerEntity.toDomain(): Customer =
    Customer(
        id = id,
        name = name,
        lastname = lastname,
        cuit = cuit,
        telephoneNumber = telephoneNumber,
        address = address,
        email = email,
        createdAt = createdAt
    )


/*
========================
Sale
========================
*/

fun com.litvy.litvysales.domain.model.enums.SaleStatus.toEntity():
        com.litvy.litvysales.data.local.entity.enums.SaleStatus {
    return com.litvy.litvysales.data.local.entity.enums.SaleStatus.valueOf(this.name)
}

fun com.litvy.litvysales.data.local.entity.enums.SaleStatus.toDomain():
        com.litvy.litvysales.domain.model.enums.SaleStatus {
    return com.litvy.litvysales.domain.model.enums.SaleStatus.valueOf(this.name)
}

fun Sale.toEntity(): SaleEntity =
    SaleEntity(
        id = id,
        cashSessionId = cashSessionId,
        sellerId = sellerId,
        customerId = customerId,
        totalDiscountInCents = totalDiscountInCents,
        totalInCents = totalInCents,
        status = status.toEntity(),
        cancellationReason = cancellationReason,
        createdAt = createdAt
    )

fun SaleEntity.toDomain(): Sale =
    Sale(
        id = id,
        cashSessionId = cashSessionId,
        sellerId = sellerId,
        customerId = customerId,
        totalDiscountInCents = totalDiscountInCents,
        totalInCents = totalInCents,
        status = status.toDomain(),
        cancellationReason = cancellationReason,
        createdAt = createdAt
    )


/*
========================
SaleItem
========================
*/

fun SaleItem.toEntity(): SaleItemEntity =
    SaleItemEntity(
        id = id,
        saleId = saleId,
        productId = productId,
        quantity = quantity,
        unitPriceInCents = unitPriceInCents,
        discountAppliedInCents = discountAppliedInCents,
        originalUnitPriceInCents = originalUnitPriceInCents,
        totalInCents = totalInCents
    )

fun SaleItemEntity.toDomain(): SaleItem =
    SaleItem(
        id = id,
        saleId = saleId,
        productId = productId,
        quantity = quantity,
        unitPriceInCents = unitPriceInCents,
        discountAppliedInCents = discountAppliedInCents,
        originalUnitPriceInCents = originalUnitPriceInCents,
        totalInCents = totalInCents
    )


/*
========================
SalePayment
========================
*/

fun SalePayment.toEntity(): SalePaymentEntity =
    SalePaymentEntity(
        id = id,
        saleId = saleId,
        paymentMethodId = paymentMethodId,
        amountInCents = amountInCents
    )

fun SalePaymentEntity.toDomain(): SalePayment =
    SalePayment(
        id = id,
        saleId = saleId,
        paymentMethodId = paymentMethodId,
        amountInCents = amountInCents
    )


/*
========================
SalePromotion
========================
*/

fun SalePromotion.toEntity(): SalePromotionEntity =
    SalePromotionEntity(
        id = id,
        saleId = saleId,
        promotionId = promotionId,
        discountAppliedInCents = discountAppliedInCents
    )

fun SalePromotionEntity.toDomain(): SalePromotion =
    SalePromotion(
        id = id,
        saleId = saleId,
        promotionId = promotionId,
        discountAppliedInCents = discountAppliedInCents
    )


/*
========================
TaxItem
========================
*/

fun TaxItem.toEntity(): TaxItemEntity =
    TaxItemEntity(
        id = id,
        name = name,
        percentage = percentage
    )

fun TaxItemEntity.toDomain(): TaxItem =
    TaxItem(
        id = id,
        name = name,
        percentage = percentage
    )