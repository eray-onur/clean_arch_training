package com.eray.cleanarchtraining.data

import android.content.Context
import androidx.room.*
import com.eray.cleanarchtraining.data.models.TodoData

@TypeConverters(Converter::class)
@Database(entities = [TodoData::class], version = 1, exportSchema = false)
abstract class TodoDB : RoomDatabase() {

    abstract fun todoDao(): TodoDao

    companion object {
        @Volatile
        private var INSTANCE: TodoDB? = null


        fun getDatabase(context: Context): TodoDB {

            val tempInstance = INSTANCE;

            if(tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TodoDB::class.java,
                    "todo_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }

    }
}