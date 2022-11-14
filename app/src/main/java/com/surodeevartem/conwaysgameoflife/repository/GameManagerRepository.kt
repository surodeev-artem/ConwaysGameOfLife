package com.surodeevartem.conwaysgameoflife.repository

import android.content.Context
import com.surodeevartem.conwaysgameoflife.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GameManagerRepository @Inject constructor(@ApplicationContext private val context: Context) {
    private val prefs = context.getSharedPreferences("Data", Context.MODE_PRIVATE)
    private val highScoreKey = context.getString(R.string.high_score_key)

    fun getHighScore(): Int {
        return prefs.getInt(highScoreKey, 0)
    }

    fun setHighScore(score: Int) {
        with (prefs.edit()) {
            putInt(highScoreKey, score)
            apply()
        }
    }
}