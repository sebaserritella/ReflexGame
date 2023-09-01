package com.sebaserritella.reflexgame.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sebaserritella.reflexgame.BuildConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class GameViewModel @Inject constructor() : ViewModel() {

    private var _currentState = MutableStateFlow(getInitialGameState())
    val currentState: StateFlow<GameState> = _currentState

    private var selectedList = mutableListOf<Int>()

    private var job: Job? = null
    private var timeDelay = 0L
    private var turnsInRow = 0
    private var maxValues = 0
    private var sizeBoard = 0
    private var percentDecrease = 0.0

    init {
        initialBoard()
    }

    fun resetGame() {
        _currentState.value = getInitialGameState()
        initialBoard()
    }

    fun run() {
        _currentState.value = GameState.Playing(
            score = 0, painted = getRandom()
        )

        job = viewModelScope.launch(Dispatchers.Default) {
            while (true) {
                delay(getReflexTime())
                turnsInRow++
                when (_currentState.value) {
                    is GameState.GameOver -> {

                    }

                    is GameState.Playing -> {
                        if (verifyBoxes(
                                selectedList, (_currentState.value as GameState.Playing).painted
                            )
                        ) {
                            _currentState.value = GameState.Playing(
                                score = (_currentState.value as GameState.Playing).score + 1,
                                painted = getRandom()
                            )
                        } else {
                            if ((_currentState.value as GameState.Playing).score > 1) {
                                _currentState.value = GameState.Playing(
                                    score = (_currentState.value as GameState.Playing).score - 1,
                                    painted = getRandom()
                                )
                            } else {
                                _currentState.value = GameState.GameOver(score = 0)
                                this.cancel()
                            }
                        }
                        //remove all selected boxes
                        selectedList = mutableListOf()
                    }

                    GameState.Start -> {

                    }
                }
            }
        }
    }

    fun play(boxClicked: Int) {
        selectedList.add(boxClicked)
    }

    private fun getReflexTime(): Long {
        if (turnsInRow == 5) {
            timeDelay = (timeDelay * percentDecrease).toLong()
            turnsInRow = 0
        }

        return timeDelay
    }

    private fun getRandom(): List<Int> {
        val result = mutableListOf<Int>()
        for (index in 1..maxValues) {
            var randomGeneratedValue = Random.nextInt(0, sizeBoard)
            while (result.contains(randomGeneratedValue)) {
                randomGeneratedValue = Random.nextInt(0, sizeBoard)
            }
            result.add(randomGeneratedValue)
        }
        return result
    }

    private fun getInitialGameState(): GameState = (GameState.Start)

    private fun initialBoard() {
        turnsInRow = 0
        maxValues = BuildConfig.MaxFlashes
        sizeBoard = BuildConfig.SizeBoard
        percentDecrease = BuildConfig.PercentDecrease.toDouble()
        timeDelay = BuildConfig.TimeDelay.toLong()
    }

    private fun verifyBoxes(selectedList: MutableList<Int>, painted: List<Int>): Boolean {
        painted.forEach {
            if (!selectedList.contains(it)) {
                return false
            }
        }

        return true
    }

    fun setState(gameOver: GameState) {
        _currentState.value = gameOver
    }
}