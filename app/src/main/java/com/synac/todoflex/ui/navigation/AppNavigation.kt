package com.synac.todoflex.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.synac.todoflex.ui.ToDoFlexViewModel
import com.synac.todoflex.ui.screens.AddTaskScreen
import com.synac.todoflex.ui.screens.ToDoFlexScreen

@Composable
fun AppNavigation(viewModel: ToDoFlexViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Home
    ) {
        //Route 1: ToDoFlexScreen
        composable<Home> {
            ToDoFlexScreen(
                viewModel = viewModel,
                navController = navController
            )
        }

        //Route 2: AddTaskScreen
        composable<AddTask> {
            AddTaskScreen(
                viewModel = viewModel,
                navController = navController
            )
        }
    }
}