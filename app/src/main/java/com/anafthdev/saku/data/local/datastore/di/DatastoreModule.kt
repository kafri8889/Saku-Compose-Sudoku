package com.anafthdev.saku.data.local.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.anafthdev.saku.UserPreferences
import com.anafthdev.saku.data.Constant
import com.anafthdev.saku.data.repository.UserPreferencesRepository
import com.anafthdev.saku.data.serializer.UserPreferencesSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatastoreModule {
	
	@Provides
	@Singleton
	fun provideUserPreferencesDatastore(
		@ApplicationContext context: Context
	): DataStore<UserPreferences> {
		return DataStoreFactory.create(
			serializer = UserPreferencesSerializer,
			corruptionHandler = UserPreferencesRepository.corruptionHandler,
			produceFile = { context.dataStoreFile(Constant.USER_PREFERENCES) }
		)
	}
	
}