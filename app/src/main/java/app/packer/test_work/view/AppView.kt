package app.packer.test_work.view

import TaskApp
import TaskViewModel
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import app.packer.test_work.db.TaskApps
import app.packer.test_work.factory.TaskViewModelFactory

@Composable
fun App() {
    val context = LocalContext.current
    val taskDao = (context.applicationContext as TaskApps).database.taskDao()
    val viewModel: TaskViewModel = viewModel(factory = TaskViewModelFactory(taskDao))

    TaskApp(viewModel, context)
}
