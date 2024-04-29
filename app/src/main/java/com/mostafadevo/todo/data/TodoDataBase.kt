package com.mostafadevo.todo.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mostafadevo.todo.data.model.Todo

@TypeConverters(Converters::class)
@Database(entities = [Todo::class], version = 1, exportSchema = false)
abstract class TodoDataBase : RoomDatabase() {
    abstract fun todoDao(): TodoDAO

    companion object {
        @Volatile
        private var INSTANCE: TodoDataBase? = null

        fun getDatabase(context: Context): TodoDataBase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    TodoDataBase::class.java,
                    "todo_database"
                ).build().also {
                    INSTANCE = it
                }
            }
        }
    }
}