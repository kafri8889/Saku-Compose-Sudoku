package com.anafthdev.saku.ui.setting

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anafthdev.saku.data.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
	private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {
	
	var remainingNumberEnabled by mutableStateOf(false)
		private set
	
	var highlightNumberEnabled by mutableStateOf(false)
		private set
	
	init {
		viewModelScope.launch {
			userPreferencesRepository.getUserPreferences.collect { preferences ->
				remainingNumberEnabled = preferences.remainingNumberEnabled
				highlightNumberEnabled = preferences.highlightNumberEnabled
			}
		}
	}
	
	fun updateRemainingNumberEnabled(enabled: Boolean) {
		viewModelScope.launch(Dispatchers.IO) {
			userPreferencesRepository.setRemainingNumberEnabled(enabled)
		}
	}
	
	fun updateHighlightNumberEnabled(enabled: Boolean) {
		viewModelScope.launch(Dispatchers.IO) {
			userPreferencesRepository.setHighlightNumberEnabled(enabled)
		}
	}
	
}