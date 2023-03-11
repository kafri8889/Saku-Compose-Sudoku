package com.anafthdev.saku.utils

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import timber.log.Timber

object UriUtils {
	
	fun getTreeDocumentPath(uri: Uri): String? {
		if (isExternalStorageDocument(uri)) {
			val docId = DocumentsContract.getTreeDocumentId(uri)
			val split = docId.split(":".toRegex()).toTypedArray()
			
			Timber.i("split: ${split.contentDeepToString()}")
			
			val type = split[0]
			return if ("primary".equals(type, ignoreCase = true)) {
				Environment.getExternalStorageDirectory().toString() + "/" + split[1]
			} else "sdcard/${split[1]}"
		}
		
		return null
	}
	
	fun getDataColumn(context: Context, uri: Uri?, selection: String?, selectionArgs: Array<String>?): String? {
		var cursor: Cursor? = null
		val column = "_data"
		val projection = arrayOf(column)
		try {
			cursor = context.contentResolver.query(uri!!, projection, selection, selectionArgs,null)
			if (cursor != null && cursor.moveToFirst()) {
				val columnIndex: Int = cursor.getColumnIndexOrThrow(column)
				return cursor.getString(columnIndex)
			}
		} finally {
			cursor?.close()
		}
		return null
	}
	
	fun isExternalStorageDocument(uri: Uri): Boolean {
		return "com.android.externalstorage.documents" == uri.authority
	}
	
	fun isDownloadsDocument(uri: Uri): Boolean {
		return "com.android.providers.downloads.documents" == uri.authority
	}
	
	fun isMediaDocument(uri: Uri): Boolean {
		return "com.android.providers.media.documents" == uri.authority
	}
	
}