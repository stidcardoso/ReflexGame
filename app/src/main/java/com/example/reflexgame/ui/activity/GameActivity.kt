package com.example.reflexgame.ui.activity

import android.os.Bundle
import android.os.SystemClock
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.reflexgame.databinding.ActivityGameBinding
import com.example.reflexgame.model.GameState
import com.example.reflexgame.viewModel.GameViewModel

class GameActivity : AppCompatActivity() {

    private val viewModel: GameViewModel by viewModels()
    private lateinit var binding: ActivityGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        addObservers()
        addListeners()
    }

    private fun addListeners() {
        binding.buttonStart.setOnClickListener {
            restartTimer()
            viewModel.startGame()
        }
        binding.board.clickPositionListener = {
            viewModel.onPositionClicked(it)
        }
    }

    private fun addObservers() {
        addGameStateObserver()
        viewModel.scoreLiveData.observe(this) {
            binding.textScore.text = it?.toString()
        }
        viewModel.squareHighlighted.observe(this) {
            binding.board.highlightSquare(it)
        }
    }

    private fun addGameStateObserver() {
        viewModel.gameStateLiveData.observe(this) {
            if (it == GameState.STARTED) {
                binding.chronometer.start()
            } else {
                restartTimer()
            }
        }
    }

    private fun restartTimer() {
        binding.chronometer.base = SystemClock.elapsedRealtime()
        binding.chronometer.stop()
    }
}
