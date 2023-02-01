package com.anafthdev.saku.runtime.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.anafthdev.saku.data.SakuDestination
import com.anafthdev.saku.ui.score.ScoreScreen
import com.anafthdev.saku.ui.score.ScoreViewModel
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.ScoreAnimatedNavHost(navController: NavController) {
	navigation(
		startDestination = SakuDestination.Score.Home.route,
		route = SakuDestination.Score.Root.route
	) {
		composable(
			route = SakuDestination.Score.Home.route,
			enterTransition = { fadeIn() },
			exitTransition = { fadeOut() },
			popEnterTransition = { fadeIn() },
			popExitTransition = { fadeOut() }
		) { backEntry ->
			val viewModel = hiltViewModel<ScoreViewModel>(backEntry)
			
			ScoreScreen(
				navController = navController,
				viewModel = viewModel
			)
		}
	}
}
