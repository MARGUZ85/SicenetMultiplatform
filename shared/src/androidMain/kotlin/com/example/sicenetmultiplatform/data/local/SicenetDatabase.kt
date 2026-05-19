package com.example.sicenetmultiplatform.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        AndroidAcademicLoadEntity::class,
        AndroidCardexEntity::class,
        AndroidUnitGradesEntity::class,
        AndroidFinalGradesEntity::class,
        AndroidLastUpdateEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class SicenetDatabase : RoomDatabase() {

    abstract fun androidSicenetDao(): AndroidSicenetDao

    companion object {

        @Volatile
        private var instance: SicenetDatabase? = null

        fun getDatabase(context: Context): SicenetDatabase {
            return instance ?: synchronized(this) {
                val database = Room.databaseBuilder(
                    context.applicationContext,
                    SicenetDatabase::class.java,
                    "sicenet_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                instance = database
                database
            }
        }
    }
}
