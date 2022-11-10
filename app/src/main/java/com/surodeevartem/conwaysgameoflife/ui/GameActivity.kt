package com.surodeevartem.conwaysgameoflife.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.surodeevartem.conwaysgameoflife.ui.theme.ConwaysGameOfLifeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GameActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ConwaysGameOfLifeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Content()
                }
            }
        }
    }
}

@Composable
fun Content(viewModel: GameViewModel = viewModel()) {
    Column(modifier = Modifier.fillMaxSize()) {
        val cells = viewModel.gameState.cells
        val isStarted = remember {
            viewModel.gameState.isStarted
        }
        val darkThemeEnabled = isSystemInDarkTheme()
        Canvas(
            modifier = Modifier
                .background(
                    if (darkThemeEnabled)
                        Brush.verticalGradient(
                            0f to Color(0xff151515),
                            0.5f to Color(0xff151515),
                            1f to MaterialTheme.colors.background
                        )
                    else
                        Brush.verticalGradient(
                            0f to Color(0xffebebeb),
                            0.5f to Color(0xffebebeb),
                            1f to MaterialTheme.colors.background
                        )
                )
                .padding(16.dp)
                .weight(1f)
                .fillMaxWidth()
        ) {
            val canvasWidth = size.width
            val canvasHeight = size.height


            val cellOffset = 2f
            val cellSize = (canvasWidth - (49 * cellOffset)) / 50

            for (i in cells.indices) {
                for (j in cells[i].indices) {
                    drawRect(
                        color = if (cells[i][j].isAlive) {
                            if (darkThemeEnabled) {
                                Color.White
                            } else {
                                Color.Black
                            }
                        } else {
                            if (darkThemeEnabled) {
                                Color.Black
                            } else {
                                Color.White
                            }
                        },
                        topLeft = Offset(
                            x = i * cellSize + i * cellOffset,
                            y = j * cellSize + j * cellOffset
                        ),
                        size = Size(cellSize, cellSize)
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        ) {
            Button(modifier = Modifier.weight(1f), onClick = {
                if (viewModel.gameState.isStarted) viewModel.stop() else viewModel.start()
            }) {
                Text(text = if (viewModel.gameState.isStarted) "Stop" else "Start")
            }
            Spacer(modifier = Modifier.weight(0.25f))
            Button(modifier = Modifier.weight(1f), onClick = {
                viewModel.restartGame()
            }) {
                Text(text = "Generate")
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ConwaysGameOfLifeTheme {
        Greeting("Android")
    }
}