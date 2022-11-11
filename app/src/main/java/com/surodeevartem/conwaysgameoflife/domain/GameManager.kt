package com.surodeevartem.conwaysgameoflife.domain

import android.util.Log
import com.surodeevartem.conwaysgameoflife.entity.Cell
import com.surodeevartem.conwaysgameoflife.entity.LifecycleState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.random.Random

class GameManager {
    private val _cells = MutableStateFlow<List<List<Cell>>>(emptyList())
    val cells: StateFlow<List<List<Cell>>> = _cells

    private val _lifecycleState = MutableStateFlow(LifecycleState.PAUSED)
    val lifecycleState: StateFlow<LifecycleState> = _lifecycleState

    private val _stepsCount = MutableStateFlow(0)
    val stepsCount: StateFlow<Int> = _stepsCount

    private val stepsHashcode: MutableList<Int> = mutableListOf()

    suspend fun startGameLoop() {
        _lifecycleState.value = LifecycleState.STARTED

        while (lifecycleState.value == LifecycleState.STARTED) {
            makeGameStep()
        }
    }

    fun stopGameLoop() {
        _lifecycleState.value = LifecycleState.PAUSED
    }

    fun randomizeGameField() {
        val generatedCells: MutableList<MutableList<Cell>> = MutableList(GAME_FIELD_WIDTH) {
            MutableList(GAME_FIELD_HEIGHT) {
                Cell(isAlive = Random.nextBoolean())
            }
        }

        _cells.value = generatedCells
        _stepsCount.value = 0
    }

    private suspend fun makeGameStep() {
        val startStepTime = System.currentTimeMillis()

        val cellsCopy = cells.value.map { cellsRow ->
            cellsRow.map { cell ->
                cell.copy()
            }
        }

        cells.value.forEachIndexed() { cellX, cellsRow ->
            cellsRow.forEachIndexed { cellY, cell ->
                val aliveNeighboursCount = getAliveNeighboursCount(cellX, cellY)
                cellsCopy[cellX][cellY].isAlive = cellIsAlive(cell.isAlive, aliveNeighboursCount)
            }
        }

        _cells.value = cellsCopy

        val elapsedTimeMillis = System.currentTimeMillis() - startStepTime
        val delayTimeMillis = STEP_TIME_MILLIS - elapsedTimeMillis
        delay(delayTimeMillis)

        checkGameOver()

        _stepsCount.value++

        Log.d("GameLoop", "Step completed in $elapsedTimeMillis millis")
    }

    private fun getAliveNeighboursCount(cellX: Int, cellY: Int): Int {
        var aliveNeighboursCount = 0

        val leftIndex = if (cellX == 0) GAME_FIELD_WIDTH - 1 else cellX - 1
        val topIndex = if (cellY == 0) GAME_FIELD_HEIGHT - 1 else cellY - 1
        val rightIndex = if (cellX == GAME_FIELD_WIDTH - 1) 0 else cellX + 1
        val bottomIndex = if (cellY == GAME_FIELD_HEIGHT - 1) 0 else cellY + 1

        if (cells.value[leftIndex][topIndex].isAlive) aliveNeighboursCount++
        if (cells.value[cellX][topIndex].isAlive) aliveNeighboursCount++
        if (cells.value[rightIndex][topIndex].isAlive) aliveNeighboursCount++
        if (cells.value[leftIndex][cellY].isAlive) aliveNeighboursCount++
        if (cells.value[rightIndex][cellY].isAlive) aliveNeighboursCount++
        if (cells.value[leftIndex][bottomIndex].isAlive) aliveNeighboursCount++
        if (cells.value[cellX][bottomIndex].isAlive) aliveNeighboursCount++
        if (cells.value[rightIndex][bottomIndex].isAlive) aliveNeighboursCount++

        return aliveNeighboursCount
    }

    private fun cellIsAlive(cellAlreadyAlive: Boolean, aliveNeighboursCount: Int): Boolean {
        return if (!cellAlreadyAlive) {
            aliveNeighboursCount == 3
        } else {
            aliveNeighboursCount == 2 || aliveNeighboursCount == 3
        }
    }

    private fun checkGameOver() {
        checkStepRetrying()
        checkForLivingCells()
    }

    private fun checkStepRetrying() {
        val stepHashcode = _cells.value.hashCode()
        if (stepsHashcode.contains(stepHashcode)) _lifecycleState.value = LifecycleState.GAME_OVER
        stepsHashcode.add(stepHashcode)
    }

    private fun checkForLivingCells() {
        var hasLivingCells = false
        _cells.value.flatten().forEach { cell ->
            if (cell.isAlive) hasLivingCells = true
        }
        if (!hasLivingCells) _lifecycleState.value = LifecycleState.GAME_OVER
    }


    companion object {
        const val GAME_FIELD_WIDTH = 50
        const val GAME_FIELD_HEIGHT = 50
        const val STEP_TIME_MILLIS = 250
    }
}