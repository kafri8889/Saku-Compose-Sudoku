package com.anafthdev.saku.uicomponent

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit

private const val TEXT_SCALE_REDUCTION_INTERVAL = 0.9f

@Composable
fun AutoResizeText(
	modifier: Modifier = Modifier,
	text: String,
	color: Color = Color.Unspecified,
	textAlign: TextAlign = TextAlign.Center,
	style: TextStyle,
	targetTextSizeHeight: TextUnit = style.fontSize,
	maxLines: Int = 1,
) {
	var textSize by remember { mutableStateOf(targetTextSizeHeight) }
	
	val textColor = color.takeOrElse {
		style.color.takeOrElse {
			Color.Black
		}
	}
	
	Text(
		modifier = modifier,
		text = text,
		color = textColor,
		textAlign = textAlign,
		fontSize = textSize,
		fontFamily = style.fontFamily,
		fontStyle = style.fontStyle,
		fontWeight = style.fontWeight,
		lineHeight = style.lineHeight,
		maxLines = maxLines,
		overflow = TextOverflow.Ellipsis,
		onTextLayout = { textLayoutResult ->
			val maxCurrentLineIndex: Int = textLayoutResult.lineCount - 1
			
			if (textLayoutResult.isLineEllipsized(maxCurrentLineIndex)) {
				textSize = textSize.times(TEXT_SCALE_REDUCTION_INTERVAL)
			}
		},
	)
}
