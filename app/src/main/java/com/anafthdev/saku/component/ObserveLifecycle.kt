package com.anafthdev.saku.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver

@Composable
fun ObserveLifecycle(
	onCreate: () -> Unit = {},
	onStart: () -> Unit = {},
	onResume: () -> Unit = {},
	onPause: () -> Unit = {},
	onStop: () -> Unit = {},
	onDestroy: () -> Unit = {},
	onAny: () -> Unit = {}
) {
	
	val owner = LocalLifecycleOwner.current
	
	DisposableEffect(owner) {
		val observer = LifecycleEventObserver { _, event ->
			when (event) {
				Lifecycle.Event.ON_CREATE -> onCreate()
				Lifecycle.Event.ON_START -> onStart()
				Lifecycle.Event.ON_RESUME -> onResume()
				Lifecycle.Event.ON_PAUSE -> onPause()
				Lifecycle.Event.ON_STOP -> onStop()
				Lifecycle.Event.ON_DESTROY -> onDestroy()
				Lifecycle.Event.ON_ANY -> onAny()
			}
		}
		
		owner.lifecycle.addObserver(observer)
		
		onDispose {
			owner.lifecycle.removeObserver(observer)
		}
	}
}
