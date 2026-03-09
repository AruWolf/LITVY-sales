package com.litvy.litvysales.data.mapper.promotion

import com.litvy.litvysales.domain.model.enums.PromotionBenefitType as DomainPromotionBenefitType
import com.litvy.litvysales.data.local.entity.enums.PromotionBenefitType as DataPromotionBenefitType
import com.litvy.litvysales.domain.model.enums.PromotionConditionType as DomainPromotionConditionType
import com.litvy.litvysales.data.local.entity.enums.PromotionConditionType as DataPromotionConditionType
import com.litvy.litvysales.data.local.entity.promotion.*
import com.litvy.litvysales.domain.model.promotion.*


// ENUM MAPPERS

fun DataPromotionConditionType.toDomain(): DomainPromotionConditionType =
    DomainPromotionConditionType.valueOf(this.name)

fun DomainPromotionConditionType.toData(): DataPromotionConditionType =
    DataPromotionConditionType.valueOf(this.name)

fun DataPromotionBenefitType.toDomain(): DomainPromotionBenefitType =
    DomainPromotionBenefitType.valueOf(this.name)

fun DomainPromotionBenefitType.toData(): DataPromotionBenefitType =
    DataPromotionBenefitType.valueOf(this.name)


// PROMOTION

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


// CONDITION

fun PromotionConditionEntity.toDomain() =
    PromotionCondition(
        id,
        promotionId,
        type.toDomain(),
        value
    )

fun PromotionCondition.toEntity() =
    PromotionConditionEntity(
        id,
        promotionId,
        type.toData(),
        value
    )


// BENEFIT

fun PromotionBenefitEntity.toDomain() =
    PromotionBenefit(
        id,
        promotionId,
        type.toDomain(),
        value
    )

fun PromotionBenefit.toEntity() =
    PromotionBenefitEntity(
        id,
        promotionId,
        type.toData(),
        value
    )


// TARGET

fun PromotionTargetEntity.toDomain() =
    PromotionTarget(
        id,
        promotionId,
        productId,
        brandId,
        categoryId,
        batchId
    )

fun PromotionTarget.toEntity() =
    PromotionTargetEntity(
        id,
        promotionId,
        productId,
        brandId,
        categoryId,
        batchId
    )


// PRODUCT REQUIREMENT

fun PromotionProductRequirementEntity.toDomain() =
    PromotionProductRequirement(
        id,
        promotionId,
        productId,
        requiredQuantity
    )

fun PromotionProductRequirement.toEntity() =
    PromotionProductRequirementEntity(
        id,
        promotionId,
        productId,
        requiredQuantity
    )