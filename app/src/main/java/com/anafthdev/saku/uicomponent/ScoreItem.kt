package com.anafthdev.saku.uicomponent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.anafthdev.saku.R
import com.anafthdev.saku.data.local.database.ScoreDataProvider
import com.anafthdev.saku.data.model.Score
import com.anafthdev.saku.extension.hourMinuteFormat
import com.anafthdev.saku.extension.icon
import com.anafthdev.saku.theme.SakuTheme

@Preview
@Composable
fun ScoreItemPreview() {
	SakuTheme {
		ScoreItem(
			index = 1,
			score = ScoreDataProvider.scores[1],
			date = "01/08/04"
		)
	}
}

@Composable
fun ScoreItem(
	index: Int,
	score: Score,
	date: String,
	modifier: Modifier = Modifier
) {
	
	val second = remember {
		hourMinuteFormat(score.time % 60)
	}
	
	val minute = remember {
		hourMinuteFormat((score.time / 60) % 60)
	}
	
	val hours = remember {
		hourMinuteFormat(score.time / 3600)
	}
	
	Card(modifier = modifier) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier
				.padding(8.dp)
		) {
			Box(
				contentAlignment = Alignment.Center
			) {
				Icon(
					painter = painterResource(id = score.difficulty.icon),
					contentDescription = null,
					modifier = Modifier
						.size(32.dp)
				)
				
				Text(
					text = "$index",
					style = MaterialTheme.typography.bodyMedium.copy(
						color = Color.White
					),
					modifier = Modifier
						.zIndex(2f)
				)
			}
			
			Spacer(modifier = Modifier.width(8.dp))
			
			Row(
				horizontalArrangement = Arrangement.SpaceEvenly,
				modifier = Modifier.
				fillMaxWidth()
			) {
				Column(
					horizontalAlignment = Alignment.CenterHorizontally
				) {
					Text(
						text = stringResource(R.string.date),
						style = MaterialTheme.typography.titleSmall
					)
					
					Spacer(modifier = Modifier.height(4.dp))
					
					Text(
						text = date,
						style = MaterialTheme.typography.bodyMedium
					)
				}
				
				Column(
					horizontalAlignment = Alignment.CenterHorizontally
				) {
					Text(
						text = stringResource(R.string.difficulty),
						style = MaterialTheme.typography.titleSmall
					)
					
					Spacer(modifier = Modifier.height(4.dp))
					
					Text(
						text = score.difficulty.name,
						style = MaterialTheme.typography.bodyMedium
					)
				}
				
				Column(
					horizontalAlignment = Alignment.CenterHorizontally
				) {
					Text(
						text = stringResource(R.string.time),
						style = MaterialTheme.typography.titleSmall
					)
					
					Spacer(modifier = Modifier.height(4.dp))
					
					Text(
						text = "$hours:$minute:$second",
						style = MaterialTheme.typography.bodyMedium
					)
				}
			}
		}
	}
}
