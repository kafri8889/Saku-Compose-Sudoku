package com.anafthdev.saku.data.local.database

import android.content.Context
import com.anafthdev.saku.data.local.database.dao.ScoreDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
	
	@Provides
	@Singleton
	fun provideAppDatabase(
		@ApplicationContext context: Context
	): AppDatabase = AppDatabase.getInstance(context)
	
	@Provides
	@Singleton
	fun provideScoreDao(
		appDatabase: AppDatabase
	): ScoreDao = appDatabase.scoreDao()
	
}