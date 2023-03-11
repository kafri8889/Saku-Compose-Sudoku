package com.anafthdev.saku.ui.game

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anafthdev.saku.UserPreferences
import com.anafthdev.saku.common.CountUpTimer
import com.anafthdev.saku.common.GameEngine
import com.anafthdev.saku.data.ARG_GAME_MODE
import com.anafthdev.saku.data.ARG_USE_LAST_BOARD
import com.anafthdev.saku.data.Difficulty
import com.anafthdev.saku.data.model.Cell
import com.anafthdev.saku.data.model.RemainingNumber
import com.anafthdev.saku.data.model.Score
import com.anafthdev.saku.data.repository.ScoreRepository
import com.anafthdev.saku.data.repository.UserPreferencesRepository
import com.anafthdev.saku.uicomponent.SudokuGameAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class GameViewModel @Inject constructor(
	private val gameEngine: GameEngine,
	private val countUpTimer: CountUpTimer,
	private val scoreRepository: ScoreRepository,
	private val savedStateHandle: SavedStateHandle,
	private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {
	
	private val deliveredGameMode: StateFlow<Int> = savedStateHandle.getStateFlow(ARG_GAME_MODE, -1)
	private val useLastBoardState: StateFlow<Boolean> = savedStateHandle.getStateFlow(ARG_USE_LAST_BOARD, false)
	
	private var userPreferences = UserPreferences()
	private var lastUpdatedCell = Cell.NULL
	private var hasValidate = false
	private var hasWin = false
	
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
	
	var showFinishGameDialog by mutableStateOf(false)
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
	val solvedBoard = mutableStateListOf<Cell>()
	val initialBoard = mutableStateListOf<Cell>()
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
					userPreferences = preferences
					remainingNumberEnabled = preferences.remainingNumberEnabled
					highlightNumberEnabled = preferences.highlightNumberEnabled
					
					if (use and !hasWin) {
						second = preferences.time
						difficulty = Difficulty.values()[preferences.gameMode]
						
						withContext(Dispatchers.IO) {
							if (preferences.boardState.isNotBlank() and preferences.solvedBoardState.isNotBlank()) {
								countUpTimer.snapTo(preferences.time)
								gameEngine.init(
									preferences.boardState,
									preferences.solvedBoardState
								)
							}
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
							if (!hasWin) {
								gameEngine.init(mGameMode)
							}
						}
					}
				}
			}
		}
		
		viewModelScope.launch {
			gameEngine.win.collect { mWin ->
				println("win: $mWin")
				
				win = mWin
				
				if (mWin) {
					gameEngine.pause()
					hasWin = true
				} else gameEngine.resume()
			}
		}
		
		viewModelScope.launch {
			gameEngine.currentBoard.collect { mBoard ->
				gameEngine.getRemainingNumber(mBoard)
				
				board.apply {
					clear()
					addAll(mBoard)
				}
				
				if (gameEngine.currentBoard.replayCache.size < 3) {
					initialBoard.apply {
						clear()
						addAll(
							mBoard.map { parentCell ->
								parentCell.copy(
									subCells = parentCell.subCells.map { it.copy(n = if (it.missingNum) 0 else it.n) }
								)
							}
						)
					}
				}
			}
		}
		
		viewModelScope.launch {
			gameEngine.solvedBoard.collect { mBoard ->
				solvedBoard.apply {
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
		saveState()
		
		super.onCleared()
	}
	
	fun saveScore() {
		if (!hasValidate) {
			val mSec = second
			val mDifficulty = difficulty
			
			viewModelScope.launch(Dispatchers.IO) {
				val score = Score(
					id = Random.nextInt(),
					date = System.currentTimeMillis(),
					time = mSec,
					difficulty = mDifficulty
				)
				
				scoreRepository.insert(score)
			}
		}
	}
	
	fun saveState() {
		Timber.i("exit koll")
		pause()
		viewModelScope.launch {
			userPreferencesRepository.apply {
				if (win or hasValidate) {
					update(
						pref = userPreferences.copy(
							time = 0,
							gameMode = 0,
							boardState = "",
							solvedBoardState = ""
						)
					)
				} else {
					Timber.i("seffffff")
					
					val boardState = gameEngine.getBoardStateInJson()
					val solvedBoardState = gameEngine.getSolvedBoardStateInJson()
					
					update(
						pref = userPreferences.copy(
							time = second,
							gameMode = difficulty.ordinal,
							boardState = boardState,
							solvedBoardState = solvedBoardState
						)
					)
				}
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
	
	fun updateIsPaused(paused: Boolean) {
		isPaused = paused
	}
	
	fun updateShowFinishGameDialog(show: Boolean) {
		showFinishGameDialog = show
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
	
	fun validate() {
		hasValidate = true
		win = true
		
		solve()
		pause()
	}
	
	fun pause() {
		gameEngine.pause()
	}
	
	fun resume() {
		gameEngine.resume()
	}
	
	fun resetTimer() {
		viewModelScope.launch {
			countUpTimer.reset()
		}
	}
	
}