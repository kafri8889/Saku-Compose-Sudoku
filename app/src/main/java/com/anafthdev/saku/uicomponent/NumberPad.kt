package com.anafthdev.saku.uicomponent

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowCrossAxisAlignment
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.SizeMode

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NumberPad(
	selectedNumber: Int,
	modifier: Modifier = Modifier,
	onNumberSelected: (Int) -> Unit
) {

	FlowRow(
		mainAxisSize = SizeMode.Expand,
		crossAxisAlignment = FlowCrossAxisAlignment.Center,
		lastLineMainAxisAlignment = FlowMainAxisAlignment.Center,
		modifier = modifier
	) {
		for (i in 1..9) {
			val selected = selectedNumber == i
			
			val backgroundColor by animateColorAsState(
				targetValue = if (selected) MaterialTheme.colorScheme.primary else Color.Transparent,
				animationSpec = tween(500)
			)
			
			Box(
				contentAlignment = Alignment.Center,
				modifier = Modifier
					.padding(4.dp)
					.size(40.dp)
					.clip(CircleShape)
					.drawBehind {
						drawCircle(backgroundColor)
					}
					.border(
						width = 1.dp,
						shape = CircleShape,
						color = if (!selected) MaterialTheme.colorScheme.outline else Color.Transparent
					)
					.clickable { onNumberSelected(i) }
			) {
				Text(
					text = i.toString(),
					style = LocalTextStyle.current.copy(
						color = if (selected) MaterialTheme.colorScheme.background
						else MaterialTheme.colorScheme.onBackground
					)
				)
			}
		}
	}
}
