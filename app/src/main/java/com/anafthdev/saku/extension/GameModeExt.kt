package com.anafthdev.saku.extension

import com.anafthdev.saku.data.GameMode

val GameMode.missingDigits: Int
	get() = when (this) {
		GameMode.Fast -> 24
		GameMode.Easy -> 32
		GameMode.Normal -> 40
		GameMode.Hard -> 48
		GameMode.Evil -> 56
	}
