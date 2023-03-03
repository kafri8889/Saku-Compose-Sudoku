package com.anafthdev.saku.data.serializer

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.anafthdev.saku.UserPreferences
import okio.IOException
import java.io.InputStream
import java.io.OutputStream

object UserPreferencesSerializer: Serializer<UserPreferences> {
	
	override val defaultValue: UserPreferences
		get() = UserPreferences()
	
	override suspend fun readFrom(input: InputStream): UserPreferences {
		try {
			return UserPreferences.ADAPTER.decode(input)
		} catch (e: IOException) {
			throw CorruptionException("Cannot read proto", e)
		}
	}
	
	override suspend fun writeTo(t: UserPreferences, output: OutputStream) {
		UserPreferences.ADAPTER.encode(output, t)
	}
}