package com.anafthdev.saku.runtime.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.anafthdev.saku.data.SakuDestination
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialNavigationApi::class)
@Composable
fun SakuNavigation() {
	
	val bottomSheetNavigator = rememberBottomSheetNavigator()
	
	val navController = rememberAnimatedNavController(bottomSheetNavigator)
	val systemUiController = rememberSystemUiController()
	
	SideEffect {
		systemUiController.setSystemBarsColor(
			color = Color.Transparent,
			darkIcons = true
		)
	}
	
	ModalBottomSheetLayout(
		sheetShape = MaterialTheme.shapes.large,
		bottomSheetNavigator = bottomSheetNavigator,
		sheetBackgroundColor = MaterialTheme.colorScheme.surface,
		sheetContentColor = MaterialTheme.colorScheme.contentColorFor(MaterialTheme.colorScheme.surface)
	) {
		AnimatedNavHost(
			navController = navController,
			startDestination = SakuDestination.Dashboard.Root.route,
			modifier = Modifier
				.fillMaxSize()
		) {
			DashboardAnimatedNavHost(navController)
			
			ScoreAnimatedNavHost(navController)
			
			GameAnimatedNavHost(navController)
			
			SettingAnimatedNavHost(navController)
			
			PrintAnimatedNavHost(navController)
		}
	}
	
}
