package com.anafthdev.saku.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import com.anafthdev.saku.UserPreferences
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserPreferencesRepository @Inject constructor(
	private val userPreferencesDataStore: DataStore<UserPreferences>
) {
	
	suspend fun update(pref: UserPreferences) {
		userPreferencesDataStore.updateData { currentPreferences ->
			currentPreferences.copy(
				time = pref.time,
				gameMode = pref.gameMode,
				boardState = pref.boardState,
				solvedBoardState = pref.solvedBoardState,
				exportBoardPath = pref.exportBoardPath,
				highlightNumberEnabled = pref.highlightNumberEnabled,
				remainingNumberEnabled = pref.remainingNumberEnabled
			)
		}
	}
	
	suspend fun setTime(seconds: Int) {
		userPreferencesDataStore.updateData { currentPreferences ->
			currentPreferences.copy(
				time = seconds
			)
		}
	}
	
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
	
	suspend fun setExportBoardPath(path: String) {
		userPreferencesDataStore.updateData { currentPreferences ->
			currentPreferences.copy(
				exportBoardPath = path
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