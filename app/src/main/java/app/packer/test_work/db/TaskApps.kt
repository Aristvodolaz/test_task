package app.packer.test_work.db
import TaskViewModel
import android.app.Application
import androidx.room.Room

class TaskApps : Application() {
    lateinit var database: TaskDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            TaskDatabase::class.java,
            "task_database"
        ).build()
    }
}
