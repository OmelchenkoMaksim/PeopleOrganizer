package com.instagirlsorgainzer.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.instagirlsorgainzer.room.GirlEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GirlDao {
    @Query("SELECT * FROM girls")
    fun getAllGirls(): Flow<List<GirlEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGirl(girl: GirlEntity)

    @Delete
    suspend fun deleteGirl(girl: GirlEntity)
}