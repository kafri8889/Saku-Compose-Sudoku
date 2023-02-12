package com.anafthdev.saku.extension

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Dp.ceil(): Dp {
	return value.ceil().dp
}
