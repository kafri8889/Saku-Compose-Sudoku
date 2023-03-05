package com.anafthdev.saku.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.anafthdev.saku.data.model.Score
import kotlinx.coroutines.flow.Flow

@Dao
interface ScoreDao {
	
	@Query("SELECT * FROM score")
	fun getAll(): Flow<List<Score>>
	
	@Query("SELECT * FROM score WHERE id_score LIKE :mID")
	fun get(mID: Int): Flow<Score>
	
	@Update
	suspend fun update(vararg score: Score)
	
	@Delete
	suspend fun delete(vararg score: Score)
	
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insert(vararg score: Score)
}