package com.anafthdev.saku.ui.dashboard

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.anafthdev.saku.R
import com.anafthdev.saku.data.GameMode
import com.anafthdev.saku.data.SakuDestination
import com.anafthdev.saku.uicomponent.GameModeSelector

@Composable
fun DashboardScreen(
	navController: NavController,
	viewModel: DashboardViewModel
) {
	
	val context = LocalContext.current
	
	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Center,
		modifier = Modifier
			.fillMaxSize()
	) {
		Image(
//			painter = painterResource(id = R.drawable.ic_app_icon),
			painter = ColorPainter(Color.LightGray),
			contentDescription = null,
			modifier = Modifier
				.size(96.dp)
				.clip(MaterialTheme.shapes.large)
		)
		
		Spacer(modifier = Modifier.height(8.dp))
		
		GameModeSelector(
			gameModes = GameMode.values(),
			selectedGameMode = viewModel.selectedGameMode,
			onGameModeChanged = viewModel::updateGameMode,
			modifier = Modifier
				.fillMaxWidth(0.6f)
		)
		
		Spacer(modifier = Modifier.height(8.dp))
		
		OutlinedButton(
			onClick = {
				navController.navigate(
					SakuDestination.Game.Home.createRoute(
						mode = viewModel.selectedGameMode
					)
				)
			}
		) {
			Text("Play")
		}
		
		Spacer(modifier = Modifier.height(16.dp))
		
		Row(
			verticalAlignment = Alignment.CenterVertically
		) {
			IconButton(
				onClick = {
					navController.navigate(SakuDestination.Score.Home.route)
				}
			) {
				Icon(
					painter = painterResource(id = R.drawable.ic_award),
					contentDescription = null
				)
			}
			
			IconButton(
				onClick = {
					context.startActivity(
						Intent(Intent.ACTION_VIEW).apply {
							flags = Intent.FLAG_ACTIVITY_NEW_TASK
							data = Uri.parse("https://github.com/kafri8889/Saku-Compose-Sudoku")
						}
					)
				}
			) {
				Icon(
					painter = painterResource(id = R.drawable.ic_github_mark),
					contentDescription = null
				)
			}
		}
		
//		Spacer(modifier = Modifier.height(16.dp))
//
//		OpenSourceText()
	}
}

@Composable
fun OpenSourceText() {
	
	val context = LocalContext.current
	
	val openSourceProjectMsg = buildAnnotatedString {
		withStyle(
			style = LocalTextStyle.current.copy(
				textAlign = TextAlign.Center,
				fontWeight = FontWeight.Light
			).toSpanStyle()
		) {
			append("This is an open source project, source code can be found on ")
			
			pushStringAnnotation(tag = "github", annotation = "https://github.com/kafri8889/Saku-Compose-Sudoku")
			
			withStyle(
				style = SpanStyle(
					color = MaterialTheme.colorScheme.primary,
					fontWeight = FontWeight.Medium
				)
			) {
				append("GitHub")
			}
			
			pop()
			
			append(" or by clicking on the GitHub icon above")
		}
	}
	
	Text(
		text = "Open Source",
		style = MaterialTheme.typography.titleLarge.copy(
			fontWeight = FontWeight.Light
		)
	)
	
	Spacer(modifier = Modifier.padding(8.dp))
	
	ClickableText(
		text = openSourceProjectMsg,
		style = LocalTextStyle.current.copy(
			textAlign = TextAlign.Center
		),
		onClick = { offset ->
			openSourceProjectMsg.getStringAnnotations(
				tag = "github",
				start = offset,
				end = offset
			).firstOrNull()?.let { _ ->
				context.startActivity(
					Intent(Intent.ACTION_VIEW).apply {
						flags = Intent.FLAG_ACTIVITY_NEW_TASK
						data = Uri.parse("https://github.com/kafri8889/Saku-Compose-Sudoku")
					}
				)
			}
		},
		modifier = Modifier
			.fillMaxWidth(0.7f)
	)
}
