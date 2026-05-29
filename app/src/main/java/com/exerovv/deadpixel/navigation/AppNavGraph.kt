package com.exerovv.deadpixel.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.exerovv.deadpixel.core.network.TokenManager
import com.exerovv.deadpixel.feature.auth.presentation.login.LoginScreen
import com.exerovv.deadpixel.feature.auth.presentation.register.RegisterScreen
import com.exerovv.deadpixel.feature.diagnostics.presentation.DiagnosticsScreen
import com.exerovv.deadpixel.feature.orders.presentation.create.CreateOrderScreen
import com.exerovv.deadpixel.feature.orders.presentation.detail.OrderDetailScreen
import com.exerovv.deadpixel.feature.reports.presentation.detail.ReportDetailScreen
import com.exerovv.deadpixel.feature.reports.presentation.generate.GenerateReportScreen
import com.exerovv.deadpixel.feature.workplans.presentation.WorkPlanScreen
import com.exerovv.deadpixel.ui.MainScreen

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Register : Screen("register")
    data object Main : Screen("main")
    data object OrderDetail : Screen("order/{orderId}") {
        fun createRoute(orderId: Int) = "order/$orderId"
    }
    data object Diagnostics : Screen("diagnostics/{orderId}") {
        fun createRoute(orderId: Int) = "diagnostics/$orderId"
    }
    data object ReportDetail : Screen("report-detail/{reportId}") {
        fun createRoute(reportId: Int) = "report-detail/$reportId"
    }
    data object GenerateReport : Screen("generate-report")
    data object WorkPlan : Screen("workplan/{orderId}") {
        fun createRoute(orderId: Int) = "workplan/$orderId"
    }
    data object CreateOrder : Screen("create-order")
}

@Composable
fun NavGraph(isLoggedIn: Boolean, tokenManager: TokenManager) {
    val navController = rememberNavController()

    LaunchedEffect(Unit) {
        tokenManager.unauthorizedEvents.collect {
            navController.navigate(Screen.Login.route) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) Screen.Main.route else Screen.Login.route
    ) {
        composable(route = Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }
        composable(route = Screen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }
        composable(route = Screen.Main.route) {
            MainScreen(
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Main.route) { inclusive = true }
                    }
                },
                onNavigateToOrderDetail = { orderId ->
                    navController.navigate(Screen.OrderDetail.createRoute(orderId))
                },
                onNavigateToReportDetail = { reportId ->
                    navController.navigate(Screen.ReportDetail.createRoute(reportId))
                },
                onGenerateReport = {
                    navController.navigate(Screen.GenerateReport.route)
                },
                onCreateOrder = {
                    navController.navigate(Screen.CreateOrder.route)
                }
            )
        }
        composable(
            route = Screen.OrderDetail.route,
            arguments = listOf(navArgument("orderId") { type = NavType.IntType })
        ) {
            OrderDetailScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToDiagnostics = { orderId ->
                    navController.navigate(Screen.Diagnostics.createRoute(orderId))
                },
                onNavigateToWorkPlan = { orderId ->
                    navController.navigate(Screen.WorkPlan.createRoute(orderId))
                }
            )
        }
        composable(
            route = Screen.Diagnostics.route,
            arguments = listOf(navArgument("orderId") { type = NavType.IntType })
        ) {
            DiagnosticsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(
            route = Screen.ReportDetail.route,
            arguments = listOf(navArgument("reportId") { type = NavType.IntType })
        ) {
            ReportDetailScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(route = Screen.GenerateReport.route) {
            GenerateReportScreen(
                onNavigateBack = { navController.popBackStack() },
                onGenerated = { reportId ->
                    navController.navigate(Screen.ReportDetail.createRoute(reportId)) {
                        popUpTo(Screen.GenerateReport.route) { inclusive = true }
                    }
                }
            )
        }
        composable(
            route = Screen.WorkPlan.route,
            arguments = listOf(navArgument("orderId") { type = NavType.IntType })
        ) {
            WorkPlanScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(route = Screen.CreateOrder.route) {
            CreateOrderScreen(
                onNavigateBack = { navController.popBackStack() },
                onCreated = { orderId ->
                    navController.navigate(Screen.OrderDetail.createRoute(orderId)) {
                        popUpTo(Screen.CreateOrder.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
