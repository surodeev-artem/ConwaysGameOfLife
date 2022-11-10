package com.surodeevartem.conwaysgameoflife.ui

import com.surodeevartem.conwaysgameoflife.entity.Cell

data class GameState(
    val isStarted: Boolean = false,
    val cells: List<List<Cell>> = emptyList()
)
