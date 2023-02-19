package com.anafthdev.saku.common

import com.anafthdev.saku.data.GameMode
import com.anafthdev.saku.data.model.Cell
import com.anafthdev.saku.extension.missingDigits
import com.anafthdev.saku.uicomponent.SudokuGameAction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

class GameEngine @Inject constructor(
	private val countUpTimer: CountUpTimer
) {
	
	private val unre: Unre<List<Cell>> = Unre()
	private val sudoku: Sudoku = Sudoku()
	private var listener: EngineListener? = null
	
	private val _currentBoard = MutableSharedFlow<List<Cell>>(
		replay = 1,
		onBufferOverflow = BufferOverflow.DROP_OLDEST
	)
	val currentBoard: SharedFlow<List<Cell>> = _currentBoard
	
	val second = countUpTimer.second
	
	init {
		unre.setListener(object : Unre.UnreListener<List<Cell>> {
			override fun onUndo(data: List<Cell>) {
				CoroutineScope(Dispatchers.Main).launch {
					_currentBoard.emit(data)
				}
			}
			
			override fun onRedo(data: List<Cell>) {
				CoroutineScope(Dispatchers.Main).launch {
					_currentBoard.emit(data)
				}
			}
		})
	}
	
	private fun toCellBoard(board: Array<IntArray>): List<Cell> {
		val mBoard = ArrayList<Cell>()
		
		board.forEachIndexed { col, ints ->
			val parentId = Random.nextInt()
			val subCells = ArrayList<Cell>()
			
			ints.forEachIndexed { row, num ->
				subCells.add(
					Cell(
						n = num,
						indexInBoard = row * board.size + col,
						indexInParent = row,
						parentN = col + 1,
						parentId = parentId,
						canEdit = num == 0
					)
				)
			}
			
			val cell = Cell(
				n = col + 1,
				parentId = parentId,
				subCells = subCells
			)
			
			mBoard.add(cell)
		}
		
		return mBoard
	}
	
	suspend fun init(gameMode: GameMode) {
		sudoku.init(9, gameMode.missingDigits)
		
		sudoku.printBoard().let {
			val cellBoards = toCellBoard(it)
			
			_currentBoard.emit(cellBoards)
			unre.swap(cellBoards)
		}
	}
	
	suspend fun updateBoard(cell: Cell, num: Int, action: SudokuGameAction) {
		val boardValue = currentBoard.replayCache[currentBoard.replayCache.lastIndex]
		val parentCell = boardValue.getOrNull(cell.parentN - 1)
		val parentCellIndex = cell.parentN - 1
		val cellIndex = parentCell?.subCells?.indexOfFirst { cell.id == it.id }
		
		if (cellIndex != null) {
			var updatedCell = cell
			
			when (action) {
				SudokuGameAction.Pencil -> {
					if (cell.n != -1) {
						updatedCell = updatedCell.copy(n = -1)
					}
					
					updatedCell = updatedCell.copy(
						subCells = updatedCell.subCells.toMutableList().apply {
							val subCell = find { it.n == num }
							
							if (subCell != null) {
								removeIf { it.n == num }
							} else {
								add(
									Cell(n = num)
								)
							}
						}
					)
				}
				SudokuGameAction.Undo -> {}
				SudokuGameAction.Redo -> {}
				else -> {
					updatedCell = updatedCell.copy(
						n = when {
							cell.n == 0 -> num
							cell.n != num -> num
							cell.n == num -> 0
							else -> 0
						},
						subCells = emptyList()
					)
				}
			}
			
			val newBoard = boardValue.toMutableList().apply {
				val newSubCells = get(parentCellIndex).subCells.toMutableList().apply {
					set(cellIndex, updatedCell)
				}
				
				val newParentCell = parentCell.copy(subCells = newSubCells)
				
				set(parentCellIndex, newParentCell)
			}
			
			_currentBoard.emit(newBoard.toList())
			unre.addStack(newBoard.toList())
		}
	}
	
	fun undo() {
		unre.undo()
	}
	
	fun redo() {
		unre.redo()
	}
	
	fun pause() {
		countUpTimer.cancel()
	}
	
	fun resume() {
		countUpTimer.start()
	}
	
	fun setListener(l: EngineListener) {
		listener = l
	}
	
	interface EngineListener {
	
	}
	
}
