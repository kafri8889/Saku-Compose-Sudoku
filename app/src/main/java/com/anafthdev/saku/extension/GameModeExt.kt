package com.anafthdev.saku.extension

import com.anafthdev.saku.data.GameMode

val GameMode.missingDigits: Int
	get() = when (this) {
		GameMode.Easy -> 45
		GameMode.Normal -> 50
		GameMode.Hard -> 55
	}
