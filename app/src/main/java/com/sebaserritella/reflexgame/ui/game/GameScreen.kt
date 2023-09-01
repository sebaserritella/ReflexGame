package com.sebaserritella.reflexgame.ui.game

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sebaserritella.reflexgame.BuildConfig
import com.sebaserritella.reflexgame.R
import com.sebaserritella.reflexgame.ui.components.MyAlertDialog
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@Composable
fun GameScreen(
    viewModel: GameViewModel, gameState: GameState, sizeBoard: Int
) {
    if (gameState is GameState.GameOver) {
        MyAlertDialog() {
            viewModel.resetGame()
        }
    }
    Column {
        Board(viewModel, gameState, sizeBoard)
        InfoLayer(gameState)
        CommandLayer(viewModel, gameState)
    }
}

@Composable
fun Board(viewModel: GameViewModel, gameState: GameState, sizeBoard: Int) {
    Box {
        LazyVerticalGrid(columns = GridCells.Fixed(
            Math.sqrt(BuildConfig.SizeBoard.toDouble()).toInt()
        ), contentPadding = PaddingValues(
            start = 12.dp, top = 16.dp, end = 12.dp, bottom = 16.dp
        ), content = {
            items(sizeBoard) {
                Box(
                    Modifier
                        .border(border = BorderStroke(2.dp, Color.Black))
                        .aspectRatio(1f)
                        .background(
                            if ((gameState is GameState.Playing) && gameState.painted.contains(
                                    it
                                )
                            ) {
                                Color.Blue
                            } else {
                                Color.White
                            }
                        )
                        .fillMaxWidth()
                        .clickable {
                            viewModel.play(it)
                        }) {}
            }
        })
    }
}

@Composable
fun InfoLayer(gameState: GameState) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(
                start = 12.dp, top = 16.dp, end = 12.dp, bottom = 16.dp
            )
    ) {
        Column(
            Modifier.fillMaxWidth(0.5f), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(
                    id = R.string.points, if (gameState is GameState.Playing) gameState.score else 0
                ),
                fontSize = 30.sp,
                fontStyle = FontStyle.Normal,
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Bold
            )
        }
        Column(
            Modifier.fillMaxWidth(0.5f), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TimerClock(gameState)
        }
    }
}

@Composable
fun CommandLayer(viewModel: GameViewModel, gameState: GameState) {
    Button(
        onClick = {
            viewModel.run()
        },
        enabled = gameState !is GameState.Playing,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 16.dp, 16.dp, 16.dp),
        shape = RoundedCornerShape(10),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Gray,
            contentColor = Color.Black,
        )
    ) {
        val text = when (gameState) {
            is GameState.GameOver -> stringResource(R.string.gameover)
            is GameState.Playing -> stringResource(R.string.playing)
            GameState.Start -> stringResource(R.string.start)
        }
        Text(
            text = text,
            fontSize = 30.sp,
            fontStyle = FontStyle.Normal,
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Composable
fun TimerClock(gameState: GameState) {
    var ticks by remember { mutableStateOf(0) }

    when (gameState) {
        is GameState.GameOver -> {
            ticks = 0
        }

        is GameState.Playing -> {
            LaunchedEffect(Unit) {
                while (true) {
                    delay(1.seconds)
                    ticks++
                }
            }
        }

        GameState.Start -> {
            ticks = 0
        }
    }

    val minutes = ticks / 60
    val seconds = ticks % 60

    Text(
        text = when (gameState) {
            is GameState.GameOver -> {
                stringResource(id = R.string.timer_format, minutes, seconds)
            }

            is GameState.Playing -> {
                stringResource(id = R.string.timer_format, minutes, seconds)
            }

            GameState.Start -> stringResource(id = R.string.timer_format, 0, 0)
        },
        fontSize = 30.sp,
        fontStyle = FontStyle.Normal,
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
    )
}

