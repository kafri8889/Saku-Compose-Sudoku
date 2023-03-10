package com.anafthdev.saku.ui.print

import android.content.Context
import android.graphics.Bitmap
import android.icu.text.SimpleDateFormat
import android.os.Environment
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anafthdev.saku.R
import com.anafthdev.saku.UserPreferences
import com.anafthdev.saku.common.GameEngine
import com.anafthdev.saku.data.ARG_INITIAL_BOARD
import com.anafthdev.saku.data.ARG_SOLVED_BOARD
import com.anafthdev.saku.data.model.Cell
import com.anafthdev.saku.data.repository.UserPreferencesRepository
import com.anafthdev.saku.extension.toast
import com.itextpdf.text.Document
import com.itextpdf.text.Image
import com.itextpdf.text.PageSize
import com.itextpdf.text.pdf.PdfWriter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.Locale
import javax.inject.Inject
import kotlin.math.min

@HiltViewModel
class PrintViewModel @Inject constructor(
	private val userPreferencesRepository: UserPreferencesRepository,
	private val gameEngine: GameEngine,
	savedStateHandle: SavedStateHandle
): ViewModel() {
	
	private val timeFormatter = SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.getDefault())
	private var userPreferences = UserPreferences()
	
	private val initialBoard = savedStateHandle.getStateFlow(ARG_INITIAL_BOARD, "")
	private val solvedBoard = savedStateHandle.getStateFlow(ARG_SOLVED_BOARD, "")
	
	private val _imageBa = MutableStateFlow(emptyList<ByteArray>())
	private val imageBa: StateFlow<List<ByteArray>> = _imageBa
	
	var generateBitmap by mutableStateOf(false)
		private set
	
	var includeSolvedBoard by mutableStateOf(true)
		private set
	
	var exportPath by mutableStateOf("")
		private set
	
	var error by mutableStateOf("")
		private set
	
	var printSuccess by mutableStateOf<Boolean?>(null)
		private set
	
	var boardForPrint = mutableStateListOf<Cell>()
		private set
	
	var solvedBoardForPrint = mutableStateListOf<Cell>()
		private set
	
	init {
		if (!defaultPrintDirectory.exists()) defaultPrintDirectory.mkdirs()
		
		viewModelScope.launch {
			initialBoard.collect { boardJson ->
				val board = gameEngine.boardFromJson(boardJson)
				
				boardForPrint.apply {
					clear()
					addAll(board)
				}
			}
		}
		
		viewModelScope.launch {
			solvedBoard.collect { boardJson ->
				val board = gameEngine.boardFromJson(boardJson)
				
				solvedBoardForPrint.apply {
					clear()
					addAll(board)
				}
			}
		}
		
		viewModelScope.launch {
			userPreferencesRepository.getUserPreferences.collect { preferences ->
				val realPath = preferences.exportBoardPath.split("|").getOrNull(1)
				
				exportPath = realPath ?: ""
				userPreferences = preferences
			}
		}
	}
	
	fun updateGenerateBitmap(generate: Boolean) {
		generateBitmap = generate
	}
	
	fun updateIncludeSolvedBoard(include: Boolean) {
		includeSolvedBoard = include
	}
	
	fun updateExportPath(path: String) {
		exportPath = path
	}
	
	fun updateError(err: String) {
		error = err
	}
	
	fun updatePrintSuccess(success: Boolean?) {
		printSuccess = success
	}
	
	fun updateExportBoardPath(path: String) {
		viewModelScope.launch {
			userPreferencesRepository.setExportBoardPath(path)
		}
	}
	
	fun addImageAndGenerate(context: Context, bitmap: Bitmap) {
		val bos = ByteArrayOutputStream()
		
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos)
		
		val byteArray = bos.toByteArray()
		
		viewModelScope.launch {
			_imageBa.emit(imageBa.value.toMutableList().apply { add(byteArray) })
			
			when {
				includeSolvedBoard -> {
					if (imageBa.value.size == 2) {
						generatePdf(context)
					}
				}
				else -> generatePdf(context)
			}
		}
	}
	
	fun generatePdf(context: Context) {
		val byteArrays = imageBa.value
		val uri = userPreferences.exportBoardPath.split("|")[0].toUri()
		
		try {
			val doc = DocumentFile.fromTreeUri(context, uri)
			val file = doc?.createFile("application/pdf", "Saku ${timeFormatter.format(System.currentTimeMillis())}.pdf")
			val pfd = context.contentResolver.openFileDescriptor(file!!.uri, "w")
			val fos = FileOutputStream(pfd!!.fileDescriptor)
			
			val document = Document()
			
			PdfWriter.getInstance(document, fos)
			
			document.apply {
				open()
				
				pageSize = PageSize.A4
				
				byteArrays.forEach { bytes ->
					val img = Image.getInstance(bytes).apply {
						val padding = min(PageSize.A4.width, PageSize.A4.height) * 0.05f
						
						scaleToFit(PageSize.A4.width - padding, PageSize.A4.height - padding)
						
						val x: Float = (PageSize.A4.width - scaledWidth) / 2
						val y: Float = (PageSize.A4.height - scaledHeight) / 2
						
						setAbsolutePosition(x, y)
					}
					
					add(img)
					newPage()
				}
				
				close()
			}
			
			fos.close()
			pfd.close()
			
			updatePrintSuccess(true)
			context.getString(R.string.export_successful).toast(context)
		} catch (e: Exception) {
			e.printStackTrace()
			updatePrintSuccess(false)
		}
		
		updateGenerateBitmap(false)
		viewModelScope.launch {
			_imageBa.emit(emptyList())
		}
	}
	
	fun print() {
		generateBitmap = true
	}
	
	companion object {
		val defaultPrintDirectory = File(
			Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
			"Saku"
		)
	}
	
}