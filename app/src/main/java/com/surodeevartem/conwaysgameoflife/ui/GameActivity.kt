package com.surodeevartem.conwaysgameoflife.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.surodeevartem.conwaysgameoflife.R
import com.surodeevartem.conwaysgameoflife.domain.GameManager
import com.surodeevartem.conwaysgameoflife.entity.Cell
import com.surodeevartem.conwaysgameoflife.entity.LifecycleState
import com.surodeevartem.conwaysgameoflife.ui.theme.ConwaysGameOfLifeTheme
import com.surodeevartem.conwaysgameoflife.ui.theme.DeadCellLight
import com.surodeevartem.conwaysgameoflife.ui.theme.ScoreFont
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
                    Content() {
                        Toast.makeText(this, "Game over", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}

@Composable
fun Content(viewModel: GameViewModel = viewModel(), gameOverCallback: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        val cells = viewModel.gameState.cells
        val lifecycleState = viewModel.gameState.lifecycleState
        val stepsCount = viewModel.gameState.stepsCount
        val highScore = viewModel.gameState.highScore
        val darkThemeEnabled = isSystemInDarkTheme()

        if (lifecycleState == LifecycleState.GAME_OVER) {
            gameOverCallback()
        }

        ScoresText(stepsCount, highScore)

        GameField(
            Modifier
                .padding(16.dp)
                .weight(1f)
                .fillMaxWidth(),
            darkThemeEnabled,
            cells
        )

        Row(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        ) {
            Button(modifier = Modifier.weight(1f), onClick = {
                when (viewModel.gameState.lifecycleState) {
                    LifecycleState.STARTED -> {
                        viewModel.stop()
                    }

                    LifecycleState.PAUSED -> {
                        viewModel.start()
                    }

                    else -> {
                        viewModel.randomizeGameField()
                        viewModel.start()
                    }
                }
            }) {
                Text(
                    text = if (viewModel.gameState.lifecycleState == LifecycleState.STARTED) {
                        stringResource(R.string.stop)
                    } else {
                        stringResource(R.string.start)
                    }
                )
            }
            Spacer(modifier = Modifier.weight(0.25f))
            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    viewModel.randomizeGameField()
                }
            ) {
                Text(text = stringResource(R.string.regenerate))
            }
        }
    }
}

@Composable
fun ScoresText(score: Int, highScore: Int) {
    Row(modifier = Modifier.fillMaxWidth()) {
        ScoreText(
            title = stringResource(R.string.score),
            value = score,
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
                .padding(16.dp)
        )

        ScoreText(
            title = stringResource(R.string.high_score),
            value = highScore,
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
                .padding(16.dp)
        )
    }
}

@Composable
fun ScoreText(title: String, value: Int, modifier: Modifier) {
    Text(
        text = String.format(title, value),
        modifier = modifier,
        textAlign = TextAlign.Center,
        fontFamily = ScoreFont,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    )
}

@Composable
fun GameField(modifier: Modifier, darkThemeEnabled: Boolean, cells: List<List<Cell>>) {
    Canvas(
        modifier
    ) {
        val canvasWidth = size.width

        val cellsGapSize = 2f
        val cellsGapSum = ((GameManager.GAME_FIELD_WIDTH - 1) * cellsGapSize)
        val cellSize = (canvasWidth - cellsGapSum) / GameManager.GAME_FIELD_WIDTH

        for (x in cells.indices) {
            for (y in cells[x].indices) {
                drawRoundRect(
                    color = if (cells[x][y].isAlive) {
                        if (darkThemeEnabled) {
                            Color.White
                        } else {
                            Color.Black
                        }
                    } else {
                        if (darkThemeEnabled) {
                            Color.Black
                        } else {
                            DeadCellLight
                        }
                    },
                    topLeft = Offset(
                        x = x * cellSize + x * cellsGapSize,
                        y = y * cellSize + y * cellsGapSize
                    ),
                    size = Size(cellSize, cellSize),
                    cornerRadius = CornerRadius(8f, 8f)
                )
            }
        }
    }
}
