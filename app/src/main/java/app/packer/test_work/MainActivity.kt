package app.packer.test_work

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import app.packer.test_work.view.App

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Устанавливаем UI для активности с использованием Jetpack Compose
        setContent {
            // Запускаем функцию App, которая будет работать с ViewModel и базой данных
            App()
        }
    }
}
