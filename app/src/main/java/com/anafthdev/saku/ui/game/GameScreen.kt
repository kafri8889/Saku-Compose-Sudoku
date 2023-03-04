package com.anafthdev.saku.ui.game

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.anafthdev.saku.R
import com.anafthdev.saku.component.ObserveLifecycle
import com.anafthdev.saku.data.GameMode
import com.anafthdev.saku.extension.hourMinuteFormat
import com.anafthdev.saku.extension.toast
import com.anafthdev.saku.uicomponent.AnimatedTextByChar
import com.anafthdev.saku.uicomponent.GamePausedDialog
import com.anafthdev.saku.uicomponent.NumberPad
import com.anafthdev.saku.uicomponent.SudokuBoard
import com.anafthdev.saku.uicomponent.SudokuGameAction
import com.anafthdev.saku.uicomponent.SudokuGameActionDefaults

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
	viewModel: GameViewModel,
	navController: NavController
) {
	
	val context = LocalContext.current
	
	var isPaused by remember { mutableStateOf(false) }
	
	LaunchedEffect(viewModel.win) {
		if (viewModel.win) {
			"Win".toast(context)
		}
	}
	
	BackHandler {
		viewModel.exit()
		navController.popBackStack()
	}
	
	ObserveLifecycle(
		onResume = viewModel::resume,
		onPause = {
			viewModel.saveState()
			viewModel.pause()
		}
	)
	
	if (isPaused) {
		GamePausedDialog(
			onDismissRequest = {
				viewModel.resume()
				isPaused = false
			},
			text = {
				Text(stringResource(R.string.game_paused))
			},
			confirmButton = {
				Button(
					onClick = {
						viewModel.resume()
						isPaused = false
					},
					modifier = Modifier
						.fillMaxWidth()
				) {
					Text(stringResource(R.string.resume))
				}
			},
			dismissButton = {
				TextButton(
					onClick = {
						isPaused = false
						
						navController.popBackStack()
					},
					modifier = Modifier
						.fillMaxWidth()
				) {
					Text(stringResource(id = R.string.exit))
				}
			}
		)
	}
	
	GameContent(
		viewModel = viewModel,
		onPause = {
			viewModel.pause()
			isPaused = true
		}
	)
}

@Composable
fun GameContent(
	viewModel: GameViewModel,
	onPause: () -> Unit
) {
	LazyColumn(
		verticalArrangement = Arrangement.SpaceBetween,
		contentPadding = PaddingValues(16.dp),
		modifier = Modifier
			.fillMaxSize()
			.systemBarsPadding()
	) {
		item {
			GameScreenHeader(
				minute = viewModel.minute,
				second = viewModel.second,
				gameMode = viewModel.gameMode,
				onPause = onPause
			)
		}
		
		item {
			SudokuBoard(
				cells = viewModel.board,
				selectedCell = viewModel.selectedCell,
				onCellClicked = viewModel::updateBoard,
				highlightNumberEnabled = viewModel.highlightNumberEnabled,
				modifier = Modifier
					.fillMaxWidth()
					.aspectRatio(1f / 1f)
			)
		}
		
		item {
			Box(
				contentAlignment = Alignment.Center
			) {
				NumberPad(
					selectedNumber = viewModel.selectedNumber,
					remainingNumbers = viewModel.remainingNumbers,
					showRemainingNumber = viewModel.remainingNumberEnabled,
					onNumberSelected = viewModel::updateSelectedNumber,
					modifier = Modifier
						.fillMaxWidth()
				)
			}
		}
		
		item {
			Box(
				contentAlignment = Alignment.Center
			) {
				SudokuGameAction(
					selected = viewModel.selectedGameAction,
					onClick = { action ->
						if (action in SudokuGameActionDefaults.selectableActions) {
							viewModel.updateSelectedGameAction(
								if (viewModel.selectedGameAction == action) SudokuGameAction.None
								else action
							)
						} else {
							when (action) {
								SudokuGameAction.Undo -> viewModel.undo()
								SudokuGameAction.Redo -> viewModel.redo()
								SudokuGameAction.Validate -> viewModel.solve()
								else -> {}
							}
						}
					},
					modifier = Modifier
						.fillMaxWidth()
				)
			}
		}
	}
}

@Composable
private fun GameScreenHeader(
	minute: Int,
	second: Int,
	gameMode: GameMode,
	onPause: () -> Unit
) {
	
	Row {
		Column {
			AnimatedTextByChar(
				text = "${hourMinuteFormat(minute)}:${hourMinuteFormat(second)}",
				style = MaterialTheme.typography.titleLarge.copy(
					fontWeight = FontWeight.ExtraBold
				)
			)
			
			Text(
				text = gameMode.name,
				style = MaterialTheme.typography.titleLarge.copy(
					fontWeight = FontWeight.Light,
					color = Color.Gray
				)
			)
		}
		
		Spacer(modifier = Modifier.weight(1f))
		
		IconButton(onClick = onPause) {
			Icon(
				painter = painterResource(id = R.drawable.ic_pause),
				contentDescription = null
			)
		}
	}
}
