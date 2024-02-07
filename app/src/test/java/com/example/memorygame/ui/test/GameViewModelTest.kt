@file:Suppress("DEPRECATION")

package com.example.memorygame.ui.test

import com.example.memorygame.GameViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GameViewModelTest {
    private val dispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private val viewModel = GameViewModel()

    @Test
    fun gameViewModel_CorrectLevelUp() {
        viewModel.resetGame()
        val currentGameUiState = viewModel.uiState.value
        val stateMap = currentGameUiState.currentStateMap
        for (i in stateMap.indices) {
            if (stateMap[i] == 1) {
                viewModel.checkUserPick(i)
            }
        }
        assertFalse(currentGameUiState.isGameOver)
        assertEquals(1, currentGameUiState.currentLevel)
    }

    @Test
    fun gameViewModel_fullGameCheck() {
        viewModel.resetGame()
        for (i in 0 until 24) {
            val currentGameUiState = viewModel.uiState.value
            val stateMap = currentGameUiState.currentStateMap
            for (j in stateMap.indices) {
                if (stateMap[j] == 1) {
                    viewModel.checkUserPick(j)
                }
            }
        }
        assertEquals(25, viewModel.uiState.value.currentLevel)
    }
}