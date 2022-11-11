package com.surodeevartem.conwaysgameoflife.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColors(
    primary = Purple200,
    primaryVariant = Purple700,
    secondary = Teal200
)

private val LightColorPalette = lightColors(
    primary = Purple500,
    primaryVariant = Purple700,
    secondary = Teal200
)

@Composable
fun ConwaysGameOfLifeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    StatusBarColor(darkTheme)

    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}

@Composable
private fun StatusBarColor(darkThemeEnabled: Boolean) {
    val systemUiController = rememberSystemUiController()
    if (darkThemeEnabled) {
        systemUiController.setStatusBarColor(
            color = GameFieldBackgroundDark
        )
    } else {
        systemUiController.setStatusBarColor(
            color = GameFieldBackgroundLight
        )
    }
}