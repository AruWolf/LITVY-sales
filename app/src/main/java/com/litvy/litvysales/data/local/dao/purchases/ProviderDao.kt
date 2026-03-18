package com.litvy.litvysales.data.local.dao.purchases

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.litvy.litvysales.data.local.entity.purchases.ProviderEntity
import com.litvy.litvysales.data.local.entity.purchases.ProviderVisitDayEntity
import com.litvy.litvysales.data.local.relation.ProviderWithVisitDays
import kotlinx.coroutines.flow.Flow

@Dao
interface ProviderDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(provider: ProviderEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVisitDays(days: List<ProviderVisitDayEntity>)

    @Query("DELETE FROM provider_visit_day WHERE providerId = :providerId")
    suspend fun deleteVisitDaysByProviderId(providerId: Int)

    @Transaction
    suspend fun insertProviderWithDays(
        provider: ProviderEntity,
        visitDays: Set<Int>
    ): Long {
        val providerId = insert(provider).toInt()

        if (visitDays.isNotEmpty()) {
            insertVisitDays(
                visitDays.map { day ->
                    ProviderVisitDayEntity(
                        providerId = providerId,
                        dayOfWeek = day
                    )
                }
            )
        }

        return providerId.toLong()
    }

    @Transaction
    suspend fun updateProviderWithDays(
        provider: ProviderEntity,
        visitDays: Set<Int>
    ) {
        val providerId = requireNotNull(provider.id) {
            "Provider id is required for update"
        }

        update(provider)
        deleteVisitDaysByProviderId(providerId)

        if (visitDays.isNotEmpty()) {
            insertVisitDays(
                visitDays.map { day ->
                    ProviderVisitDayEntity(
                        providerId = providerId,
                        dayOfWeek = day
                    )
                }
            )
        }
    }

    @Update
    suspend fun update(provider: ProviderEntity)

    @Transaction
    @Query("SELECT * FROM provider WHERE id = :id")
    suspend fun getById(id: Int): ProviderWithVisitDays?

    @Transaction
    @Query("SELECT * FROM provider WHERE name = :name COLLATE NOCASE ORDER BY name ASC")
    fun getByName(name: String): Flow<List<ProviderWithVisitDays>>

    @Transaction
    @Query("SELECT * FROM provider ORDER BY name ASC")
    fun getAll(): Flow<List<ProviderWithVisitDays>>
}
