package com.anafthdev.saku.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.view.ViewTreeObserver
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import com.anafthdev.saku.data.model.Cell
import com.anafthdev.saku.uicomponent.SudokuBoard
import com.anafthdev.saku.utils.GraphicUtils

@SuppressLint("ViewConstructor")
class SudokuBoardView(
	private val context: Context,
	private val cells: List<Cell>,
	private val onBitmapCreated: (Bitmap) -> Unit
): LinearLayoutCompat(context) {
	
	private val widthPx = 2480
	private val heightPx = 3508
	
	init {
		val view = ComposeView(context).apply {
			visibility = GONE
			layoutParams = LayoutParams(width, height)
			
			setContent { 
				SudokuBoard(
					cells = cells,
					selectedCell = Cell.NULL,
					win = false,
					forPrint = true,
					highlightNumberEnabled = false,
					onCellClicked = {},
					modifier = Modifier
						.aspectRatio(1f/1f)
				)
			}
		}
		
		addView(view)
		
		viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
			override fun onGlobalLayout() {
				val bitmap = GraphicUtils.createBitmapFromView(
					view = view,
					width = widthPx,
					height = heightPx
				)
				
				onBitmapCreated(bitmap)
				viewTreeObserver.removeOnGlobalLayoutListener(this)
			}
		})
	}
	
}