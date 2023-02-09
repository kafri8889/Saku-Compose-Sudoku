package com.anafthdev.saku.ui.game

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.anafthdev.saku.data.GameMode
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(): ViewModel() {
	
	var second by mutableStateOf(0)
		private set
	
	var minute by mutableStateOf(0)
		private set
	
	var gameMode by mutableStateOf(GameMode.Easy)
		private set
	
	fun updateSecond(s: Int) {
		second = s
	}
	
	fun updateMinute(m: Int) {
		minute = m
	}
	
}