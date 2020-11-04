package com.example.android.guesstheword.screens.score

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ScoreViewModel(initScore: Int) : ViewModel() {

    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score

    private val _eventPlayAgain = MutableLiveData<Boolean>()
    val eventPlayAgain: LiveData<Boolean>
        get() = _eventPlayAgain

    init {
        _score.value = initScore
    }

    private fun onPlayAgain() {
        _eventPlayAgain.value = true
    }

    private fun onPlayAgainComplete() {
        _eventPlayAgain.value = false
    }

    fun playAgain() {
        onPlayAgain()
        onPlayAgainComplete()
    }


//    override fun onCleared() {
//        super.onCleared()
//        Log.i("cacSocoreViewmodel", "ScoreViewModel destroyed")
//    }
}
