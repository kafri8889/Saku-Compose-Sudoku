package com.anafthdev.saku.runtime.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.anafthdev.saku.data.SakuDestination
import com.anafthdev.saku.ui.game.GameScreen
import com.anafthdev.saku.ui.game.GameViewModel
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.GameAnimatedNavHost(navController: NavController) {
	navigation(
		startDestination = SakuDestination.Game.Home.route,
		route = SakuDestination.Game.Root.route
	) {
		composable(
			route = SakuDestination.Game.Home.route,
			arguments = SakuDestination.Game.Home.arguments,
			enterTransition = { fadeIn() },
			exitTransition = { fadeOut() },
			popEnterTransition = { fadeIn() },
			popExitTransition = { fadeOut() }
		) { backEntry ->
			val viewModel = hiltViewModel<GameViewModel>(backEntry)
			
			GameScreen(
				navController = navController,
				viewModel = viewModel
			)
		}
	}
}
