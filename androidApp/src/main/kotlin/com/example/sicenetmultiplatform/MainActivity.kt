package com.example.sicenetmultiplatform

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.example.sicenetmultiplatform.ui.MarsPhotosApp
import com.example.sicenetmultiplatform.ui.theme.MarsPhotosTheme

import com.example.sicenetmultiplatform.data.DefaultAppContainer
import com.example.sicenetmultiplatform.ui.SicenetViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val container = DefaultAppContainer().apply { init(applicationContext) }
        val viewModel = SicenetViewModel(container.sicenetRepository, container.sicenetLocalRepository)
        
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            MarsPhotosTheme {
                val darkTheme = isSystemInDarkTheme()
                val colorScheme = MaterialTheme.colorScheme
                val view = LocalView.current
                if (!view.isInEditMode) {
                    SideEffect {
                        val window = (view.context as Activity).window
                        window.statusBarColor = colorScheme.primary.toArgb()
                        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
                    }
                }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    MarsPhotosApp(viewModel)
                }
            }
        }
    }
}
