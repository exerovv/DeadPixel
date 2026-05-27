package com.exerovv.deadpixel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import com.exerovv.deadpixel.core.network.TokenManager
import com.exerovv.deadpixel.core.ui.theme.DeadPixelTheme
import com.exerovv.deadpixel.navigation.NavGraph
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DeadPixelTheme {
                val isLoggedIn = remember { tokenManager.getAccessToken() != null }
                NavGraph(isLoggedIn = isLoggedIn)
            }
        }
    }
}
