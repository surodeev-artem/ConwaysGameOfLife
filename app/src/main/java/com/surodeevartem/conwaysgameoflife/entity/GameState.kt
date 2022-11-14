package com.surodeevartem.conwaysgameoflife.entity

data class GameState(
    val lifecycleState: LifecycleState = LifecycleState.PAUSED,
    val cells: List<List<Cell>> = emptyList(),
    val stepsCount: Int = 0,
    val highScore: Int = 0
)
