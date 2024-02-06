package com.example.memorygame

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.example.memorygame.ui.theme.blue

@Immutable
data class GameUiState(
    val currentLevel: Int = 1,
    val currentStateMap: List<Int> = List(25){ 0 },
    val currentColorMap: List<Color> = List(25) { blue },
    val countOfChosen: Int = 0,
    val isGameOver: Boolean = false,
    val buttonEnabled: Boolean = true
)