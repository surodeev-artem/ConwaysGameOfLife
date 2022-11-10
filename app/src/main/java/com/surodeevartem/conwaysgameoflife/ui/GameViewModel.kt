package com.surodeevartem.conwaysgameoflife.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.surodeevartem.conwaysgameoflife.domain.GameLifecycleManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class GameViewModel @Inject constructor() : ViewModel() {
    var gameState by mutableStateOf(GameState())
        private set

    private val gameLifecycleManager = GameLifecycleManager()
    
    init {
        restartGame()

        viewModelScope.launch {
            gameLifecycleManager.cells.collect {
                gameState = gameState.copy(cells = it)
            }
        }
    }

    fun restartGame() {
        gameLifecycleManager.restartGame()
    }

    fun start() {
        gameState = gameState.copy(isStarted = true)

        viewModelScope.launch(Dispatchers.IO) {
            gameLifecycleManager.startGameLoop(gameState.cells)
        }

    }

    fun stop() {
        gameState = gameState.copy(isStarted = false)

        gameLifecycleManager.stopGameLoop()
    }
}