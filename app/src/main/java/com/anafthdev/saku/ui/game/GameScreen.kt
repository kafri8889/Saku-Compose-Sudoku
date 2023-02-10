package com.anafthdev.saku.ui.game

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.anafthdev.saku.component.ObserveLifecycle
import com.anafthdev.saku.data.Cells
import com.anafthdev.saku.extension.hourMinuteFormat
import com.anafthdev.saku.uicomponent.AnimatedTextByChar
import com.anafthdev.saku.uicomponent.NumberPad
import com.anafthdev.saku.uicomponent.SudokuBoard

@Composable
fun GameScreen(
	viewModel: GameViewModel,
	navController: NavController
) {
	
	ObserveLifecycle(
		onResume = viewModel::resume,
		onPause = viewModel::pause
	)

	Column(
		modifier = Modifier
			.verticalScroll(rememberScrollState())
			.padding(8.dp)
			.fillMaxSize()
			.systemBarsPadding()
	) {
		AnimatedTextByChar(
			text = "${hourMinuteFormat(viewModel.minute)}:${hourMinuteFormat(viewModel.second)}",
			style = MaterialTheme.typography.titleLarge.copy(
				fontWeight = FontWeight.ExtraBold
			)
		)
		
		Text(
			text = viewModel.gameMode.name,
			style = MaterialTheme.typography.titleLarge.copy(
				fontWeight = FontWeight.Light,
				color = Color.Gray
			)
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
