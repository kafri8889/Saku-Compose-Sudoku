package com.anafthdev.saku.ui.setting

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.anafthdev.saku.R
import com.anafthdev.saku.uicomponent.SwitchPreference

@Composable
fun SettingScreen(
	navController: NavController,
	viewModel: SettingViewModel
) {
	
	LazyColumn(
		modifier = Modifier
			.fillMaxSize()
			.systemBarsPadding()
	) {
		item {
			SwitchPreference(
				isChecked = viewModel.highlightNumberEnabled,
				onCheckedChange = viewModel::updateHighlightNumberEnabled,
				title = {
					Text(stringResource(id = R.string.highlight_same_numbers))
				},
				summary = {
					Text(stringResource(id = R.string.highlight_same_numbers_msg))
				},
				icon = {
					Icon(
						painter = painterResource(id = R.drawable.ic_highlight_same_numbers),
						contentDescription = null
					)
				}
			)
			
			SwitchPreference(
				isChecked = viewModel.remainingNumberEnabled,
				onCheckedChange = viewModel::updateRemainingNumberEnabled,
				title = {
					Text(stringResource(id = R.string.remaining_numbers))
				},
				summary = {
					Text(stringResource(id = R.string.remaining_numbers_msg))
				},
				icon = {
					Icon(
						painter = painterResource(id = R.drawable.ic_remaining_numbers),
						contentDescription = null
					)
				}
			)
		}
	}
}
