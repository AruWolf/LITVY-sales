package com.litvy.litvysales.data.mapper.purchase

import com.litvy.litvysales.data.local.entity.purchases.*
import com.litvy.litvysales.domain.model.enums.PurchaseOrderStatus
import com.litvy.litvysales.data.local.relation.ProviderWithVisitDays as dataProviderWithVisitDays
import com.litvy.litvysales.domain.model.purchases.ProviderWithVisitDays as domainProviderWithVisitDays
import com.litvy.litvysales.domain.model.enums.PurchaseOrderStatus as domPurchaseOrderStatus
import com.litvy.litvysales.data.local.entity.enums.PurchaseOrderStatus as dataPurchaseOrderStatus
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

fun dataProviderWithVisitDays.toDomain() = domainProviderWithVisitDays(
    provider = provider.toDomain(),
    visitDays = visitDays.map { it.dayOfWeek }.toSet()
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
    id = 0,
    purchaseId = 0,
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
    id = 0,
    purchaseOrderId = 0,
    productId,
    quantity
)

fun domPurchaseOrderStatus.toEntity(): dataPurchaseOrderStatus =
    when(this) {
        domPurchaseOrderStatus.RECEIVED -> dataPurchaseOrderStatus.RECEIVED
        domPurchaseOrderStatus.CANCELLED -> dataPurchaseOrderStatus.CANCELLED
        domPurchaseOrderStatus.PENDING -> dataPurchaseOrderStatus.PENDING
        domPurchaseOrderStatus.SENT -> dataPurchaseOrderStatus.SENT
    }

fun dataPurchaseOrderStatus.toDomain(): domPurchaseOrderStatus =
    when(this) {
        dataPurchaseOrderStatus.RECEIVED -> domPurchaseOrderStatus.RECEIVED
        dataPurchaseOrderStatus.CANCELLED -> domPurchaseOrderStatus.CANCELLED
        dataPurchaseOrderStatus.PENDING -> domPurchaseOrderStatus.PENDING
        dataPurchaseOrderStatus.SENT -> domPurchaseOrderStatus.SENT
    }