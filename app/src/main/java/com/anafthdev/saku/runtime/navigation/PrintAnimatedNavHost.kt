package com.anafthdev.saku.runtime.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.anafthdev.saku.data.SakuDestination
import com.anafthdev.saku.ui.print.PrintScreen
import com.anafthdev.saku.ui.print.PrintViewModel
import com.google.accompanist.navigation.animation.navigation
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.bottomSheet

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialNavigationApi::class)
fun NavGraphBuilder.PrintAnimatedNavHost(navController: NavController) {
	navigation(
		startDestination = SakuDestination.Sheet.Print.Home.route,
		route = SakuDestination.Sheet.Print.Root.route
	) {
		bottomSheet(
			route = SakuDestination.Sheet.Print.Home.route,
			arguments = SakuDestination.Sheet.Print.Home.arguments
		) { backEntry ->
			val viewModel = hiltViewModel<PrintViewModel>(backEntry)
			
			PrintScreen(
				navController = navController,
				viewModel = viewModel
			)
		}
	}
}
