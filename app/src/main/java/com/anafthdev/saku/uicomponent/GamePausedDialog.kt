package com.anafthdev.saku.uicomponent

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun GamePausedDialog(
	modifier: Modifier = Modifier,
	shape: Shape = AlertDialogDefaults.shape,
	containerColor: Color = AlertDialogDefaults.containerColor,
	textContentColor: Color = AlertDialogDefaults.textContentColor,
	tonalElevation: Dp = AlertDialogDefaults.TonalElevation,
	text: @Composable () -> Unit,
	confirmButton: @Composable () -> Unit,
	dismissButton: @Composable () -> Unit,
	onDismissRequest: () -> Unit
) {
	Dialog(onDismissRequest = onDismissRequest) {
		Surface(
			modifier = modifier,
			shape = shape,
			color = containerColor,
			tonalElevation = tonalElevation,
		) {
			Column(
				horizontalAlignment = Alignment.CenterHorizontally,
				modifier = Modifier
					.padding(24.dp)
			) {
				CompositionLocalProvider(LocalContentColor provides textContentColor) {
					ProvideTextStyle(MaterialTheme.typography.bodyLarge) {
						Box(
							Modifier
								.weight(weight = 1f, fill = false)
								.padding(24.dp)
						) {
							text()
						}
					}
				}
				
				confirmButton()
				
				dismissButton()
			}
		}
	}
}
