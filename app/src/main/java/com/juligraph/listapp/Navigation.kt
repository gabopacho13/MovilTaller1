package com.juligraph.listapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.juligraph.listapp.ui.screens.HomeScreen
import com.juligraph.listapp.ui.screens.UserDetail
import kotlinx.serialization.Serializable

@Serializable
sealed interface Routes {
    @Serializable
    object Home

    @Serializable
    data class UserDetail(val id: Int)
}


@Composable
fun NavigationStack(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.Home, modifier = modifier) {
        composable<Routes.Home> {
            HomeScreen(navController = navController)
        }
        composable<Routes.UserDetail> {
            val args = it.toRoute<Routes.UserDetail>()
            UserDetail(id = args.id)
        }
    }
}