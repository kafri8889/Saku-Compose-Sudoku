package com.anafthdev.saku.data.local.database

import androidx.room.TypeConverter
import com.anafthdev.saku.data.Difficulty

object DatabaseTypeConverter {
	
	@TypeConverter
	fun difficultyToOrdinal(difficulty: Difficulty): Int = difficulty.ordinal
	
	@TypeConverter
	fun difficultyFromOrdinal(difficulty: Int): Difficulty = Difficulty.values()[difficulty]
	
}