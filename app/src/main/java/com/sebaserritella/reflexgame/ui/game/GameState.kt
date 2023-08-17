package com.sebaserritella.reflexgame.ui.game

import androidx.compose.runtime.Immutable

@Immutable
sealed interface GameState {
    object Start : GameState

    data class Playing(
        val painted: List<Int>, val score: Int
    ) : GameState

    data class GameOver(
        val score: Int
    ) : GameState
}
