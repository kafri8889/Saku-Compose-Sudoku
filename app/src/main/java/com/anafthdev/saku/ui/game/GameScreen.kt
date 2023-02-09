package com.anafthdev.saku.ui.game

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.anafthdev.saku.data.Cells
import com.anafthdev.saku.uicomponent.SudokuBoard

@Composable
fun GameScreen(
	viewModel: GameViewModel,
	navController: NavController
) {

	Box(
		modifier = Modifier
			.fillMaxSize()
	) {
		SudokuBoard(
			cells = Cells.bigCells,
			modifier = Modifier
				.padding(8.dp)
				.fillMaxWidth()
				.aspectRatio(1f/1f)
		)
	}
	
}
