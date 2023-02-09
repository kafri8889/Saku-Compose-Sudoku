package com.anafthdev.saku.common

import kotlin.math.floor
import kotlin.math.sqrt

class Sudoku {
	
	var board: Array<IntArray> = arrayOf()
	
	/**
	 * number of columns/rows.
	 */
	private var numRowOrColumn: Int = 0
	
	/**
	 * square root of [numRowOrColumn]
	 */
	private var srNumRowOrColumn: Int = 0
	
	/**
	 * number of missing digits
	 */
	private var numMissingDigits: Int = 0
	
	fun init(n: Int, k: Int) {
		numRowOrColumn = n
		numMissingDigits = k
		
		// Compute square root of numRowOrColumn
		srNumRowOrColumn = sqrt(numRowOrColumn.toDouble()).toInt()
		
		board = Array(n) { IntArray(9) }
	}
	
	// check in the row for existence
	private fun unUsedInCol(j: Int, num: Int): Boolean {
		for (i in 0 until numRowOrColumn) {
			if (board[i][j] == num) {
				return false
			}
		}
		
		return true
	}
	
	// check in the row for existence
	private fun unUsedInRow(i: Int, num: Int): Boolean {
		for (j in 0 until numRowOrColumn) {
			if (board[i][j] == num) {
				return false
			}
		}
		
		return true
	}
	
	// Returns false if given 3 x 3 block contains num.
	private fun unUsedInBox(rowStart: Int, colStart: Int, num: Int): Boolean {
		for (i in 0 until srNumRowOrColumn) {
			for (j in 0 until srNumRowOrColumn) {
				if (board[rowStart + i][colStart + j] == num) {
					return false
				}
			}
		}
		
		return true
	}
	
	private fun checkIfSafe(i: Int, j: Int, num: Int): Boolean {
		val rowStart = i - i % srNumRowOrColumn
		val colStart = j - j % srNumRowOrColumn
		
		return  unUsedInRow(i, num) &&
				unUsedInCol(j, num) &&
				unUsedInBox(rowStart, colStart, num)
	}
	
	// Sudoku Generator
	private fun fillValues() {
		// Fill the diagonal with SRN x SRN matrices
		fillDiagonal()
		
		// Fill remaining blocks
		fillRemaining(0, srNumRowOrColumn)
		
		// Remove Randomly K digits to make game
		removeKDigits()
	}
	
	private fun fillBox(row: Int, col: Int) {
		var num = 0
		
		for (i in 0 until srNumRowOrColumn) {
			for (j in 0 until srNumRowOrColumn) {
				do {
					num = floor(Math.random() * numRowOrColumn + 1).toInt()
				} while (!unUsedInBox(row, col, num))
				
				board[col + i][row + j] = num
			}
		}
	}
	
	// Fill the diagonal srNumRowOrColumn number of srNumRowOrColumn x srNumRowOrColumn matrices
	private fun fillDiagonal() {
		var i = 0
		
		while (i < numRowOrColumn) {
			
			// for diagonal box, start coordinates->i==j
			fillBox(i, i)
			
			i += srNumRowOrColumn
		}
	}
	
	// A recursive function to fill remaining
	// matrix
	fun fillRemaining(i: Int, j: Int): Boolean {
		
		var mI = i
		var mJ = j
		
		if (mJ >= numRowOrColumn && mI < numRowOrColumn - 1) {
			mI += 1
			mJ = 0
		}
		
		if (mI >= numRowOrColumn && mJ >= numRowOrColumn) {
			return true
		}
		
		when {
			mI < srNumRowOrColumn -> {
				if (mJ < srNumRowOrColumn) {
					mJ = srNumRowOrColumn
				}
			}
			mI < numRowOrColumn - srNumRowOrColumn -> {
				if (mJ == mI / srNumRowOrColumn * srNumRowOrColumn) {
					mJ += srNumRowOrColumn
				}
			}
			else -> {
				if (mJ == numRowOrColumn - srNumRowOrColumn) {
					mI += 1
					mJ = 0
					
					if (mI >= numRowOrColumn) {
						return true
					}
				}
			}
		}
		
		for (num in 1..numRowOrColumn) {
			if (checkIfSafe(mI, mJ, num)) {
				board[mI][mJ] = num
				
				if (fillRemaining(mI, mJ + 1)) {
					return true
				}
				
				board[mI][mJ] = 0
			}
		}
		
		return false
	}
	
	// Remove the K no. of digits to
	// complete game
	fun removeKDigits() {
		var count: Int = numMissingDigits
		
		while (count != 0) {
			val cellId: Int = floor(Math.random() * (numRowOrColumn * numRowOrColumn) + 1).toInt() - 1
			
			// extract coordinates i and j
			val i: Int = cellId / numRowOrColumn
			var j: Int = cellId % 9
			
			if (j != 0) {
				j -= 1
			}
			
			if (board[i][j] != 0) {
				count--
				
				board[i][j] = 0
			}
		}
	}
	
	fun printBoard(): Array<IntArray> {
		fillValues()
		
		for (i in 0 until numRowOrColumn) {
			for (j in 0 until numRowOrColumn) {
				print(board[i][j].toString() + " ")
				
				return board
			}
			
			println()
		}
		
		return arrayOf()
	}
}
