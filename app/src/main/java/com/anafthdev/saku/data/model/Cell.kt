package com.anafthdev.saku.data.model

data class Cell(
	val id: Int,
	val subCells: List<Cell> = emptyList()
)
