package com.anafthdev.saku.uicomponent

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.anafthdev.saku.data.Cells
import com.anafthdev.saku.data.model.Cell
import com.anafthdev.saku.extension.to2DArray
import com.anafthdev.saku.theme.SakuTheme

@Preview
@Composable
fun SudokuBoardPreview() {
	
	SakuTheme {
		SudokuBoard(
			cells = Cells.bigCells,
			modifier = Modifier
				.size(512.dp)
		)
	}
}

@Composable
fun SudokuBoard(
	cells: List<Cell>,
	modifier: Modifier = Modifier
) {
	
	BoxWithConstraints(
		modifier = modifier
	) {
		val dividerWidth = 8.dp
		
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
					modifier = Modifier
						.offset(
							x = (cellSize * col) + (endPadding * col),
							y = (cellSize * row) + (bottomPadding * row)
						)
						.size(cellSize)
				) {
					SmallBoard(cells = cellRow.subCells)
				}
			}
		}
	}
}
