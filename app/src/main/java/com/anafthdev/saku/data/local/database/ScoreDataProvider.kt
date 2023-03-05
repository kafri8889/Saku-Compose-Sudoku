package com.anafthdev.saku.data.local.database

import com.anafthdev.saku.data.Difficulty
import com.anafthdev.saku.data.model.Score

object ScoreDataProvider {
	
	val scores = listOf(
		Score(
			id = 0,
			date = System.currentTimeMillis(),
			time = 3630,
			difficulty = Difficulty.Easy
		),
		Score(
			id = 1,
			date = System.currentTimeMillis(),
			time = 6,
			difficulty = Difficulty.Evil
		),
		Score(
			id = 2,
			date = System.currentTimeMillis(),
			time = 85,
			difficulty = Difficulty.Normal
		),
	)
	
}