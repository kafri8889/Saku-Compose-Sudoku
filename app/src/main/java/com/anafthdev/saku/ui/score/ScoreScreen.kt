package com.anafthdev.saku.ui.score

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.anafthdev.saku.R
import com.anafthdev.saku.data.local.database.ScoreDataProvider
import com.anafthdev.saku.uicomponent.ScoreItem

@Composable
fun ScoreScreen(
	viewModel: ScoreViewModel,
	navController: NavController
) {

	if (viewModel.scores.isEmpty()) {
		Box(
			contentAlignment = Alignment.Center,
			modifier = Modifier
				.fillMaxSize()
		) {
			Text(
				text = stringResource(R.string.you_have_no_game_history),
				style = MaterialTheme.typography.titleMedium.copy(
					fontWeight = FontWeight.Medium
				)
			)
		}
	} else {
		LazyColumn(
			modifier = Modifier
				.fillMaxSize()
				.systemBarsPadding()
		) {
			itemsIndexed(ScoreDataProvider.scores) { i, score ->
				ScoreItem(
					index = i + 1,
					score = score,
					date = viewModel.formatDate(score.date),
					modifier = Modifier
						.padding(
							vertical = 4.dp,
							horizontal = 8.dp
						)
				)
			}
		}
	}
}
