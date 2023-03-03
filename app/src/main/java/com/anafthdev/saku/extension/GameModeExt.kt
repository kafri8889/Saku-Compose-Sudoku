package com.anafthdev.saku.extension

import com.anafthdev.saku.data.GameMode

val GameMode.missingDigits: Int
	get() = when (this) {
		GameMode.Easy -> 36
		GameMode.Normal -> 44
		GameMode.Hard -> 52
		GameMode.Evil -> 64
	}
