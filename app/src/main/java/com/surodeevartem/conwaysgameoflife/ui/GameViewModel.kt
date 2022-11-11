package com.surodeevartem.conwaysgameoflife.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.surodeevartem.conwaysgameoflife.domain.GameManager
import com.surodeevartem.conwaysgameoflife.entity.GameState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class GameViewModel @Inject constructor() : ViewModel() {
    var gameState by mutableStateOf(GameState())
        private set

    private val gameManager = GameManager()

    init {
        randomizeGameField()

        viewModelScope.launch {
            gameManager.cells.collect {
                gameState = gameState.copy(cells = it)
            }
        }

        viewModelScope.launch {
            gameManager.lifecycleState.collect {
                gameState = gameState.copy(lifecycleState = it)
            }
        }

        viewModelScope.launch {
            gameManager.stepsCount.collect {
                gameState = gameState.copy(stepsCount = it)
            }
        }
    }

    fun randomizeGameField() {
        gameManager.randomizeGameField()
    }

    fun start() {
        viewModelScope.launch(Dispatchers.IO) {
            gameManager.startGameLoop()
        }
    }

    fun stop() {
        gameManager.stopGameLoop()
    }
}