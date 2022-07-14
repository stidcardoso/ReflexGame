package com.example.reflexgame.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reflexgame.model.GameState
import com.example.reflexgame.ui.view.TABLE_LENGTH
import kotlinx.coroutines.*

const val MAXIMUM_TURNS = 5
const val INITIAL_TURN_TIME = 3000L
const val DECREASE_TIME_FACTOR = 0.9

class GameViewModel : ViewModel() {

    private var _gameStateLiveData = MutableLiveData<GameState>()
    var gameStateLiveData = _gameStateLiveData as LiveData<GameState>

    private var _scoreLiveData = MutableLiveData<Int>()
    var scoreLiveData = _scoreLiveData as LiveData<Int>

    private var _squareHighlighted = MutableLiveData<Pair<Int, Int>>()
    var squareHighlighted = _squareHighlighted as LiveData<Pair<Int, Int>>


    private var maximumClickTime = INITIAL_TURN_TIME
    private var turns = 1
    private var coroutineJob: Job? = null

    fun startGame() {
        maximumClickTime = INITIAL_TURN_TIME
        _scoreLiveData.value = (0)
        _gameStateLiveData.value = GameState.STARTED
        generateHighlightSquarePosition()
    }

    private fun stopGame() {
        _gameStateLiveData.value = (GameState.STOPPED)
        _scoreLiveData.value = (0)
        turns = 1
        _squareHighlighted.value = (null)
    }

    fun onPositionClicked(position: Pair<Int, Int>) {
        if (position.first == _squareHighlighted.value?.first && position.second == _squareHighlighted.value?.second)
            increaseScore()
        else
            decreaseScore()
    }

    private fun startTurnTimer() {
        coroutineJob = viewModelScope.launch {
            delay(maximumClickTime)
            GlobalScope.launch(Dispatchers.Main) {
                decreaseScore()
            }
        }
    }

    private fun generateHighlightSquarePosition() {
        _squareHighlighted.value = (Pair(generateRandomPosition(), generateRandomPosition()))
        startTurnTimer()
    }

    private fun generateRandomPosition() = (0 until TABLE_LENGTH).random()

    private fun increaseScore() {
        coroutineJob?.cancel()
        _scoreLiveData.value = _scoreLiveData.value?.inc()
        turns = turns.inc()
        validateTurns()
        generateHighlightSquarePosition()
    }

    private fun decreaseScore() {
        coroutineJob?.cancel()
        _scoreLiveData.value = (_scoreLiveData.value?.dec())
        if (!isValidScore()) return
        turns = turns.inc()
        validateTurns()
        generateHighlightSquarePosition()
    }

    private fun isValidScore(): Boolean {
        if ((_scoreLiveData.value ?: 0) <= 0) {
            stopGame()
            return false
        }
        return true
    }

    private fun validateTurns() {
        if (turns == MAXIMUM_TURNS) {
            turns = 1
            decreaseMaximumClickTime()
        }
    }

    private fun decreaseMaximumClickTime() {
        maximumClickTime = (maximumClickTime * DECREASE_TIME_FACTOR).toLong()
    }
}
