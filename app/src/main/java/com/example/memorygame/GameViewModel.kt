package com.example.memorygame

import android.widget.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random.Default.nextInt

class GameViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    fun checkUserPick(chosenButtonIndex: Int): Boolean {
        if (chosenButtonIndex == _uiState.value.currentLevelPath[_uiState.value.nextButtonIndex]) {
            if (_uiState.value.nextButtonIndex == _uiState.value.currentLevel) {
                nextLevel()
            }
            else {
                _uiState.update { currentState ->
                    currentState.copy(
                        nextButtonIndex = _uiState.value.nextButtonIndex.inc()
                    )
                }
            }
            return true
        }
        else {
            gameOver()
            return false
        }
    }

    fun nextLevel() {
        updateGameState()
    }

    fun gameOver() {
        _uiState.update { currentState ->
            currentState.copy(
                isGameOver = true
            )
        }
    }

    fun updateGameState() {
        val path = updatePath()
        _uiState.update { currentState ->
            currentState.copy(
                isGameOver = false,
                currentLevel = currentState.currentLevel.inc(),
                nextButtonIndex = 0
            )
        }
    }

    fun updatePath(): MutableList<Int> {
        while (true) {
            val pickNext = nextInt(1, 25)
            if (_uiState.value.currentLevelPath.last() != pickNext) {
                _uiState.value.currentLevelPath.add(pickNext)
                break
            }
        }
        return _uiState.value.currentLevelPath
    }

    fun resetGame() {
        val pickNext = nextInt(0, 24)
        println(pickNext)
        _uiState.value = GameUiState(currentLevel = 1,
            currentLevelPath = mutableListOf(pickNext),
            nextButtonIndex = 0,
            isGameOver = false)
    }

}