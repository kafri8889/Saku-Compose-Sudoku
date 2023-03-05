package com.anafthdev.saku.extension

import com.anafthdev.saku.data.Difficulty

val Difficulty.missingDigits: Int
	get() = when (this) {
		Difficulty.Fast -> 24
		Difficulty.Easy -> 32
		Difficulty.Normal -> 40
		Difficulty.Hard -> 48
		Difficulty.Evil -> 56
	}
