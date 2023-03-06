package com.anafthdev.saku.common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class CountUpTimer @Inject constructor() {
	
	private var isPaused: Boolean = true
	private var timerJob: Job? = null
	private var listener: CountUpTimerListener? = null
	
	private val _second = MutableStateFlow(0)
	val second: StateFlow<Int> = _second
	
	suspend fun snapTo(sec: Int) {
		_second.emit(sec)
	}
	
	suspend fun reset() {
		_second.emit(0)
	}
	
	fun start() {
		if (timerJob == null) {
			timerJob = CoroutineScope(Dispatchers.IO).launch {
				while (true) {
					delay(1000)
					
					if (!isPaused) {
						_second.emit(second.value + 1)
						
						listener?.onTick()
					}
				}
			}
		}
		
		isPaused = false
	}
	
	fun cancel() {
		isPaused = true
	}
	
	fun setListener(l: CountUpTimerListener) {
		listener = l
	}
	
	interface CountUpTimerListener {
		
		fun onTick()
	}
	
}
