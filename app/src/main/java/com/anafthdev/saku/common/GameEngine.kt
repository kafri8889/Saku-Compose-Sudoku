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
						parentId = parentId,
						col = col,
						row = row,
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
	
	suspend fun updateBoard(num: Int, cell: Cell) {
		val parentCell = currentBoard.value.find { cell.parentId == it.parentId }
		val parentCellIndex = currentBoard.value.indexOf(parentCell)
		val cellIndex = parentCell?.subCells?.indexOfFirst { cell.id == it.id }
		
		if (cellIndex != null) {
			_currentBoard.emit(
				currentBoard.value.toMutableList().apply {
					val newSubCells = get(parentCellIndex).subCells.toMutableList().apply {
						set(cellIndex, cell.copy(n = num))
					}
					
					set(parentCellIndex, parentCell.copy(subCells = newSubCells))
				}
			)
		}
	}
	
	fun pause() {
		countUpTimer.cancel()
	}
	
	fun resume() {
		countUpTimer.start()
	}
	
}