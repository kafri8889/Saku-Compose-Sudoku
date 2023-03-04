package com.anafthdev.saku.ui.dashboard

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anafthdev.saku.UserPreferences
import com.anafthdev.saku.data.GameMode
import com.anafthdev.saku.data.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
	private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {
	
	var canResume by mutableStateOf(false)
		private set
	
	var showNewGameDialog by mutableStateOf(false)
		private set
	
	var lastGameMode by mutableStateOf(GameMode.Fast)
		private set
	
	var selectedGameMode by mutableStateOf(GameMode.Fast)
		private set
	
	private var userPreferences = UserPreferences()
	
	init {
		viewModelScope.launch {
			userPreferencesRepository.getUserPreferences.collect { preferences ->
				canResume = preferences.boardState.isNotBlank()
				lastGameMode = GameMode.values()[preferences.gameMode]
				
				userPreferences = preferences
			}
		}
	}
	
	fun updateGameMode(mode: GameMode) {
		selectedGameMode = mode
	}
	
	fun updateShowNewGameDialog(show: Boolean) {
		showNewGameDialog = show
	}
}