package com.anafthdev.saku.uicomponent

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun GroupPreference(
	modifier: Modifier = Modifier,
	title: @Composable (() -> Unit)? = null,
	preferences: @Composable () -> Unit
) {
	Column(
		modifier = Modifier
			.fillMaxWidth()
			.then(modifier)
	) {
		if (title != null) {
			Box(
				modifier = Modifier
					.padding(
						top = 16.dp,
						bottom = 8.dp,
						start = 12.dp,
						end = 12.dp
					)
			) {
				ProvideTextStyle(
					value = MaterialTheme.typography.titleMedium.copy(
						color = MaterialTheme.colorScheme.primary,
						fontWeight = FontWeight.Bold,
					)
				) {
					title()
				}
			}
		}
		
		preferences()
	}
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun BasicPreference(
	modifier: Modifier = Modifier,
	showValue: Boolean = false,
	enabled: Boolean = true,
	title: @Composable () -> Unit,
	value: @Composable () -> Unit = {},
	summary: @Composable () -> Unit = {},
	icon: @Composable (() -> Unit)? = null,
	style: BasicPreferenceStyle = SettingPreferenceDefaults.basicPreferenceStyle(),
	onClick: () -> Unit
) {
	
	val valueTextStyle by style.valueTextStyle(enabled = enabled)
	val titleTextStyle by style.titleTextStyle(enabled = enabled)
	val summaryTextStyle by style.summaryTextStyle(enabled = enabled)
	
	val valueTextColor by animateColorAsState(
		targetValue = valueTextStyle.color,
		animationSpec = tween(500)
	)
	
	Row(
		verticalAlignment = Alignment.CenterVertically,
		modifier = modifier
			.height(64.dp)
			.clickable { onClick() }
	) {
		Box(
			contentAlignment = Alignment.Center,
			modifier = Modifier
				.weight(
					weight = 0.12f
				)
		) {
			icon?.invoke()
		}
		
		Column(
			verticalArrangement = Arrangement.Center,
			modifier = Modifier
				.weight(
					weight = 0.68f
				)
		) {
			ProvideTextStyle(
				value = titleTextStyle
			) {
				title()
			}
			
			ProvideTextStyle(
				value = summaryTextStyle
			) {
				summary()
			}
		}
		
		Box(
			contentAlignment = Alignment.CenterEnd,
			modifier = Modifier
				.padding(end = 12.dp)
				.weight(0.2f)
		) {
			if (showValue) {
				AnimatedContent(targetState = value) {
					ProvideTextStyle(
						value = valueTextStyle
					) {
						value()
					}
				}
			}
		}
	}
}

@Composable
fun SwitchPreference(
	title: @Composable () -> Unit,
	isChecked: Boolean,
	enabled: Boolean = true,
	style: SwitchPreferenceStyle = SettingPreferenceDefaults.switchPreferencesStyle(),
	onCheckedChange: (Boolean) -> Unit,
	summary: @Composable () -> Unit = {},
	icon: @Composable (() -> Unit)? = null,
	switch: @Composable () -> Unit = {
		val checkedTrackColor by animateColorAsState(
			targetValue = MaterialTheme.colorScheme.primary,
			animationSpec = tween(500)
		)
		
		val checkedThumbColor by animateColorAsState(
			targetValue = MaterialTheme.colorScheme.onPrimary,
			animationSpec = tween(500)
		)
		
		Switch(
			checked = isChecked,
			onCheckedChange = {
				onCheckedChange(!isChecked)
			},
			colors = SwitchDefaults.colors(
				checkedTrackColor = checkedTrackColor,
				checkedThumbColor = checkedThumbColor
			)
		)
	}
) {
	
	val titleTextStyle by style.titleTextStyle(enabled = enabled)
	val summaryTextStyle by style.summaryTextStyle(enabled = enabled)
	
	Row(
		verticalAlignment = Alignment.CenterVertically,
		modifier = Modifier
			.fillMaxWidth()
			.height(64.dp)
			.clickable {
				onCheckedChange(!isChecked)
			}
	) {
		Box(
			contentAlignment = Alignment.Center,
			modifier = Modifier
				.weight(
					weight = 0.12f
				)
		) {
			icon?.invoke()
		}
		
		Column(
			verticalArrangement = Arrangement.Center,
			modifier = Modifier
				.weight(0.68f)
		) {
			ProvideTextStyle(
				value = titleTextStyle
			) {
				title()
			}
			
			ProvideTextStyle(
				value = summaryTextStyle
			) {
				summary()
			}
		}
		
		Box(
			contentAlignment = Alignment.Center,
			modifier = Modifier
				.weight(0.2f)
		) {
			switch()
		}
	}
}

object SettingPreferenceDefaults {
	
	@Composable
	fun basicPreferenceStyle(
		valueTextStyle: TextStyle = MaterialTheme.typography.titleSmall.copy(
			color = MaterialTheme.colorScheme.primary,
			fontWeight = FontWeight.Normal
		),
		titleTextStyle: TextStyle = MaterialTheme.typography.titleMedium.copy(
			fontWeight = FontWeight.Normal
		),
		summaryTextStyle: TextStyle = MaterialTheme.typography.titleSmall.copy(
			color = Color.Gray,
			fontWeight = FontWeight.Normal
		),
		disabledValueTextStyle: TextStyle = MaterialTheme.typography.titleSmall.copy(
			color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.4f),
			fontWeight = FontWeight.Normal
		),
		disabledTitleTextStyle: TextStyle = MaterialTheme.typography.titleMedium.copy(
			fontWeight = FontWeight.Normal,
			color = MaterialTheme.colorScheme.onSurface
		),
		disabledSummaryTextStyle: TextStyle = MaterialTheme.typography.titleSmall.copy(
			color = MaterialTheme.colorScheme.onSurface,
			fontWeight = FontWeight.Normal
		)
	): BasicPreferenceStyle {
		return BasicPreferenceStyle(
			valueTextStyle = valueTextStyle,
			titleTextStyle = titleTextStyle,
			summaryTextStyle = summaryTextStyle,
			disabledValueTextStyle = disabledValueTextStyle,
			disabledTitleTextStyle = disabledTitleTextStyle,
			disabledSummaryTextStyle = disabledSummaryTextStyle
		)
	}
	
	@Composable
	fun switchPreferencesStyle(
		titleTextStyle: TextStyle = MaterialTheme.typography.titleMedium.copy(
			fontWeight = FontWeight.Normal
		),
		summaryTextStyle: TextStyle = MaterialTheme.typography.titleSmall.copy(
			color = Color.Gray,
			fontWeight = FontWeight.Normal
		),
		disabledTitleTextStyle: TextStyle = MaterialTheme.typography.titleMedium.copy(
			fontWeight = FontWeight.Normal,
			color = MaterialTheme.colorScheme.onSurface
		),
		disabledSummaryTextStyle: TextStyle = MaterialTheme.typography.titleSmall.copy(
			color = MaterialTheme.colorScheme.onSurface,
			fontWeight = FontWeight.Normal
		)
	): SwitchPreferenceStyle {
		return SwitchPreferenceStyle(
			titleTextStyle = titleTextStyle,
			summaryTextStyle = summaryTextStyle,
			disabledTitleTextStyle = disabledTitleTextStyle,
			disabledSummaryTextStyle = disabledSummaryTextStyle
		)
	}
	
}

