package com.sebaserritella.reflexgame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.sebaserritella.reflexgame.ui.game.GameScreen
import com.sebaserritella.reflexgame.ui.game.GameViewModel
import com.sebaserritella.reflexgame.ui.theme.ReflexGameTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ReflexGameTheme {
                val viewModel = hiltViewModel<GameViewModel>()
                val gameState = viewModel.currentState.collectAsState().value

                val sizeBoard = BuildConfig.SizeBoard
                GameScreen(viewModel, gameState, sizeBoard)
            }
        }

    }
}
