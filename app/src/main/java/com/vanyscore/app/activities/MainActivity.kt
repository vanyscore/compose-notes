package com.vanyscore.app.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.vanyscore.app.navigation.App
import com.vanyscore.app.theme.ThemeProvider
import com.vanyscore.app.viewmodel.AppViewModelProvider
import com.vanyscore.notes.data.InMemoryInitializer
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var inMemoryInitializer: InMemoryInitializer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppViewModelProvider {
                ThemeProvider {
                    App()
                }
            }
        }
    }
}