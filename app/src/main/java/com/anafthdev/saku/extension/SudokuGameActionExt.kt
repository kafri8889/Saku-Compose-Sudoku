package com.anafthdev.saku.extension

import com.anafthdev.saku.R
import com.anafthdev.saku.uicomponent.SudokuGameAction

val SudokuGameAction.iconId: Int
	get() = when (this) {
		SudokuGameAction.Undo -> R.drawable.ic_undo
		SudokuGameAction.Validate -> R.drawable.ic_check
		SudokuGameAction.Pencil -> R.drawable.ic_edit
		SudokuGameAction.Redo -> R.drawable.ic_redo
		SudokuGameAction.None -> -1
	}

fun iconIdToSudokuGameAction(iconId: Int): SudokuGameAction {
	return when (iconId) {
		R.drawable.ic_undo -> SudokuGameAction.Undo
		R.drawable.ic_check -> SudokuGameAction.Validate
		R.drawable.ic_edit -> SudokuGameAction.Pencil
		R.drawable.ic_redo -> SudokuGameAction.Redo
		else -> SudokuGameAction.None
	}
}
