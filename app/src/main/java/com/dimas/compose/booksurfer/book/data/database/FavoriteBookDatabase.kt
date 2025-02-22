package com.dimas.compose.booksurfer.book.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.driver.bundled.BundledSQLiteDriver


@Database(
    entities = [BookEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    StringListTypeConverter::class
)
abstract class FavoriteBookDatabase : RoomDatabase() {
    abstract val favoriteBookDao: FavoriteBookDao

    companion object {
        const val DB_NAME = "book.db"

        @Volatile
        private var Instance: FavoriteBookDatabase? = null

        fun getDatabase(context: Context): FavoriteBookDatabase {
            return Instance ?: synchronized(this) {
                val appContext = context.applicationContext
                val dbFile = appContext.getDatabasePath(DB_NAME)

                Room.databaseBuilder<FavoriteBookDatabase>(
                    context = appContext,
                    name = dbFile.absolutePath
                )
                    .fallbackToDestructiveMigration(dropAllTables = true)
                    .setDriver(BundledSQLiteDriver())
                    .build()
                    .also { Instance = it }
            }
        }
    }
}