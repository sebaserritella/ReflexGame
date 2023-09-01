package com.sebaserritella.reflexgame.ui.game

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.MockKAnnotations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GameViewModelTest {

    private lateinit var viewModel: GameViewModel

    @get:Rule
    var rule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        viewModel = GameViewModel()
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun onAfter() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test reset game`() {
        //given
        val gameStateStart = (GameState.Start)
        viewModel.setState(GameState.GameOver(0))

        //when
        viewModel.resetGame()

        //Then
        assert(viewModel.currentState.value == gameStateStart)
    }

    @Test
    fun `test play game`() = runTest {
        //given
        val gameState = (GameState.Playing(listOf(1), 0))

        //when
        launch { viewModel.run() }
        launch {
            val painted = (viewModel.currentState.value as GameState.Playing).painted.first()
            viewModel.play(painted)
        }
        advanceUntilIdle()

        //Then
        if ((viewModel.currentState.value as GameState.Playing).painted.first() == gameState.painted.first()){
            assert((viewModel.currentState.value as GameState.Playing).score == 1)
        }
        else {
            assert((viewModel.currentState.value as GameState.Playing).score == 0)
        }

    }
}