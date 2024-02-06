package com.example.memorygame

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.memorygame.ui.theme.MemoryGameTheme
import com.example.memorygame.ui.theme.lightBlue

@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    gameViewModel: GameViewModel = viewModel()
) {
    val gameUiState by gameViewModel.uiState.collectAsState()
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = CenterHorizontally
    ) {
        Text(text = stringResource(id = R.string.game_title),
            style = typography.displayLarge, fontFamily = FontFamily.Serif)
        GameLayout(
            modifier = modifier,
            currentLevel = gameUiState.currentLevel,
            colorMap = gameUiState.currentColorMap,
            checkInput = {gameViewModel.checkUserPick(it)},
            buttonsEnabled = !gameUiState.buttonEnabled)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp, end = 32.dp, top = 8.dp)
        )
        {
            Button(onClick = { gameViewModel.resetGame() },
                modifier = Modifier.fillMaxWidth(),
                enabled = gameUiState.buttonEnabled
            ) {
                Text(
                    text = stringResource(id = R.string.start)
                )
            }
        }

        if (gameUiState.isGameOver) {
            ResultDialog(lastLevel = gameUiState.currentLevel,
                onPlayAgain = { gameViewModel.resetGame() })
        }
    }
}

@Composable
fun GameLayout(
    modifier: Modifier = Modifier,
    currentLevel: Int,
    checkInput: (Int) -> Unit,
    colorMap: List<Color>,
    buttonsEnabled: Boolean
) {
    var currentButtonIndex = 0

    Card(
        modifier = modifier.padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Text(
            modifier = Modifier
                .clip(shapes.small)
                .background(colorScheme.surfaceTint)
                .padding(horizontal = 5.dp, vertical = 4.dp)
                .align(End),
            text = stringResource(R.string.current_level, currentLevel),
            style = typography.titleLarge,
            color = colorScheme.onPrimary
        )
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = CenterHorizontally
        ) {
            for (i in 0 until 5) {
                Row {
                    for  (j in 0 until 5) {
                        Card {
                            MemoryButton(currentButtonIndex,
                                checkInput = checkInput,
                                colorMap = colorMap,
                                buttonsEnabled = buttonsEnabled)
                            currentButtonIndex++
                        }
                    }
                }
            }
            Text(modifier = modifier.padding(16.dp),
                textAlign = TextAlign.Center,
                text = stringResource(id = R.string.game_description),
                style=typography.labelLarge)
        }
    }
}

@Composable
fun MemoryButton(
    buttonIndex: Int,
    checkInput: (Int) -> Unit,
    colorMap: List<Color>,
    buttonsEnabled: Boolean) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val color: Color = if (isPressed) colorScheme.primary
        else colorMap[buttonIndex]

    Button(
        modifier = Modifier.padding(5.dp),
        enabled = buttonsEnabled,
        onClick = {
            checkInput(buttonIndex)
        },
        interactionSource = interactionSource,
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            disabledContainerColor = lightBlue
        )
    ) {}
}

@Composable
fun ResultDialog(
    lastLevel: Int,
    onPlayAgain: () -> Unit,
    modifier: Modifier = Modifier
) {
    val activity = (LocalContext.current as Activity)
    AlertDialog(
        onDismissRequest = {},
        title = { Text(text = stringResource(R.string.congratulations)) },
        text = {Text(text = stringResource(R.string.you_finished, lastLevel - 1))},
        modifier = modifier,
        dismissButton = {
            TextButton(
                onClick = {activity.finish()}
            ) {
                Text(text = stringResource(R.string.exit))
            }
        },
        confirmButton = {
            TextButton(
                onClick = onPlayAgain) {
                Text(text = stringResource(R.string.play_again))
            }
        }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GameScreenPreview() {
    MemoryGameTheme {
        GameScreen()
    }
}