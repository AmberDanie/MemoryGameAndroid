package com.example.memorygame

import android.widget.Button
import androidx.compose.runtime.Immutable

@Immutable
data class GameUiState(
    val currentLevel: Int = 1,
    val currentLevelPath: MutableList<Int> = mutableListOf(),
    val nextButtonIndex: Int = 0,
    val isGameOver: Boolean = true,
    val buttonsList: MutableList<Button> = mutableListOf()
)