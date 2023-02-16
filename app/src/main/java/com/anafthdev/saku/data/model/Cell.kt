package com.anafthdev.saku.data.model

import kotlin.random.Random

data class Cell(
	val n: Int,
	val subCells: List<Cell> = emptyList(),
	val id: Int = Random.nextInt(),
	val parentId: Int = Random.nextInt(),
	val col: Int = -1,
	val row: Int = -1,
	val canEdit: Boolean = false
)
