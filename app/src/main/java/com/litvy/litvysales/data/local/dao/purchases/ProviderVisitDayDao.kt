package com.litvy.litvysales.data.local.dao.purchases

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.litvy.litvysales.data.local.entity.purchases.ProviderEntity
import com.litvy.litvysales.data.local.entity.purchases.ProviderVisitDayEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProviderVisitDayDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(providerVisitDay: ProviderVisitDayEntity)

    @Query("DELETE FROM provider_visit_day WHERE providerId = :providerId")
    suspend fun deleteByProvider(providerId: Int)

    @Update
    suspend fun update(providerVisitDay: ProviderVisitDayEntity)

    @Query("SELECT * FROM provider_visit_day WHERE providerId = :providerId")
    fun getByProvider(providerId: Int): Flow<List<ProviderVisitDayEntity?>>

    @Query("SELECT * FROM provider_visit_day")
    fun getAll(): Flow<List<ProviderVisitDayEntity?>>

    @Query("""
        SELECT pvd.dayOfWeek, p.*
        FROM provider_visit_day pvd
        JOIN provider p ON p.id = pvd.providerId
        ORDER BY pvd.dayOfWeek ASC
        """)
    fun getByVisitDay(): Flow<List<visitDaysWithProviders>>
}


data class visitDaysWithProviders(
        val dayOfWeek: Int,
        @Embedded val provider: ProviderEntity
        )