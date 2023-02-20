package com.anafthdev.saku.data.model

import kotlin.random.Random

data class Cell(
	val n: Int,
	val subCells: List<Cell> = emptyList(),
	
	/**
	 * 1 - 9
	 */
	val parentN: Int = 1,
	val id: Int = Random.nextInt(),
	val parentId: Int = Random.nextInt(),
	val canEdit: Boolean = false
) {
	companion object {
		val NULL = Cell(
			n = -1
		)
	}
}
