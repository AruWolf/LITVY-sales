package com.litvy.litvysales.data.mapper.promotion

import com.litvy.litvysales.data.local.entity.promotion.*
import com.litvy.litvysales.domain.model.promotion.*

fun PromotionEntity.toDomain() = Promotion(
    id,
    name,
    description,
    priority,
    stackable,
    active,
    clearStock,
    startDate,
    endDate,
    createdAt
)

fun Promotion.toEntity() = PromotionEntity(
    id,
    name,
    description,
    priority,
    stackable,
    active,
    clearStock,
    startDate,
    endDate,
    createdAt
)

fun PromotionConditionEntity.toDomain() = PromotionCondition(
    id,
    promotionId,
    minQuantity,
    maxQuantity,
    requiredQuantity,
    minSubtotalInCents
)

fun PromotionCondition.toEntity() = PromotionConditionEntity(
    id,
    promotionId,
    minQuantity,
    maxQuantity,
    requiredQuantity,
    minSubtotalInCents
)

fun PromotionBenefitEntity.toDomain() = PromotionBenefit(
    id,
    promotionId,
    discountPercentage,
    discountAmountInCents,
    freeQuantity
)

fun PromotionBenefit.toEntity() = PromotionBenefitEntity(
    id,
    promotionId,
    discountPercentage,
    discountAmountInCents,
    freeQuantity
)

fun PromotionTargetEntity.toDomain() = PromotionTarget(
    id,
    promotionId,
    productId,
    brandId,
    categoryId,
    batchId
)

fun PromotionTarget.toEntity() = PromotionTargetEntity(
    id,
    promotionId,
    productId,
    brandId,
    categoryId,
    batchId
)