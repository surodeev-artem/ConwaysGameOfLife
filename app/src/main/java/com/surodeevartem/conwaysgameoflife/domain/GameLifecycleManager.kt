package com.surodeevartem.conwaysgameoflife.domain

import android.util.Log
import com.surodeevartem.conwaysgameoflife.entity.Cell
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class GameLifecycleManager {
    private var isStarted = false

    private val _cells = MutableStateFlow<List<List<Cell>>>(emptyList())
    val cells: StateFlow<List<List<Cell>>> = _cells

    suspend fun startGameLoop(initialCells: List<List<Cell>>) {
        isStarted = true

        _cells.value = initialCells

        coroutineScope {
            launch {
                while (isStarted) {
                    val startTime = System.currentTimeMillis()
                    val cellsCopy = cells.value.map { cellsRow ->
                        cellsRow.map { cell ->
                            cell.copy()
                        }
                    }

                    cells.value.forEachIndexed() { i, cellsRow ->
                        cellsRow.forEachIndexed { j, cell ->
                            var livedNeighboursCount = 0
                            val leftIndex = if (i == 0) 49 else i - 1
                            val topIndex = if (j == 0) 49 else j - 1
                            val rightIndex = if (i == 49) 0 else i + 1
                            val bottomIndex = if (j == 49) 0 else j + 1
                            if (cells.value[leftIndex][topIndex].isAlive) livedNeighboursCount++
                            if (cells.value[i][topIndex].isAlive) livedNeighboursCount++
                            if (cells.value[rightIndex][topIndex].isAlive) livedNeighboursCount++
                            if (cells.value[leftIndex][j].isAlive) livedNeighboursCount++
                            if (cells.value[rightIndex][j].isAlive) livedNeighboursCount++
                            if (cells.value[leftIndex][bottomIndex].isAlive) livedNeighboursCount++
                            if (cells.value[i][bottomIndex].isAlive) livedNeighboursCount++
                            if (cells.value[rightIndex][bottomIndex].isAlive) livedNeighboursCount++


                            if (!cell.isAlive) {
                                cellsCopy[i][j].isAlive = livedNeighboursCount == 3
                            } else {
                                cellsCopy[i][j].isAlive = livedNeighboursCount == 2 || livedNeighboursCount == 3
                            }
                        }
                    }

                    _cells.value = cellsCopy
                    val d = 250 - (System.currentTimeMillis() - startTime)
                    delay(d)
                    Log.d("GameLoop", "Step is over")
                }
            }
        }
    }

    fun stopGameLoop() {
        isStarted = false
    }

    fun restartGame() {
        val newCells: MutableList<MutableList<Cell>> = MutableList(50) {
            MutableList(50) {
                Cell(isAlive = Random.nextBoolean())
            }
        }

        _cells.value = newCells
    }
}