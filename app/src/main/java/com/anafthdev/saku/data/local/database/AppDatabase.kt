package com.anafthdev.saku.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.anafthdev.saku.data.local.database.dao.ScoreDao
import com.anafthdev.saku.data.model.Score

@Database(
	entities = [
		Score::class
	],
	version = 1
)
@TypeConverters(DatabaseTypeConverter::class)
abstract class AppDatabase: RoomDatabase() {
	
	abstract fun scoreDao(): ScoreDao
	companion object {
		private var INSTANCE: AppDatabase? = null
		
		fun getInstance(context: Context): AppDatabase {
			if (INSTANCE == null) {
				synchronized(AppDatabase::class.java) {
					INSTANCE = Room.databaseBuilder(
						context,
						AppDatabase::class.java,
						"app.db"
					).build()
				}
			}
			
			return INSTANCE!!
		}
	}
	
}