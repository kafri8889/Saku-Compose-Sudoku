package com.anafthdev.saku.ui.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.anafthdev.saku.component.ObserveLifecycle
import com.anafthdev.saku.data.GameMode
import com.anafthdev.saku.extension.hourMinuteFormat
import com.anafthdev.saku.extension.toast
import com.anafthdev.saku.uicomponent.AnimatedTextByChar
import com.anafthdev.saku.uicomponent.NumberPad
import com.anafthdev.saku.uicomponent.SudokuBoard
import com.anafthdev.saku.uicomponent.SudokuGameAction
import com.anafthdev.saku.uicomponent.SudokuGameActionDefaults

@Composable
fun GameScreen(
	viewModel: GameViewModel,
	navController: NavController
) {
	
	val context = LocalContext.current
	
	LaunchedEffect(viewModel.win) {
		if (viewModel.win) {
			"Win".toast(context)
		}
	}
	
	ObserveLifecycle(
		onResume = viewModel::resume,
		onPause = viewModel::pause
	)

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
				gameMode = viewModel.gameMode
			)
		}
		
		item {
			SudokuBoard(
				cells = viewModel.board,
				selectedCell = viewModel.selectedCell,
				onCellClicked = viewModel::updateBoard,
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
	
//	Column(
//		verticalArrangement = Arrangement.SpaceBetween,
//		modifier = Modifier
//			.verticalScroll(rememberScrollState())
//			.padding(8.dp)
//			.fillMaxSize()
//			.systemBarsPadding()
//	) {
//		GameScreenHeader(
//			minute = viewModel.minute,
//			second = viewModel.second,
//			gameMode = viewModel.gameMode
//		)
//
//		SudokuBoard(
//			cells = Cells.bigCells,
//			modifier = Modifier
//				.fillMaxWidth()
//				.aspectRatio(1f / 1f)
//		)
//
//		NumberPad(
//			selectedNumber = viewModel.selectedNumber,
//			onNumberSelected = viewModel::updateSelectedNumber,
//			modifier = Modifier
//				.fillMaxWidth()
//				.align(Alignment.CenterHorizontally)
//		)
//
//		SudokuGameAction(
//			selected = viewModel.selectedGameAction,
//			onClick = { action ->
//				if (action in SudokuGameActionDefaults.selectableActions) {
//					viewModel.updateSelectedGameAction(
//						if (viewModel.selectedGameAction == action) SudokuGameAction.None
//						else action
//					)
//				} else {
//					when (action) {
//						SudokuGameAction.Undo -> {}
//						SudokuGameAction.Validate -> {}
//						else -> {}
//					}
//				}
//			},
//			modifier = Modifier
//				.fillMaxWidth()
//				.align(Alignment.CenterHorizontally)
//		)
//
//		Spacer(modifier = Modifier.height(8.dp))
//	}
	
}

@Composable
fun GameScreenHeader(
	minute: Int,
	second: Int,
	gameMode: GameMode
) {
	
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
}
