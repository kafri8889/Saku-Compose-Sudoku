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
	
	private val solvedBoard: ArrayList<Cell> = arrayListOf()
	
	private val _currentBoard = MutableSharedFlow<List<Cell>>(
		replay = 1,
		onBufferOverflow = BufferOverflow.DROP_OLDEST
	)
	val currentBoard: SharedFlow<List<Cell>> = _currentBoard
	
	val second = countUpTimer.second
	
	init {
		sudoku.setListener(object : Sudoku.SudokuListener {
			override fun onSolvedBoardCreated(board: Array<IntArray>) {
				solvedBoard.apply {
					clear()
					addAll(toCellBoard(board))
				}
			}
		})
		
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
		
		val dCells1 = arrayListOf<List<Int>>()
		val dCells2 = arrayListOf<List<Int>>()
		val dCells3 = arrayListOf<List<Int>>()
		
		for (i in 0 until 9) {
			val cellList = board[i].toList()
			
			dCells1.add(cellList.subList(0, 3))
			dCells2.add(cellList.subList(3, 6))
			dCells3.add(cellList.subList(6, 9))
		}
		
		for (i in 0 until 9) {
			val parentId = Random.nextInt()
			val subCells = ArrayList<Cell>()
			
			val (from, to) = when (i) {
				in 0 until 3 -> 0 to 3
				in 3 until 6 -> 3 to 6
				in 6 until 9 -> 6 to 9
				else -> -1 to -1
			}
			
			val dCellList = when {
				i % 3 == 0 -> dCells1.subList(from, to)
				i % 3 == 1 -> dCells2.subList(from, to)
				i % 3 == 2 -> dCells3.subList(from, to)
				else -> emptyList()
			}
			
			dCellList.flatten().forEach{ int ->
				subCells.add(
					Cell(
						n = int,
						parentN = i + 1,
						parentId = parentId,
						canEdit = int == 0
					)
				)
			}
			
			val cell = Cell(
				n = i + 1,
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
	
	suspend fun solve() {
		_currentBoard.emit(solvedBoard)
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
