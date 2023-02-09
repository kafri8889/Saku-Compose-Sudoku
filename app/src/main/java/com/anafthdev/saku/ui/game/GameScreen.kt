package com.anafthdev.saku.ui.game

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.anafthdev.saku.data.Cells
import com.anafthdev.saku.data.GameMode
import com.anafthdev.saku.extension.hourMinuteFormat
import com.anafthdev.saku.uicomponent.AnimatedTextByChar
import com.anafthdev.saku.uicomponent.NumberPad
import com.anafthdev.saku.uicomponent.SudokuBoard

@Composable
fun GameScreen(
	viewModel: GameViewModel,
	navController: NavController
) {

	Column(
		modifier = Modifier
			.padding(8.dp)
			.fillMaxSize()
			.systemBarsPadding()
	) {
		GameScreenHeader(
			second = viewModel.second,
			minute = viewModel.minute,
			gameMode = viewModel.gameMode
		)
		
		Spacer(modifier = Modifier.height(16.dp))
		
		SudokuBoard(
			cells = Cells.bigCells,
			modifier = Modifier
				.fillMaxWidth()
				.aspectRatio(1f / 1f)
		)
		
		Spacer(modifier = Modifier.height(16.dp))
		
		NumberPad(
			selectedNumber = viewModel.selectedNumber,
			onNumberSelected = viewModel::updateSelectedNumber
		)
	}
	
}

@Composable
fun GameScreenHeader(
	second: Int,
	minute: Int,
	gameMode: GameMode,
	modifier: Modifier = Modifier
) {
	Column(modifier = modifier) {
		AnimatedTextByChar(
			text = "${hourMinuteFormat(second)}:${hourMinuteFormat(minute)}",
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
