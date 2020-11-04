package com.example.android.guesstheword.screens.title

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TittleViewModel : ViewModel() {
    private val _initGame = MutableLiveData<Boolean>()
    val initGame: LiveData<Boolean>
        get() = _initGame

    fun goToGame() {
        onGameInit()
        onGameInitComplete()
    }

    private fun onGameInit() {
        _initGame.value = true
    }

    private fun onGameInitComplete() {
        _initGame.value = false
    }
}
