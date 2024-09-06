import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.packer.test_work.db.dao.TaskDao
import app.packer.test_work.db.model.Task
import kotlinx.coroutines.launch
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException

class TaskViewModel(private val taskDao: TaskDao) : ViewModel() {
    // Список задач, который наблюдается в UI
    var tasks = mutableStateListOf<Task>()
        private set

    // Инициализация ViewModel
    init {
        loadTasks() // Загрузка задач при создании ViewModel
    }

    // Загрузка задач из базы данных
    private fun loadTasks() {
        viewModelScope.launch {
            taskDao.getAllTasks().collect { taskList ->
                tasks.clear() // Очистка текущего списка задач
                tasks.addAll(taskList.reversed()) // Добавление задач в обратном порядке
            }
        }
    }

    // Добавление новой задачи
    fun addTask(title: String, description: String) {
        if (title.isNotBlank() && description.isNotBlank()) {
            viewModelScope.launch {
                val task = Task(name = title, description = description)
                taskDao.addTask(task) // Добавление задачи в базу данных
                loadTasks() // Перезагрузка задач после добавления
            }
        }
    }

    // Удаление задачи
    fun deleteTask(task: Task) {
        viewModelScope.launch {
            taskDao.deleteTask(task) // Удаление задачи из базы данных
            loadTasks() // Перезагрузка задач после удаления
        }
    }

    // Обновление задачи
    fun updateTask(task: Task, newTitle: String, newDescription: String) {
        if (newTitle.isNotBlank() && newDescription.isNotBlank()) {
            viewModelScope.launch {
                val updatedTask = task.copy(name = newTitle, description = newDescription)
                taskDao.updateTask(updatedTask) // Обновление задачи в базе данных
                // Не перезагружаем задачи здесь, так как это может вызвать излишнюю перерисовку
                // Для обновления UI используйте loadTasks() если требуется
            }
        }
    }

    // Сохранение задач в файл
    fun saveTasksToFile(context: Context, fileName: String = "tasks.txt") {
        viewModelScope.launch {
            try {
                val file = File(context.filesDir, fileName)
                val writer = BufferedWriter(FileWriter(file))
                tasks.forEach { task ->
                    writer.write("${task.name},${task.description}\n") // Запись задачи в файл
                }
                writer.close() // Закрытие файла
            } catch (e: IOException) {
//                Log.e("TaskViewModel", "Ошибка при сохранении задач в файл", e)
            }
        }
    }

    // Загрузка задач из файла
    fun loadTasksFromFile(context: Context, fileName: String = "tasks.txt") {
        viewModelScope.launch {
            try {
                val file = File(context.filesDir, fileName)
                if (file.exists()) {
                    val tasksFromFile = file.readLines().map { line ->
                        val parts = line.split(",")
                        Task(name = parts[0], description = parts[1]) // Создание задачи из строки файла
                    }
                    tasks.clear() // Очистка текущего списка задач
                    tasks.addAll(tasksFromFile) // Добавление задач из файла
                }
            } catch (e: IOException) {
//                Log.e("TaskViewModel", "Ошибка при загрузке задач из файла", e)
            }
        }
    }
}
