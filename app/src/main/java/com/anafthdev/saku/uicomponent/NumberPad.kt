package com.anafthdev.saku.uicomponent

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.anafthdev.saku.data.model.RemainingNumber

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NumberPad(
	selectedNumber: Int,
	remainingNumbers: List<RemainingNumber>,
	modifier: Modifier = Modifier,
	onNumberSelected: (Int) -> Unit
) {
	
	val config = LocalConfiguration.current
	
	FlowRow(
		horizontalArrangement = Arrangement.Center,
		maxItemsInEachRow = 5,
		modifier = modifier
	) {
		for (i in 1..9) {
			val selected = selectedNumber == i
			
			val backgroundColor by animateColorAsState(
				targetValue = if (selected) MaterialTheme.colorScheme.primary else Color.Transparent,
				animationSpec = tween(500)
			)
			
			val remainingNumber by rememberUpdatedState(
				newValue = if (remainingNumbers.isNotEmpty()) {
					remainingNumbers[i - 1].remaining.toString()
				} else "0"
			)
			
			Box(
				contentAlignment = Alignment.Center,
				modifier = Modifier
					.padding(4.dp)
					.size(config.smallestScreenWidthDp.dp / 8f)
					.clip(RoundedCornerShape(25))
					.drawBehind {
						drawRect(backgroundColor)
					}
					.border(
						width = 1.dp,
						shape = RoundedCornerShape(25),
						color = if (!selected) MaterialTheme.colorScheme.outline else Color.Transparent
					)
					.clickable { onNumberSelected(i) }
			) {
				Column(
					horizontalAlignment = Alignment.CenterHorizontally
				) {
					AutoResizeText(
						text = i.toString(),
						style = MaterialTheme.typography.bodyMedium.copy(
							color = if (selected) MaterialTheme.colorScheme.background
							else MaterialTheme.colorScheme.onBackground
						)
					)
					
					Text(
						text = remainingNumber,
						style = MaterialTheme.typography.labelSmall.copy(
							color = if (selected) Color.LightGray
							else Color.Gray
						)
					)
				}
			}
		}
	}
}
