package com.anafthdev.saku.runtime.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.anafthdev.saku.data.SakuDestination
import com.anafthdev.saku.ui.setting.SettingScreen
import com.anafthdev.saku.ui.setting.SettingViewModel
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.SettingAnimatedNavHost(navController: NavController) {
	navigation(
		startDestination = SakuDestination.Setting.Home.route,
		route = SakuDestination.Setting.Root.route
	) {
		composable(
			route = SakuDestination.Setting.Home.route,
			enterTransition = { fadeIn() },
			exitTransition = { fadeOut() },
			popEnterTransition = { fadeIn() },
			popExitTransition = { fadeOut() }
		) { backEntry ->
			val viewModel = hiltViewModel<SettingViewModel>(backEntry)
			
			SettingScreen(
				navController = navController,
				viewModel = viewModel
			)
		}
	}
}
