package com.anafthdev.saku.common

import com.anafthdev.saku.data.GameMode
import com.anafthdev.saku.data.model.Cell
import com.anafthdev.saku.extension.missingDigits
import javax.inject.Inject

class GameEngine @Inject constructor(
	private val countUpTimer: CountUpTimer
) {
	
	private val sudoku: Sudoku = Sudoku()
	private var listener: EngineListener? = null
	private var currentBoard: Array<IntArray> = emptyArray()
	
	val second = countUpTimer.second
	
	private fun toCellBoard(board: Array<IntArray>): List<Cell> {
		val mBoard = ArrayList<Cell>()
		
		board.forEachIndexed { i, ints ->
			val subCells = ArrayList<Cell>()
			
			ints.forEach { num ->
				subCells.add(
					Cell(
						id = num
					)
				)
			}
			
			val cell = Cell(
				id = i + 1,
				subCells = subCells
			)
			
			mBoard.add(cell)
		}
		
		return mBoard
	}
	
	fun init(gameMode: GameMode) {
		sudoku.init(9, gameMode.missingDigits)
		
		sudoku.printBoard().let {
			currentBoard = it
			listener?.onInitialized(toCellBoard(it))
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
		fun onInitialized(board: List<Cell>)
	}
	
}