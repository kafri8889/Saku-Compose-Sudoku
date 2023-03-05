package com.anafthdev.saku.extension

import com.anafthdev.saku.R
import com.anafthdev.saku.data.Difficulty

val Difficulty.missingDigits: Int
	get() = when (this) {
		Difficulty.Fast -> 24
		Difficulty.Easy -> 32
		Difficulty.Normal -> 40
		Difficulty.Hard -> 48
		Difficulty.Evil -> 56
	}

val Difficulty.icon: Int
	get() = when (this) {
		Difficulty.Fast -> R.drawable.ic_difficulty_fast
		Difficulty.Easy -> R.drawable.ic_difficulty_easy
		Difficulty.Normal -> R.drawable.ic_difficulty_normal
		Difficulty.Hard -> R.drawable.ic_difficulty_hard
		Difficulty.Evil -> R.drawable.ic_difficulty_evil
	}
