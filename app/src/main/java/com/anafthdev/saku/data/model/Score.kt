package com.anafthdev.saku.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.anafthdev.saku.data.Difficulty

@Entity(tableName = "score")
data class Score(
	@PrimaryKey @ColumnInfo(name = "id_score") val id: Int,
	@ColumnInfo(name = "date_score") val date: Long,
	
	/**
	 * In second
	 */
	@ColumnInfo(name = "time_score") val time: Int,
	
	/**
	 * [Difficulty] ordinal
	 */
	@ColumnInfo(name = "difficulty_score") val difficulty: Int,
)
