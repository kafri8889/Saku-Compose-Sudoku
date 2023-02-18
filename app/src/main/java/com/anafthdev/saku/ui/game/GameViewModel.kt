package com.anafthdev.saku.ui.game

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anafthdev.saku.common.GameEngine
import com.anafthdev.saku.data.GameMode
import com.anafthdev.saku.data.model.Cell
import com.anafthdev.saku.uicomponent.SudokuGameAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
	private val gameEngine: GameEngine
): ViewModel() {
	
	private var lastUpdatedCell = Cell.NULL
	
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
	
	var selectedGameAction by mutableStateOf(SudokuGameAction.None)
		private set
	
//	private val _board = MutableStateFlow(emptyList<Cell>())
//	val board: StateFlow<List<Cell>> = _board
	
	val board = mutableStateListOf<Cell>()
	
	init {
		viewModelScope.launch {
			gameEngine.currentBoard.collect { mBoard ->
//				_board.emit(mBoard)
				
				board.apply {
					if (isEmpty()) {
						clear()
						addAll(mBoard)
					}
					
					if (isNotEmpty()) {
						set(lastUpdatedCell.parentN - 1, mBoard[lastUpdatedCell.parentN - 1])
					}
				}
			}
		}
		
		viewModelScope.launch(Dispatchers.IO) {
			gameEngine.second.collect { sec ->
				withContext(Dispatchers.Main) {
					second = sec % 60
					minute = (sec / 60) % 60
				}
			}
		}
	}
	
	fun init() {
		viewModelScope.launch {
			gameEngine.init(GameMode.Easy)
		}
	}
	
	fun updateBoard(cell: Cell) {
		viewModelScope.launch {
			lastUpdatedCell = cell
			gameEngine.updateBoard(cell, selectedNumber, selectedGameAction)
		}
	}
	
	fun updateSelectedGameAction(action: SudokuGameAction) {
		selectedGameAction = action
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
		gameEngine.pause()
	}
	
	fun resume() {
		gameEngine.resume()
	}
	
}