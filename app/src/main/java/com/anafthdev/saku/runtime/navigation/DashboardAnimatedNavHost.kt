package com.anafthdev.saku.runtime.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.anafthdev.saku.data.SakuDestination
import com.anafthdev.saku.ui.dashboard.DashboardScreen
import com.anafthdev.saku.ui.dashboard.DashboardViewModel
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.DashboardAnimatedNavHost(navController: NavController) {
	navigation(
		startDestination = SakuDestination.Dashboard.Home.route,
		route = SakuDestination.Dashboard.Root.route
	) {
		composable(
			route = SakuDestination.Dashboard.Home.route,
			enterTransition = { fadeIn() },
			exitTransition = { fadeOut() },
			popEnterTransition = { fadeIn() },
			popExitTransition = { fadeOut() }
		) { backEntry ->
			val viewModel = hiltViewModel<DashboardViewModel>(backEntry)
			
			DashboardScreen(
				navController = navController,
				viewModel = viewModel
			)
		}
	}
}

