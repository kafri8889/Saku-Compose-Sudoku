package com.anafthdev.saku.runtime

import android.app.Application
import com.anafthdev.saku.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class SakuApplication: Application() {
	
	override fun onCreate() {
		super.onCreate()
		if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
	}
}