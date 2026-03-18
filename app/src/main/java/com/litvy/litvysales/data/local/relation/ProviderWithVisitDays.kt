package com.litvy.litvysales.data.local.relation

import androidx.room.*
import com.litvy.litvysales.data.local.entity.purchases.*

data class ProviderWithVisitDays (
    @Embedded
    val provider: ProviderEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "providerId"
    )
    val visitDays: List<ProviderVisitDayEntity>
)