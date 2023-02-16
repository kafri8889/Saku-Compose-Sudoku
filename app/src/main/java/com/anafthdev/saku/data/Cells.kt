package com.anafthdev.saku.data

import com.anafthdev.saku.data.model.Cell

object Cells {
	
	val smallCells = listOf(
		Cell(1),
		Cell(-1, listOf(Cell(3), Cell(8), Cell(1))),
		Cell(3),
		Cell(4),
		Cell(5),
		Cell(-1, listOf(Cell(3), Cell(8), Cell(1), Cell(9), Cell(5))),
		Cell(7),
		Cell(-1, listOf(Cell(1), Cell(2), Cell(3), Cell(4), Cell(5), Cell(6), Cell(7), Cell(8), Cell(9))),
		Cell(-1, listOf(Cell(1))),
	)
	
	val bigCells = listOf(
		Cell(
			n = 1,
			subCells = smallCells
		),
		Cell(
			n = 2,
			subCells = smallCells
		),
		Cell(
			n = 3,
			subCells = smallCells
		),
		Cell(
			n = 4,
			subCells = smallCells
		),
		Cell(
			n = 5,
			subCells = smallCells
		),
		Cell(
			n = 6,
			subCells = smallCells
		),
		Cell(
			n = 7,
			subCells = smallCells
		),
		Cell(
			n = 8,
			subCells = smallCells
		),
		Cell(
			n = 9,
			subCells = smallCells
		),
	)
	
}