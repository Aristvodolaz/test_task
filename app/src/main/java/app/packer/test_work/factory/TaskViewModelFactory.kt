package app.packer.test_work.factory
import TaskViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.packer.test_work.db.dao.TaskDao

class TaskViewModelFactory(private val taskDao: TaskDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            return TaskViewModel(taskDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

