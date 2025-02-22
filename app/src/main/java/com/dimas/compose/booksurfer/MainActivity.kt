package com.dimas.compose.booksurfer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.dimas.compose.booksurfer.app.App
import com.dimas.compose.booksurfer.core.presentation.ui.theme.BookSurferTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BookSurferTheme {
                App()
            }
        }
    }
}
