package com.anafthdev.saku.data.repository

import com.anafthdev.saku.data.local.database.dao.ScoreDao
import com.anafthdev.saku.data.model.Score
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@Module
@InstallIn(ActivityRetainedComponent::class)
class ScoreRepository @Inject constructor(
	private val scoreDao: ScoreDao
) {

	fun getAll(): Flow<List<Score>> = scoreDao.getAll()
	
	fun get(id: Int): Flow<Score> = scoreDao.get(id)
	
	suspend fun update(vararg score: Score) = scoreDao.update(*score)
	
	suspend fun delete(vararg score: Score) = scoreDao.delete(*score)
	
	suspend fun insert(vararg score: Score) = scoreDao.insert(*score)

}