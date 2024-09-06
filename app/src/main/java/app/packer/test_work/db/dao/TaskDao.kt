package app.packer.test_work.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import app.packer.test_work.db.model.Task
import kotlinx.coroutines.flow.Flow


@Dao
interface TaskDao {

    @Insert
    suspend fun addTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Query("SELECT * FROM tasks ORDER BY id DESC")
    fun getAllTasks(): Flow<List<Task>>

    // Удаление всех задач и вставка новых в одной транзакции
    @Transaction
    suspend fun clearTasksAndInsertAll(tasks: List<Task>) {
        clearTasks()
        tasks.forEach { addTask(it) }
    }

    @Query("DELETE FROM tasks")
    suspend fun clearTasks()
}


