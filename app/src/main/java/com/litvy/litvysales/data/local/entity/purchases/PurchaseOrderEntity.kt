package com.litvy.litvysales.data.local.entity.purchases

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.litvy.litvysales.data.local.entity.user.UserEntity
import com.litvy.litvysales.data.local.entity.enums.PurchaseOrderStatus

@Entity(
    tableName = "purchaseOrder",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["createdBy"],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = ProviderEntity::class,
            parentColumns = ["id"],
            childColumns = ["providerId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [Index("createdBy")]
)
data class PurchaseOrderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val providerId: Int,
    val status: PurchaseOrderStatus,
    val expectedDeliveryDate: Long?,
    val createdAt: Long,
    val createdBy: Int
)