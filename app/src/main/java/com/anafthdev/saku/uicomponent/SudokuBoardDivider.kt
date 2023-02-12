package com.anafthdev.saku.uicomponent

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset

@Composable
fun SudokuBoardDivider(
	maxHeight: Dp,
	thickness: Dp,
	shape: Shape = RoundedCornerShape(0),
	color: Color = MaterialTheme.colorScheme.outline,
	offsetHorz: Density.() -> IntOffset,
	offsetVert: Density.() -> IntOffset
) {
	
	Divider(
		color = color,
		thickness = thickness,
		modifier = Modifier
			.offset {
				offsetVert()
			}
			.size(thickness, maxHeight)
			.clip(shape)
	)
	
	Divider(
		color = color,
		thickness = thickness,
		modifier = Modifier
			.offset {
				offsetHorz()
			}
			.fillMaxWidth()
			.height(thickness)
			.clip(shape)
	)
}
