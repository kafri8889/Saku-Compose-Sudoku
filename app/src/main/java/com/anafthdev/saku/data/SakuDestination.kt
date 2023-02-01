package com.anafthdev.saku.data

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class SakuDestination(val route: String) {
	
	class Dashboard {
		object Root: SakuDestination("dashboard/root")
		object Home: SakuDestination("dashboard/home")
	}
	
	class Score {
		object Root: SakuDestination("score/root")
		object Home: SakuDestination("score/home")
	}
	
	class Game {
		object Root: SakuDestination("game/root")
		object Home: SakuDestination(
			route = "game/home?" +
					"game-mode={$ARG_GAME_MODE}"
		) {
			fun createRoute(mode: GameMode): String {
				return "game/home?" +
						"game-mode=${mode.ordinal}"
			}
			
			val arguments = listOf(
				navArgument(ARG_GAME_MODE) {
					type = NavType.IntType
					defaultValue = -1
				}
			)
		}
	}
	
}

const val ARG_GAME_MODE = "game_mode"
