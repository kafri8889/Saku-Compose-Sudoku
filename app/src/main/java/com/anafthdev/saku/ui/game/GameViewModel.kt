package com.anafthdev.saku.ui.game

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anafthdev.saku.common.CountUpTimer
import com.anafthdev.saku.common.GameEngine
import com.anafthdev.saku.data.ARG_GAME_MODE
import com.anafthdev.saku.data.ARG_USE_LAST_BOARD
import com.anafthdev.saku.data.Difficulty
import com.anafthdev.saku.data.model.Cell
import com.anafthdev.saku.data.model.RemainingNumber
import com.anafthdev.saku.data.repository.UserPreferencesRepository
import com.anafthdev.saku.uicomponent.SudokuGameAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
	private val gameEngine: GameEngine,
	private val countUpTimer: CountUpTimer,
	private val savedStateHandle: SavedStateHandle,
	private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {
	
	private val deliveredGameMode: StateFlow<Int> = savedStateHandle.getStateFlow(ARG_GAME_MODE, -1)
	private val useLastBoardState: StateFlow<Boolean> = savedStateHandle.getStateFlow(ARG_USE_LAST_BOARD, false)
	
	private var lastUpdatedCell = Cell.NULL
	
	var selectedNumber by mutableStateOf(1)
		private set
	
	var second by mutableStateOf(0)
		private set
	
	var minute by mutableStateOf(0)
		private set
	
	var hours by mutableStateOf(0)
		private set
	
	var remainingNumberEnabled by mutableStateOf(false)
		private set
	
	var highlightNumberEnabled by mutableStateOf(false)
		private set
	
	var isPaused by mutableStateOf(false)
		private set
	
	var win by mutableStateOf(false)
		private set
	
	var difficulty by mutableStateOf(Difficulty.Fast)
		private set
	
	var selectedCell by mutableStateOf(Cell(1))
		private set
	
	var selectedGameAction by mutableStateOf(SudokuGameAction.None)
		private set
	
	val board = mutableStateListOf<Cell>()
	val remainingNumbers = mutableStateListOf<RemainingNumber>()
	
	init {
		viewModelScope.launch(Dispatchers.IO) {
			combine(
				userPreferencesRepository.getUserPreferences,
				useLastBoardState
			) { preferences, use ->
				preferences to use
			}.collect { (preferences, use) ->
				withContext(Dispatchers.Main) {
					remainingNumberEnabled = preferences.remainingNumberEnabled
					highlightNumberEnabled = preferences.highlightNumberEnabled
					
					if (use) {
						difficulty = Difficulty.values()[preferences.gameMode]
						
						withContext(Dispatchers.IO) {
							gameEngine.init(
								preferences.boardState,
								preferences.solvedBoardState
							)
						}
					}
				}
			}
		}
		
		viewModelScope.launch(Dispatchers.IO) {
			deliveredGameMode.collect { ordinal ->
				withContext(Dispatchers.Main) {
					if (ordinal != -1) {
						difficulty = Difficulty.values()[ordinal]
						
						// Get difficulty from mutableState in main thread
						val mGameMode = difficulty
						
						withContext(Dispatchers.IO) {
							gameEngine.init(mGameMode)
						}
					}
				}
			}
		}
		
		viewModelScope.launch {
			gameEngine.win.collect { mWin ->
				println("win: $mWin")
				
				win = mWin
				
				if (win) countUpTimer.cancel()
				else countUpTimer.start()
			}
		}
		
		viewModelScope.launch {
			gameEngine.currentBoard.collect { mBoard ->
				gameEngine.getRemainingNumber(mBoard)
				
				board.apply {
					clear()
					addAll(mBoard)
				}
			}
		}
		
		viewModelScope.launch {
			gameEngine.remainingNumbers.collect { numbers ->
				remainingNumbers.apply {
					clear()
					addAll(numbers)
				}
			}
		}
		
		viewModelScope.launch(Dispatchers.IO) {
			gameEngine.second.collect { sec ->
				withContext(Dispatchers.Main) {
					second = sec % 60
					minute = (sec / 60) % 60
					hours = sec / 3600
				}
			}
		}
	}
	
	override fun onCleared() {
		exit()
		
		super.onCleared()
	}
	
	fun saveState() {
		viewModelScope.launch(Dispatchers.IO) {
			userPreferencesRepository.apply {
				setGameMode(difficulty.ordinal)
				setBoardState(gameEngine.getBoardStateInJson())
				setSolvedBoardState(gameEngine.getSolvedBoardStateInJson())
			}
		}
	}
	
	fun updateBoard(cell: Cell) {
		viewModelScope.launch {
			if (cell.missingNum) {
				lastUpdatedCell = cell
				gameEngine.updateBoard(cell, selectedNumber, selectedGameAction)
			}
			
			if (cell.n != 0) {
				selectedCell = cell
			}
		}
	}
	
	fun updateSelectedGameAction(action: SudokuGameAction) {
		selectedGameAction = action
	}
	
	fun updateSelectedNumber(n: Int) {
		selectedNumber = n
		
		selectedCell = Cell(selectedNumber)
	}
	
	fun updateSecond(s: Int) {
		second = s
	}
	
	fun updateMinute(m: Int) {
		minute = m
	}
	
	fun undo() {
		viewModelScope.launch {
			gameEngine.undo()
		}
	}
	
	fun redo() {
		viewModelScope.launch {
			gameEngine.redo()
		}
	}
	
	fun solve() {
		viewModelScope.launch {
			gameEngine.solve()
		}
	}
	
	fun pause() {
		gameEngine.pause()
	}
	
	fun resume() {
		gameEngine.resume()
	}
	
	fun exit() {
		viewModelScope.launch(Dispatchers.IO) {
			countUpTimer.reset()
			userPreferencesRepository.apply {
				setGameMode(difficulty.ordinal)
				setBoardState(gameEngine.getBoardStateInJson())
				setSolvedBoardState(gameEngine.getSolvedBoardStateInJson())
			}
		}
	}
	
}