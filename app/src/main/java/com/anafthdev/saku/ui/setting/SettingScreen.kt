package com.anafthdev.saku.ui.setting

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun SettingScreen(
	navController: NavController,
	viewModel: SettingViewModel
) {
	
	LazyColumn(
		modifier = Modifier
			.fillMaxSize()
	) {
		
	}
}
