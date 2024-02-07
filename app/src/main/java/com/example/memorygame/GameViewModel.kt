package com.example.memorygame

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.memorygame.ui.theme.blue
import com.example.memorygame.ui.theme.darkBlue
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random.Default.nextInt

class GameViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()
    private var colorMapCopy = _uiState.value.currentStateMap.toMutableList()

    fun checkUserPick(chosenButtonIndex: Int) {
        if (colorMapCopy[chosenButtonIndex] == 1) {
            if (_uiState.value.countOfChosen == _uiState.value.currentLevel - 1) {
                nextLevel()
            }
            else {
                _uiState.update { currentState ->
                    currentState.copy(
                        countOfChosen = _uiState.value.countOfChosen.inc(),
                    )
                }
                colorMapCopy[chosenButtonIndex] = 0
            }
        }
        else {
            gameOver()
        }
    }

    private fun nextLevel() {
        updateGameState()
        for (i in 0 until 25) {
            val colorList = _uiState.value.currentColorMap.toMutableList()
                colorList[i] = darkBlue
            if (colorMapCopy[i] == 1) {
                _uiState.update {currentState ->
                    currentState.copy(
                        currentColorMap = colorList.toList()
                    )
                }
            }
        }
        viewModelScope.launch {
            delay(1500)
            _uiState.update {currentState ->
                currentState.copy(
                    currentColorMap = List(25) {blue}
                )
            }
        }
    }

    private fun gameOver() {
        _uiState.update { currentState ->
            currentState.copy(
                isGameOver = true
            )
        }

    }

    private fun updateGameState() {
        updatePath()
        colorMapCopy = _uiState.value.currentStateMap.toMutableList()
        _uiState.update { currentState ->
            currentState.copy(
                isGameOver = false,
                currentLevel = currentState.currentLevel.inc(),
                countOfChosen = 0
            )
        }

    }

    private fun updatePath() {
        while (true) {
            val pickNext = nextInt(0, 25)
            if (_uiState.value.currentStateMap[pickNext] != 1) {
                val stateList = _uiState.value.currentStateMap.toMutableList()
                stateList[pickNext] = 1
                _uiState.update {currentState ->
                    currentState.copy(
                        currentStateMap = stateList.toList()
                    )
                }
                break
            }
        }
    }

    fun resetGame() {
        _uiState.value = GameUiState(currentLevel = 0,
            currentStateMap = MutableList(25) { 0 },
            isGameOver = false,
            buttonEnabled = false)
        nextLevel()
        colorMapCopy = _uiState.value.currentStateMap.toMutableList()
    }
}