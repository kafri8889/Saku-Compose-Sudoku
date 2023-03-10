package com.anafthdev.saku.ui.print

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.anafthdev.saku.R
import com.anafthdev.saku.extension.toast
import com.anafthdev.saku.utils.UriUtils
import com.anafthdev.saku.view.SudokuBoardView

@Composable
fun PrintScreen(
	viewModel: PrintViewModel,
	navController: NavController
) {
	
	val context = LocalContext.current
	
	val openDocTreeLauncher = rememberLauncherForActivityResult(
		contract = ActivityResultContracts.OpenDocumentTree(),
		onResult = { uri ->
			if (uri != null) {
				val flags = Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
				
				context.contentResolver.takePersistableUriPermission(uri, flags)
				viewModel.updateExportBoardPath("$uri|${UriUtils.getTreeDocumentPath(uri)}")
			}
		}
	)
	
	LaunchedEffect(viewModel.error) {
		if (viewModel.error.isNotEmpty()) {
			viewModel.error.toast(context)
			viewModel.updateError("")
		}
	}
	
	Column(
		modifier = Modifier
			.padding(8.dp)
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier
				.fillMaxWidth()
		) {
			Text(
				text = stringResource(R.string.include_solved_board),
				style = MaterialTheme.typography.bodyLarge
			)
			
			Spacer(modifier = Modifier.weight(1f))
			
			Checkbox(
				checked = viewModel.includeSolvedBoard,
				onCheckedChange =viewModel::updateIncludeSolvedBoard
			)
		}
		
		Spacer(modifier = Modifier.height(8.dp))
		
		OutlinedTextField(
			value = viewModel.exportPath,
			onValueChange = viewModel::updateExportPath,
			readOnly = true,
			textStyle = MaterialTheme.typography.bodyLarge,
			trailingIcon = {
				Row(verticalAlignment = Alignment.CenterVertically) {
					Box(
						modifier = Modifier
							.width(1.dp)
							.height(20.dp)
							.background(MaterialTheme.colorScheme.outline)
					)

					IconButton(
						onClick = {
							openDocTreeLauncher.launch(null)
						}
					) {
						Icon(
							painter = painterResource(id = R.drawable.ic_folder_2),
							contentDescription = null
						)
					}
				}
			},
			modifier = Modifier
				.fillMaxWidth()
				.imePadding()
		)
		
		Spacer(modifier = Modifier.height(8.dp))
		
		Button(
			enabled = viewModel.exportPath.isNotBlank(),
			onClick = {
				viewModel.print()
			},
			modifier = Modifier
				.fillMaxWidth()
		) {
			Text(stringResource(id = R.string.export_as_pdf))
		}
		
		Spacer(modifier = Modifier.height(8.dp))
		
		if (viewModel.generateBitmap) {
			AndroidView(
				factory = { ctx ->
					val view = SudokuBoardView(
						context = ctx,
						cells = viewModel.boardForPrint,
						onBitmapCreated = { bmp ->
							viewModel.addImageAndGenerate(ctx, bmp)
						}
					)
					
					view
				}
			)
			
			if (viewModel.includeSolvedBoard) {
				AndroidView(
					factory = { ctx ->
						val view = SudokuBoardView(
							context = ctx,
							cells = viewModel.solvedBoardForPrint,
							onBitmapCreated = { bmp ->
								viewModel.addImageAndGenerate(ctx, bmp)
							}
						)
						
						view
					}
				)
			}
		}
	}
}
