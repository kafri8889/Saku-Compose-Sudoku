package com.anafthdev.saku.uicomponent

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.anafthdev.saku.R
import com.anafthdev.saku.extension.iconId
import com.anafthdev.saku.extension.iconIdToSudokuGameAction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SudokuGameAction(
	selected: SudokuGameAction,
	modifier: Modifier = Modifier,
	enabled: Boolean = true,
	onClick: (SudokuGameAction) -> Unit
) {
	
	val alpha = remember (enabled) {
		if (enabled) 1f else 0.32f
	}

	BoxWithConstraints {
		val chipWidth = maxWidth / (SudokuGameActionDefaults.actions.size + 1)
		
		Row(
			horizontalArrangement = Arrangement.Center,
			modifier = modifier
		) {
			SudokuGameActionDefaults.actions.forEach { (iconId, _) ->
				val backgroundColor by animateColorAsState(
					targetValue = if (selected.iconId == iconId && enabled) MaterialTheme.colorScheme.primary
					else MaterialTheme.colorScheme.background,
					animationSpec = tween(500)
				)
				
				Box(
					contentAlignment = Alignment.Center,
					modifier = Modifier
						.padding(horizontal = 4.dp)
						.defaultMinSize(minHeight = FilterChipDefaults.Height)
						.width(chipWidth)
						.clip(RoundedCornerShape(25))
						.border(
							width = 1.dp,
							color = MaterialTheme.colorScheme.outline.copy(alpha = alpha),
							shape = RoundedCornerShape(25)
						)
						.drawBehind {
							drawRect(backgroundColor)
						}
						.clickable(
							enabled = enabled,
							onClick = {
								onClick(iconIdToSudokuGameAction(iconId))
							}
						)
				) {
					CompositionLocalProvider(
						LocalContentColor provides if (selected.iconId == iconId) Color.White.copy(alpha = alpha)
						else LocalContentColor.current.copy(alpha = alpha)
					) {
						Icon(
							painter = painterResource(id = iconId),
							contentDescription = null
						)
					}
				}
			}
		}
	}
}

enum class SudokuGameAction {
	Undo,
	Validate,
	Pencil,
	Redo,
	
	None
}

object SudokuGameActionDefaults {
	
	val undo = R.drawable.ic_undo to R.string.undo
	val validate = R.drawable.ic_check to R.string.validate
	val pencil = R.drawable.ic_edit to R.string.pencil
	val redo = R.drawable.ic_redo to R.string.redo
	
	val selectableActions = arrayOf(
		SudokuGameAction.Pencil
	)
	
	val actions = arrayOf(
		undo,
		validate,
		pencil,
		redo
	)
	
}
