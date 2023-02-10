package com.anafthdev.saku.ui.game

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anafthdev.saku.common.CountUpTimer
import com.anafthdev.saku.data.GameMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
	private val countUpTimer: CountUpTimer
): ViewModel() {
	
	var selectedNumber by mutableStateOf(1)
		private set
	
	var second by mutableStateOf(0)
		private set
	
	var minute by mutableStateOf(0)
		private set
	
	var isPaused by mutableStateOf(false)
		private set
	
	var gameMode by mutableStateOf(GameMode.Easy)
		private set
	
	init {
		viewModelScope.launch(Dispatchers.IO) {
			countUpTimer.second.collect { sec ->
				Timber.i("current second: $sec")
				withContext(Dispatchers.Main) {
					second = sec % 60
					minute = (sec / 60) % 60
				}
			}
		}
	}
	
	fun updateSelectedNumber(n: Int) {
		selectedNumber = n
	}
	
	fun updateSecond(s: Int) {
		second = s
	}
	
	fun updateMinute(m: Int) {
		minute = m
	}
	
	fun pause() {
		countUpTimer.cancel()
	}
	
	fun resume() {
		countUpTimer.start()
	}
	
}