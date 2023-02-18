package com.anafthdev.saku.common

import com.anafthdev.saku.data.GameMode
import com.anafthdev.saku.data.model.Cell
import com.anafthdev.saku.extension.missingDigits
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import kotlin.random.Random

class GameEngine @Inject constructor(
	private val countUpTimer: CountUpTimer
) {
	
	private val sudoku: Sudoku = Sudoku()
	private var listener: EngineListener? = null
	
	private val _currentBoard = MutableStateFlow(emptyList<Cell>())
	val currentBoard: StateFlow<List<Cell>> = _currentBoard
	
	val second = countUpTimer.second
	
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
			_currentBoard.emit(toCellBoard(it))
		}
	}
	
	suspend fun updateBoard(cell: Cell) {
		val parentCell = currentBoard.value[cell.parentN - 1]
		val parentCellIndex = cell.parentN - 1
		val cellIndex = parentCell?.subCells?.indexOfFirst { cell.id == it.id }
		
		if (cellIndex != null) {
			val newBoard = currentBoard.value.toMutableList().apply {
				val newSubCells = get(parentCellIndex).subCells.toMutableList().apply {
					set(cellIndex, cell)
				}
				
				set(parentCellIndex, parentCell.copy(subCells = newSubCells))
			}
			
			_currentBoard.emit(newBoard)
		}
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