package com.anafthdev.saku.common

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CommonModule {

	@Provides
	@Singleton
	fun provideCountUpTimer(): CountUpTimer = CountUpTimer()

}