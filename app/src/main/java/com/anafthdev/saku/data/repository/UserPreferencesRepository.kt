package com.anafthdev.saku.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import com.anafthdev.saku.UserPreferences
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserPreferencesRepository @Inject constructor(
	private val userPreferencesDataStore: DataStore<UserPreferences>
) {
	
	suspend fun setGameMode(ordinal: Int) {
		userPreferencesDataStore.updateData { currentPreferences ->
			currentPreferences.copy(
				gameMode = ordinal
			)
		}
	}
	
	suspend fun setBoardState(json: String) {
		userPreferencesDataStore.updateData { currentPreferences ->
			currentPreferences.copy(
				boardState = json
			)
		}
	}
	
	suspend fun setSolvedBoardState(json: String) {
		userPreferencesDataStore.updateData { currentPreferences ->
			currentPreferences.copy(
				solvedBoardState = json
			)
		}
	}
	
	suspend fun setHighlightNumberEnabled(enabled: Boolean) {
		userPreferencesDataStore.updateData { currentPreferences ->
			currentPreferences.copy(
				highlightNumberEnabled = enabled
			)
		}
	}
	
	suspend fun setRemainingNumberEnabled(enabled: Boolean) {
		userPreferencesDataStore.updateData { currentPreferences ->
			currentPreferences.copy(
				remainingNumberEnabled = enabled
			)
		}
	}
	
	val getUserPreferences: Flow<UserPreferences> = userPreferencesDataStore.data

	companion object {
		val corruptionHandler = ReplaceFileCorruptionHandler(
			produceNewData = { UserPreferences() }
		)
	}

}