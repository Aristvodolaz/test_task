import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun TaskApp(viewModel: TaskViewModel, context: Context) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Column(modifier = Modifier
        .padding(16.dp)
        .fillMaxSize()) {
        Text(
            text = "Task Manager",
            style = MaterialTheme.typography.h4.copy(fontSize = 24.sp),
            color = MaterialTheme.colors.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Task Title") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Task Description") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (title.isNotBlank() && description.isNotBlank()) {
                    viewModel.addTask(title, description)
                    title = ""
                    description = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Task")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
            Button(onClick = { viewModel.saveTasksToFile(context) }) {
                Text("Save to File")
            }
            Button(onClick = { viewModel.loadTasksFromFile(context) }) {
                Text("Load from File")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(viewModel.tasks.reversed()) { task ->
                TaskItem(task = task, onDelete = { viewModel.deleteTask(it) }, onUpdate = { updatedTask ->
                    viewModel.updateTask(task, updatedTask.name, updatedTask.description)
                })
            }
        }
    }
}
