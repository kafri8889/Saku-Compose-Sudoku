package com.anafthdev.saku.runtime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.anafthdev.saku.runtime.navigation.SakuNavigation
import com.anafthdev.saku.theme.SakuTheme

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			SakuTheme {
				Surface(
					modifier = Modifier.fillMaxSize(),
					color = MaterialTheme.colorScheme.background
				) {
					SakuNavigation()
				}
			}
		}
	}
}
