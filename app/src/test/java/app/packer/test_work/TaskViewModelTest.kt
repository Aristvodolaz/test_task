package app.packer.test_work

import TaskViewModel
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.packer.test_work.db.dao.TaskDao
import app.packer.test_work.db.model.Task
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.*
import org.junit.*
import org.mockito.Mockito.*
import java.io.File

@ExperimentalCoroutinesApi
class TaskViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule() // Правило для выполнения задач в основном потоке

    private lateinit var taskViewModel: TaskViewModel // Экземпляр ViewModel для тестирования
    private lateinit var taskDao: TaskDao // Мок для DAO
    private val testDispatcher = TestCoroutineDispatcher() // Диспетчер корутин для тестирования

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher) // Установка тестового диспетчера для корутин
        taskDao = mock(TaskDao::class.java) // Создание мока для TaskDao
        `when`(taskDao.getAllTasks()).thenReturn(flow { emit(emptyList()) }) // Определение поведения мока
        taskViewModel = TaskViewModel(taskDao) // Инициализация ViewModel
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // Сброс диспетчера корутин после теста
        testDispatcher.cleanupTestCoroutines() // Очистка корутин
    }

    @Test
    fun `addTask should add a task if title and description are not blank`() = runBlockingTest {
        // Arrange (Подготовка данных)
        val task = Task(name = "Test Task", description = "Task Description")
        `when`(taskDao.getAllTasks()).thenReturn(flow { emit(emptyList()) }) // Установка мока

        // Act (Действие)
        taskViewModel.addTask(task.name, task.description) // Добавление задачи

        // Assert (Проверка)
        verify(taskDao).addTask(task) // Проверка вызова метода добавления задачи
    }

    @Test
    fun `deleteTask should remove a task from the database`() = runBlockingTest {
        // Arrange (Подготовка данных)
        val task = Task(name = "Test Task", description = "Task Description")
        `when`(taskDao.getAllTasks()).thenReturn(flow { emit(listOf(task)) }) // Установка мока

        // Act (Действие)
        taskViewModel.deleteTask(task) // Удаление задачи

        // Assert (Проверка)
        verify(taskDao).deleteTask(task) // Проверка вызова метода удаления задачи
    }

    @Test
    fun `updateTask should update a task with new values`() = runBlockingTest {
        // Arrange (Подготовка данных)
        val task = Task(name = "Old Task", description = "Old Description")
        val updatedTask = task.copy(name = "New Task", description = "New Description")
        `when`(taskDao.getAllTasks()).thenReturn(flow { emit(listOf(task)) }) // Установка мока

        // Act (Действие)
        taskViewModel.updateTask(task, updatedTask.name, updatedTask.description) // Обновление задачи

        // Assert (Проверка)
        verify(taskDao).updateTask(updatedTask) // Проверка вызова метода обновления задачи
    }

    @Test
    fun `saveTasksToFile should write tasks to file`() = runBlockingTest {
        // Arrange (Подготовка данных)
        val context = mock(Context::class.java) // Мок для контекста
        val tempDir = createTempDir() // Создание временной директории
        `when`(context.filesDir).thenReturn(tempDir) // Установка мока для файлового каталога

        val task1 = Task(name = "Task 1", description = "Description 1")
        val task2 = Task(name = "Task 2", description = "Description 2")
        taskViewModel.tasks.addAll(listOf(task1, task2)) // Добавление задач в ViewModel

        // Act (Действие)
        taskViewModel.saveTasksToFile(context) // Сохранение задач в файл

        val savedFile = File(tempDir, "tasks.txt") // Создание объекта файла
        Assert.assertTrue(savedFile.exists()) // Проверка существования файла

        savedFile.bufferedReader().use { reader ->
            val lines = reader.readLines() // Чтение строк из файла
            Assert.assertTrue(lines.contains("Task 1,Description 1")) // Проверка наличия первой задачи в файле
            Assert.assertTrue(lines.contains("Task 2,Description 2")) // Проверка наличия второй задачи в файле
        }

        savedFile.delete() // Удаление файла после проверки
        tempDir.delete() // Удаление временной директории
    }
}
