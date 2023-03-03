package com.anafthdev.saku.ui.dashboard

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.anafthdev.saku.data.GameMode
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(): ViewModel() {
	
	var selectedGameMode by mutableStateOf(GameMode.Fast)
	private set
	
	fun updateGameMode(mode: GameMode) {
		selectedGameMode = mode
	}
	
}