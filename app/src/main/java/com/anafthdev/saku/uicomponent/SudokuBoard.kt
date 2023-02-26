@file:JvmName("SudokuBoardKt")

package com.anafthdev.saku.uicomponent

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.anafthdev.saku.data.model.Cell

@Composable
fun SudokuBoard(
	cells: List<Cell>,
	selectedCell: Cell,
	modifier: Modifier = Modifier,
	shape: Shape = RoundedCornerShape(5),
	onCellClicked: (Cell) -> Unit
) {

	BoxWithConstraints(
		modifier = modifier
			.clip(shape)
			.border(
				width = 1.dp,
				color = Color.Black,
				shape = shape
			)
	) {
		
		val gridCellSize = remember(maxWidth) {
			maxWidth / 3
		}
		
		val cellSize = remember(gridCellSize) {
			gridCellSize / 3.2f
		}
		
		Row {
			for (i in 0 until 3) {
				Column {
					LazyVerticalGrid(
						columns = GridCells.Fixed(3),
						userScrollEnabled = false,
						verticalArrangement = Arrangement.Center,
						modifier = Modifier
							.size(gridCellSize)
							.border(
								width = 1.dp,
								color = Color.Black
							)
					) {
						itemsIndexed(cells.getOrNull(i)?.subCells ?: emptyList()) { i, cell ->
							CellBox(
								cell = cell,
								index = i,
								selected = selectedCell.n == cell.n,
								onClick = {
									onCellClicked(cell)
								},
								modifier = Modifier
									.size(cellSize)
									.aspectRatio(1f / 1f)
							)
						}
					}
					
					LazyVerticalGrid(
						columns = GridCells.Fixed(3),
						userScrollEnabled = false,
						verticalArrangement = Arrangement.Center,
						modifier = Modifier
							.size(gridCellSize)
							.border(
								width = 1.dp,
								color = Color.Black
							)
					) {
						itemsIndexed(cells.getOrNull(i + 3)?.subCells ?: emptyList()) { i, cell ->
							CellBox(
								cell = cell,
								index = i,
								selected = selectedCell.n == cell.n,
								onClick = {
									onCellClicked(cell)
								},
								modifier = Modifier
									.size(cellSize)
									.aspectRatio(1f / 1f)
							)
						}
					}
					
					LazyVerticalGrid(
						columns = GridCells.Fixed(3),
						userScrollEnabled = false,
						verticalArrangement = Arrangement.Center,
						modifier = Modifier
							.size(gridCellSize)
							.border(
								width = 1.dp,
								color = Color.Black
							)
					) {
						itemsIndexed(cells.getOrNull(i + 6)?.subCells ?: emptyList()) { i, cell ->
							CellBox(
								cell = cell,
								index = i,
								selected = selectedCell.n == cell.n,
								onClick = {
									onCellClicked(cell)
								},
								modifier = Modifier
									.size(cellSize)
									.aspectRatio(1f / 1f)
							)
						}
					}
				}
			}
		}
	}
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalAnimationApi::class)
@Composable
private fun CellBox(
	cell: Cell,
	index: Int,
	selected: Boolean,
	modifier: Modifier = Modifier,
	onClick: () -> Unit
) {
	
	val cellBackground by animateColorAsState(
		animationSpec = tween(300),
		targetValue = if (selected) MaterialTheme.colorScheme.primary
		else {
			if (cell.missingNum) MaterialTheme.colorScheme.background
			else MaterialTheme.colorScheme.surfaceVariant
		}
	)
	
	Box(modifier = modifier) {
		if (index == 1 || index == 4 || index == 7) {
			Box(
				modifier = Modifier
					.width(1.dp)
					.fillMaxHeight(0.8f)
					.background(
						color = MaterialTheme.colorScheme.outline,
						shape = CircleShape
					)
					.align(Alignment.CenterStart)
					.zIndex(2f)
			)
		}
		
		if (index == 3 || index == 4 || index == 5) {
			Box(
				modifier = Modifier
					.height(1.dp)
					.fillMaxWidth(0.8f)
					.background(
						color = MaterialTheme.colorScheme.outline,
						shape = CircleShape
					)
					.align(Alignment.TopCenter)
					.zIndex(2f)
			)
		}
		
		Box(
			contentAlignment = Alignment.Center,
			modifier = Modifier
				.padding(
					bottom = if (index in 0..2) 1.dp else 0.dp,
					top = if (index in 6..8) 1.dp else 0.dp
				)
				.fillMaxSize()
				.clickable(onClick = onClick)
				.padding(3.dp)
				.drawBehind {
					drawCircle(
						color = cellBackground
					)
				}
		) {
			AnimatedContent(
				targetState = cell.n,
				transitionSpec = {
					scaleIn(
						animationSpec = tween(250)
					) + fadeIn(
						animationSpec = tween(150)
					) with scaleOut(
						animationSpec = tween(300)
					) + fadeOut(
						animationSpec = tween(200)
					)
				}
			) { n ->
				if (n != -1) {
					Text(
						text = if (n != 0) n.toString() else "",
						style = LocalTextStyle.current.copy(
							color = if (selected) Color.White else Color.Black
						)
					)
				} else {
					val sortedSubCells = remember(cell.subCells) {
						cell.subCells.sortedBy { it.n }
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
		
		if (index == 3 || index == 4 || index == 5) {
			Box(
				modifier = Modifier
					.height(1.dp)
					.fillMaxWidth(0.8f)
					.background(
						color = MaterialTheme.colorScheme.outline,
						shape = CircleShape
					)
					.align(Alignment.BottomCenter)
					.zIndex(2f)
			)
		}
		
		if (index == 1 || index == 4 || index == 7) {
			Box(
				modifier = Modifier
					.width(1.dp)
					.fillMaxHeight(0.8f)
					.background(
						color = MaterialTheme.colorScheme.outline,
						shape = CircleShape
					)
					.align(Alignment.CenterEnd)
					.zIndex(2f)
			)
		}
	}
}
