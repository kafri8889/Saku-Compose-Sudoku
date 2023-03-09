package com.anafthdev.saku.ui.score

import android.icu.text.DateFormat
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anafthdev.saku.data.model.Score
import com.anafthdev.saku.data.repository.ScoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ScoreViewModel @Inject constructor(
	private val scoreRepository: ScoreRepository
): ViewModel() {
	
	private val dateFormatter = DateFormat.getDateInstance(DateFormat.SHORT)
	
	var scores = mutableStateListOf<Score>()
		private set
	
	init {
		viewModelScope.launch(Dispatchers.IO) {
			scoreRepository.getAll().collect { scoreList ->
				withContext(Dispatchers.Main) {
					scores.apply {
						clear()
						addAll(scoreList)
					}
				}
			}
		}
	}
	
	fun deleteScore(score: Score) {
		viewModelScope.launch(Dispatchers.IO) {
			scoreRepository.delete(score)
		}
	}
	
	fun formatDate(date: Long): String {
		return dateFormatter.format(date)
	}
	
}