package com.anafthdev.saku.uicomponent

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIos
import androidx.compose.material.icons.rounded.ArrowForwardIos
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.anafthdev.saku.data.Difficulty
import timber.log.Timber

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun DifficultySelector(
	difficulties: Array<Difficulty>,
	selectedDifficulty: Difficulty,
	modifier: Modifier = Modifier,
	onGameModeChanged: (Difficulty) -> Unit
) {
	
	val density = LocalDensity.current
	val config = LocalConfiguration.current
	
	val selectedGameModeIndex = remember(selectedDifficulty, difficulties) {
		Timber.i("$selectedDifficulty -> $difficulties")
		difficulties.indexOfFirst { it == selectedDifficulty }.also {
			Timber.i("selectedDifficultyIndex: $it")
		}
	}
	
	val backButtonAlpha by animateFloatAsState(
		targetValue = if (selectedGameModeIndex <= 0) 0f else 1f,
		animationSpec = tween(500)
	)
	
	val forwardButtonAlpha by animateFloatAsState(
		targetValue = if (selectedGameModeIndex >= difficulties.lastIndex) 0f else 1f,
		animationSpec = tween(500)
	)
	
	var enterAnimOffset by remember {
		mutableStateOf(with(density) { config.screenWidthDp.dp.roundToPx() })
	}
	var exitAnimOffset by remember {
		mutableStateOf(with(density) { -config.screenWidthDp.dp.roundToPx() })
	}

	Row(
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.SpaceBetween,
		modifier = modifier
	) {
		IconButton(
			onClick = {
				if (selectedGameModeIndex > 0) {
					onGameModeChanged(difficulties[selectedGameModeIndex - 1])
					
					// Left
					enterAnimOffset = with(density) { -config.screenWidthDp.dp.roundToPx() }
					exitAnimOffset = with(density) { config.screenWidthDp.dp.roundToPx() }
				}
			},
			modifier = Modifier
				.graphicsLayer {
					alpha = backButtonAlpha
				}
		) {
			Icon(
				imageVector = Icons.Rounded.ArrowBackIos,
				contentDescription = null
			)
		}
		
		AnimatedContent(
			targetState = selectedDifficulty,
			transitionSpec = {
				slideInHorizontally(
					initialOffsetX = { enterAnimOffset },
					animationSpec = tween(600)
				) with slideOutHorizontally(
					targetOffsetX = { exitAnimOffset },
					animationSpec = tween(600)
				)
			}
		) { mode ->
			Text(
				text = mode.name,
				style = MaterialTheme.typography.titleLarge
			)
		}
		
		IconButton(
			onClick = {
				if (selectedGameModeIndex < difficulties.lastIndex) {
					onGameModeChanged(difficulties[selectedGameModeIndex + 1])
					
					// Right
					enterAnimOffset = with(density) { config.screenWidthDp.dp.roundToPx() }
					exitAnimOffset = with(density) { -config.screenWidthDp.dp.roundToPx() }
				}
			},
			modifier = Modifier
				.graphicsLayer {
					alpha = forwardButtonAlpha
				}
		) {
			Icon(
				imageVector = Icons.Rounded.ArrowForwardIos,
				contentDescription = null
			)
		}
	}
	
}

enum class DifficultySelectorDirection {
	LEFT,
	RIGHT
}
