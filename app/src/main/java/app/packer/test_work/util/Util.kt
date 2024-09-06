package app.packer.test_work.util

import android.content.Context
import app.packer.test_work.db.model.Task
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStreamWriter

fun saveTasksToFile(context: Context, tasks: List<Task>, fileName: String) {
    val gson = Gson()
    val json = gson.toJson(tasks)
    val file = File(context.filesDir, fileName)

    FileOutputStream(file).use { fos ->
        OutputStreamWriter(fos).use { writer ->
            writer.write(json)
        }
    }
}

fun loadTasksFromFile(context: Context, fileName: String): List<Task> {
    val gson = Gson()
    val file = File(context.filesDir, fileName)

    if (!file.exists()) return emptyList()

    file.inputStream().use { inputStream ->
        return gson.fromJson(inputStream.bufferedReader(), object : TypeToken<List<Task>>() {}.type)
    }
}
