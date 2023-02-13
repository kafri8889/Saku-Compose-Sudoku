package com.anafthdev.saku.uicomponent

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.anafthdev.saku.data.Cells
import com.anafthdev.saku.data.model.Cell
import com.anafthdev.saku.extension.ceil
import com.anafthdev.saku.extension.to2DArray
import com.anafthdev.saku.theme.SakuTheme
import timber.log.Timber

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

/**
 * Big cells -> Small cells -> Extra small cells (pencil mode)
 */
@Composable
fun SudokuBoard(
	cells: List<Cell>,
	modifier: Modifier = Modifier
) {
	
	val cells2D = remember(cells) {
		Timber.i("kontol: $cells")
		cells.to2DArray(3)
	}
	
	Box(
		modifier = Modifier
			.border(
				width = 2.dp,
				color = MaterialTheme.colorScheme.onBackground,
				shape = MaterialTheme.shapes.small
			)
	) {
		BoxWithConstraints(
			modifier = modifier
				.padding(2.dp)
				.clip(MaterialTheme.shapes.small)
		) {
			val dividerWidth = 2.dp
			
			val cellSize = remember(maxWidth, dividerWidth) {
				(maxWidth / 3) - dividerWidth / 1.5f  // divided by 1.5 to fill the remaining space of the padding
			}
			
			for ((col, cellsCol) in cells2D.withIndex()) {
				for ((row, cellRow) in cellsCol.withIndex()) {
					val endPadding = remember {
						when {
							row != 0 -> dividerWidth
							else -> 0.dp
						}
					}
					
					val bottomPadding = remember {
						when {
							col != 0 -> dividerWidth
							else -> 0.dp
						}
					}
					
					Box(
						modifier = Modifier
							.offset(
								x = ((cellSize * row) + (endPadding * row)).ceil(),
								y = ((cellSize * col) + (bottomPadding * col)).ceil()
							)
							.size(cellSize)
					) {
						SmallBoard(cells = cellRow.subCells)
					}
				}
			}
			
			// Divider
			for (i in 0 until 2) {
				val padding = remember {
					when {
						i != 0 -> dividerWidth
						else -> 0.dp
					}
				}
				
				SudokuBoardDivider(
					color = MaterialTheme.colorScheme.onBackground,
					maxHeight = maxHeight,
					thickness = dividerWidth,
					offsetHorz = {
						IntOffset(
							x = 0.dp
								.toPx()
								.toInt(),
							y = (cellSize * (i + 1) + padding)
								.toPx()
								.ceil()
								.toInt()
						)
					},
					offsetVert = {
						IntOffset(
							x = (cellSize * (i + 1) + padding)
								.toPx()
								.ceil()
								.toInt(),
							y = 0.dp
								.toPx()
								.toInt()
						)
					}
				)
			}
		}
	}
}

//class SudokuBoardState() {
//
//}
//
//@Composable
//fun rememberSudokuBoardState(): SudokuBoardState {
//	return remember {
//		SudokuBoardState()
//	}
//}
