package com.exerovv.deadpixel.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Assessment
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.WorkOutline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.exerovv.deadpixel.MainViewModel
import com.exerovv.deadpixel.R
import com.exerovv.deadpixel.core.network.UserRole
import com.exerovv.deadpixel.feature.orders.presentation.OrdersScreen
import com.exerovv.deadpixel.feature.orders.presentation.mywork.MyWorkScreen
import com.exerovv.deadpixel.feature.reports.presentation.list.ReportsScreen
import com.exerovv.deadpixel.feature.users.presentation.UsersScreen

private data class BottomTab(
    val labelRes: Int,
    val icon: ImageVector,
    val selectedIcon: ImageVector
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onLogout: () -> Unit,
    onNavigateToOrderDetail: (Int) -> Unit,
    onNavigateToReportDetail: (Int) -> Unit = {},
    onGenerateReport: () -> Unit = {},
    onCreateOrder: () -> Unit = {},
    viewModel: MainViewModel = hiltViewModel()
) {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    val isManager = viewModel.userRole == UserRole.MANAGER

    val tabs = if (isManager) listOf(
        BottomTab(R.string.tab_orders, Icons.AutoMirrored.Outlined.List, Icons.AutoMirrored.Filled.List),
        BottomTab(R.string.tab_employees, Icons.Outlined.Group, Icons.Default.Group),
        BottomTab(R.string.tab_reports, Icons.Outlined.Assessment, Icons.Default.Assessment),
        BottomTab(R.string.tab_profile, Icons.Outlined.AccountCircle, Icons.Default.AccountCircle)
    ) else listOf(
        BottomTab(R.string.tab_orders, Icons.AutoMirrored.Outlined.List, Icons.AutoMirrored.Filled.List),
        BottomTab(R.string.tab_my_work, Icons.Outlined.WorkOutline, Icons.Default.Work),
        BottomTab(R.string.tab_profile, Icons.Outlined.AccountCircle, Icons.Default.AccountCircle)
    )

    val topBarTitle = if (isManager) {
        when (selectedTab) {
            1 -> stringResource(R.string.tab_employees)
            2 -> stringResource(R.string.tab_reports)
            3 -> stringResource(R.string.tab_profile)
            else -> stringResource(R.string.app_name)
        }
    } else {
        when (selectedTab) {
            1 -> stringResource(R.string.my_work_title_master)
            2 -> stringResource(R.string.tab_profile)
            else -> stringResource(R.string.app_name)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = topBarTitle,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                tabs.forEachIndexed { index, tab ->
                    val selected = selectedTab == index
                    val label = stringResource(tab.labelRes)
                    NavigationBarItem(
                        selected = selected,
                        onClick = { selectedTab = index },
                        icon = {
                            Icon(
                                imageVector = if (selected) tab.selectedIcon else tab.icon,
                                contentDescription = label
                            )
                        },
                        label = { Text(label) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        if (isManager) {
            when (selectedTab) {
                0 -> OrdersScreen(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    onOrderClick = onNavigateToOrderDetail,
                    onCreateOrder = onCreateOrder
                )
                1 -> UsersScreen(
                    modifier = Modifier.fillMaxSize().padding(padding)
                )
                2 -> ReportsScreen(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    onReportClick = onNavigateToReportDetail,
                    onGenerateReport = onGenerateReport
                )
                3 -> ProfileScreen(
                    userId = viewModel.userId,
                    userRole = viewModel.userRole,
                    onLogout = { viewModel.logout(); onLogout() },
                    modifier = Modifier.fillMaxSize().padding(padding)
                )
            }
        } else {
            when (selectedTab) {
                0 -> OrdersScreen(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    onOrderClick = onNavigateToOrderDetail
                )
                1 -> MyWorkScreen(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    onOrderClick = onNavigateToOrderDetail
                )
                2 -> ProfileScreen(
                    userId = viewModel.userId,
                    userRole = viewModel.userRole,
                    onLogout = { viewModel.logout(); onLogout() },
                    modifier = Modifier.fillMaxSize().padding(padding)
                )
            }
        }
    }
}
