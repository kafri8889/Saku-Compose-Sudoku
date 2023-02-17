package com.anafthdev.saku.uicomponent

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anafthdev.saku.data.Cells
import com.anafthdev.saku.data.model.Cell
import com.anafthdev.saku.extension.ceil
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SmallBoard(
	cells: List<Cell>,
	modifier: Modifier = Modifier,
	onCellClicked: (Cell) -> Unit = {}
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
					contentAlignment = Alignment.Center,
					modifier = Modifier
						.offset(
							x = ((cellSize * row) + (endPadding * row)).ceil(),
							y = ((cellSize * col) + (bottomPadding * col)).ceil()
						)
						.size(cellSize)
						.clickable(
							enabled = cellRow.canEdit,
							onClick = {
								onCellClicked(cellRow)
							}
						)
						.background(
							if (!cellRow.canEdit) MaterialTheme.colorScheme.tertiaryContainer
							else Color.Transparent
						)
				) {
					if (cellRow.n != -1) {
						Text(
							text = if (cellRow.n != 0) cellRow.n.toString() else ""
						)
					} else {
						val sortedSubCells = remember(cellRow.subCells) {
							cellRow.subCells.sortedBy { it.n }
						}
						
						FlowRow(
							maxItemsInEachRow = 3,
							verticalAlignment = Alignment.CenterVertically,
							horizontalArrangement = Arrangement.SpaceEvenly,
							modifier = Modifier
								.fillMaxSize()
						) {
							sortedSubCells.forEach { cell ->
								Text(
									text = cell.n.toString(),
									style = MaterialTheme.typography.labelSmall.copy(
										fontWeight = FontWeight.Light,
										fontSize = 8.sp
									)
								)
							}
						}
					}
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
				color = MaterialTheme.colorScheme.outlineVariant,
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
