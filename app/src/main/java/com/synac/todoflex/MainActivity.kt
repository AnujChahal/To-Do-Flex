package com.synac.todoflex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.synac.todoflex.entity.TaskDatabase
import com.synac.todoflex.repository.ToDoFlexRepository
import com.synac.todoflex.ui.TaskViewModelFactory
import com.synac.todoflex.ui.ToDoFlexViewModel
import com.synac.todoflex.ui.navigation.AppNavigation
import com.synac.todoflex.ui.screens.ToDoFlexScreen
import com.synac.todoflex.ui.theme.DarkBackground
import com.synac.todoflex.ui.theme.ToDoFlexTheme
import kotlin.jvm.java

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        //Init Database
        val db = Room.databaseBuilder(
            applicationContext,
            TaskDatabase::class.java,
            "todoflex_db"
        ).build()

        val repository = ToDoFlexRepository(db.taskDao())
        val viewModelFactory = TaskViewModelFactory(repository)
        val viewModel = ViewModelProvider(this, viewModelFactory)[ToDoFlexViewModel::class.java]
        setContent {
            ToDoFlexTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = DarkBackground
                ) {
                    AppNavigation(
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}
