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
	
	class Setting {
		object Root: SakuDestination("option/root")
		object Home: SakuDestination("option/home")
	}
	
	class Game {
		object Root: SakuDestination("game/root")
		object Home: SakuDestination(
			route = "game/home?" +
					"$ARG_GAME_MODE={$ARG_GAME_MODE}&" +
					"$ARG_USE_LAST_BOARD={$ARG_USE_LAST_BOARD}"
		) {
			fun createRoute(
				mode: Int,
				useLastBoard: Boolean = false
			): String {
				return "game/home?" +
						"$ARG_GAME_MODE=$mode&" +
						"$ARG_USE_LAST_BOARD=$useLastBoard"
			}
			
			val arguments = listOf(
				navArgument(ARG_GAME_MODE) {
					type = NavType.IntType
					defaultValue = -1
				},
				navArgument(ARG_USE_LAST_BOARD) {
					type = NavType.BoolType
					defaultValue = false
				}
			)
		}
	}
	
}

const val ARG_GAME_MODE = "game_mode"
const val ARG_USE_LAST_BOARD = "use_last_board"