class BasicPreferenceStyle(
	private val valueTextStyle: TextStyle,
	private val titleTextStyle: TextStyle,
	private val summaryTextStyle: TextStyle,
	private val disabledValueTextStyle: TextStyle,
	private val disabledTitleTextStyle: TextStyle,
	private val disabledSummaryTextStyle: TextStyle
) {
	
	@Composable
	fun valueTextStyle(enabled: Boolean): State<TextStyle> {
		return rememberUpdatedState(
			newValue = if (enabled) valueTextStyle else disabledValueTextStyle
		)
	}
	
	@Composable
	fun titleTextStyle(enabled: Boolean): State<TextStyle> {
		return rememberUpdatedState(
			newValue = if (enabled) titleTextStyle else disabledTitleTextStyle
		)
	}
	
	@Composable
	fun summaryTextStyle(enabled: Boolean): State<TextStyle> {
		return rememberUpdatedState(
			newValue = if (enabled) summaryTextStyle else disabledSummaryTextStyle
		)
	}

}

class SwitchPreferenceStyle(
	private val titleTextStyle: TextStyle,
	private val summaryTextStyle: TextStyle,
	private val disabledTitleTextStyle: TextStyle,
	private val disabledSummaryTextStyle: TextStyle
) {
	
	@Composable
	fun titleTextStyle(enabled: Boolean): State<TextStyle> {
		return rememberUpdatedState(
			newValue = if (enabled) titleTextStyle else disabledTitleTextStyle
		)
	}
	
	@Composable
	fun summaryTextStyle(enabled: Boolean): State<TextStyle> {
		return rememberUpdatedState(
			newValue = if (enabled) summaryTextStyle else disabledSummaryTextStyle
		)
	}

}
