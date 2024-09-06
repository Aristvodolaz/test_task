package app.packer.test_work.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import app.packer.test_work.db.dao.TaskDao
import app.packer.test_work.db.model.Task

@Database(entities = [Task::class],version = 1)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}
