package com.anafthdev.saku.uicomponent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.anafthdev.saku.data.Cells
import com.anafthdev.saku.data.model.Cell
import com.anafthdev.saku.extension.to2DArray
import com.anafthdev.saku.theme.SakuTheme

@Preview
@Composable
fun SmallBoardPreview() {
	SakuTheme {
		SmallBoard(
			cells = Cells.smallCells,
			modifier = Modifier
				.size(128.dp)
		)
	}
}

@Composable
fun SmallBoard(
	cells: List<Cell>,
	modifier: Modifier = Modifier
) {

	BoxWithConstraints(
		modifier = modifier
	) {
		val dividerWidth = 2.dp
		
		val cellSize = remember(maxWidth, dividerWidth) {
			(maxWidth / 3) - dividerWidth / 1.5f  // divided by 1.5 to fill the remaining space of the padding
		}
		
		val cells2D = remember(cells) {
			cells.to2DArray(3)
		}
		
		for ((col, cellsCol) in cells2D.withIndex()) {
			for ((row, cellRow) in cellsCol.withIndex()) {
				val endPadding = remember {
					when {
						col != 0 -> dividerWidth
						else -> 0.dp
					}
				}
				
				val bottomPadding = remember {
					when {
						row != 0 -> dividerWidth
						else -> 0.dp
					}
				}
				
				Box(
					contentAlignment = Alignment.Center,
					modifier = Modifier
						.offset(
							x = (cellSize * col) + (endPadding * col),
							y = (cellSize * row) + (bottomPadding * row)
						)
						.size(cellSize)
						.background(Color.Red)
				) {
					Text("$col, $row")
				}
			}
		}
	}
}
