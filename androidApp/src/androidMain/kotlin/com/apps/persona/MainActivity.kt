package com.apps.persona

import android.graphics.Color
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.apps.shared.PersonaApp

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            enableEdgeToEdge(
                statusBarStyle = SystemBarStyle.auto(
                    Color.argb(0xe6, 0xFF, 0xFF, 0xFF),
                    Color.argb(0x80, 0x1b, 0x1b, 0x1b)
                )
            )
            PersonaApp()
        }
    }

}

@Preview
@Composable
fun PersonaAppPreview() {
    PersonaApp()
}