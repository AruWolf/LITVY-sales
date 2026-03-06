package com.litvy.litvysales.data.mapper.purchase

import com.litvy.litvysales.data.local.entity.purchases.*
import com.litvy.litvysales.domain.model.purchases.*

fun ProviderEntity.toDomain() = Provider(
    id,
    name,
    cuit,
    telephoneNumber,
    address,
    email
)

fun Provider.toEntity() = ProviderEntity(
    id,
    name,
    cuit,
    telephoneNumber,
    address,
    email
)

fun ProviderVisitDayEntity.toDomain() = ProviderVisitDay(
    providerId,
    dayOfWeek
)

fun ProviderVisitDay.toEntity() = ProviderVisitDayEntity(
    providerId,
    dayOfWeek
)

fun InvoiceTypeEntity.toDomain() = InvoiceType(
    id,
    code,
    description
)

fun InvoiceType.toEntity() = InvoiceTypeEntity(
    id,
    code,
    description
)

fun PurchaseEntity.toDomain() = Purchase(
    id,
    providerId,
    invoiceTypeId,
    paymentMethodId,
    subtotalInCents,
    totalDiscountInCents,
    totalTaxInCents,
    totalInCents,
    createdAt,
    createdBy
)

fun Purchase.toEntity() = PurchaseEntity(
    id,
    providerId,
    invoiceTypeId,
    paymentMethodId,
    subtotalInCents,
    totalDiscountInCents,
    totalTaxInCents,
    totalInCents,
    createdAt,
    createdBy
)

fun PurchaseItemEntity.toDomain() = PurchaseItem(
    id,
    purchaseId,
    productId,
    quantity,
    unitPriceInCents
)

fun PurchaseItem.toEntity() = PurchaseItemEntity(
    id,
    purchaseId,
    productId,
    quantity,
    unitPriceInCents
)

fun PurchaseOrderEntity.toDomain() = PurchaseOrder(
    id,
    providerId,
    status,
    expectedDeliveryDate,
    createdAt,
    createdBy
)

fun PurchaseOrder.toEntity() = PurchaseOrderEntity(
    id,
    providerId,
    status,
    expectedDeliveryDate,
    createdAt,
    createdBy
)

fun PurchaseOrderItemEntity.toDomain() = PurchaseOrderItem(
    id,
    purchaseOrderId,
    productId,
    quantity
)

fun PurchaseOrderItem.toEntity() = PurchaseOrderItemEntity(
    id,
    purchaseOrderId,
    productId,
    quantity
)