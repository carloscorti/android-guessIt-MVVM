package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

// These represent different important times
// This is when the game is over
private const val DONE = 0L
// This is when game is about to finish
private const val PANIC_TIME = 5000L
// This is the number of milliseconds in a second
private const val ONE_SECOND = 1000L
// This is the total time of the game
private const val COUNTDOWN_TIME = 90000L

// buzz vibration setups
private val CORRECT_BUZZ_PATTERN = longArrayOf(100, 100, 100, 100, 100, 100)
private val PANIC_BUZZ_PATTERN = longArrayOf(0, 200)
private val GAME_OVER_BUZZ_PATTERN = longArrayOf(0, 2000)
private val NO_BUZZ_PATTERN = longArrayOf(0)

// vibrations BuzzType
enum class BuzzType(val pattern: LongArray) {
    CORRECT(CORRECT_BUZZ_PATTERN),
    GAME_OVER(GAME_OVER_BUZZ_PATTERN),
    COUNTDOWN_PANIC(PANIC_BUZZ_PATTERN),
    NO_BUZZ(NO_BUZZ_PATTERN)
}

class GameViewModel : ViewModel() {

    private lateinit var countDownTimer: CountDownTimer

    private val _timeCount = MutableLiveData<Long>()
//    private val timeCount: LiveData<Long>
//        get() = _timeCount

    private val _timeCountString = Transformations.map(_timeCount) { time ->
        DateUtils.formatElapsedTime(time)
    }
    val timeCountString: LiveData<String>
        get() = _timeCountString

    // The current word
    private val _word = MutableLiveData<String>()
    val word: LiveData<String>
        get() = _word

    // The current score
    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score

    private val _finish = MutableLiveData<Boolean>()
    val finishTrigger: LiveData<Boolean>
        get() = _finish

    private val _buzzType = MutableLiveData<BuzzType>()
    val buzzType: LiveData<BuzzType>
        get() = _buzzType

    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>

    init {
//        Log.i("GameViewModel", "GameViewModel created!")
        resetList()
        nextWord()
        _score.value = 0
        _finish.value = false
        startTimer()
    }

    override fun onCleared() {
        super.onCleared()
//        Log.i("GameViewModel", "GameViewModel destroyed!")
        pauseTimer()
    }


    /**
     * Resets the list of words and randomizes the order
     */
    private fun resetList() {
        wordList = mutableListOf(
                "queen",
                "hospital",
                "basketball",
                "cat",
                "change",
                "snail",
                "soup",
                "calendar",
                "sad",
                "desk",
                "guitar",
                "home",
                "railway",
                "zebra",
                "jelly",
                "car",
                "crow",
                "trade",
                "bag",
                "roll",
                "bubble"
        )
        wordList.shuffle()
    }

    /** Methods for buttons presses **/

    fun onSkip() {
        _score.value = score.value?.minus(1)
        nextWord()
    }

    fun onCorrect() {
        _score.value = score.value?.plus(1)
        buzzCommand(BuzzType.CORRECT)
        nextWord()
    }

    /**
     * Moves to the next word in the list
     */
    private fun nextWord() {
        //Select and remove a word from the list
        if (wordList.isEmpty()) {
            resetList()
        }
        _word.value = wordList.removeAt(0)

    }

    private fun onGamaFinishedComplete() {
        _finish.value = false
    }


    private fun startTimer(topTime: Long = COUNTDOWN_TIME) {
        countDownTimer = object : CountDownTimer(topTime, ONE_SECOND) {
            override fun onFinish() {
                _finish.value = true
                _timeCount.value = DONE
                buzzCommand(BuzzType.GAME_OVER)
                onGamaFinishedComplete()
            }

            override fun onTick(millisUntilFinished: Long) {
                _timeCount.value = (millisUntilFinished / ONE_SECOND)
                if (millisUntilFinished < PANIC_TIME) buzzCommand(BuzzType.COUNTDOWN_PANIC)
            }

        }
        countDownTimer.start()
//        Log.i("GameViewModel", "start timer from startTimer")
    }

    private fun pauseTimer() {
//        Log.i("GameViewModel","timer stop")
        countDownTimer.cancel()
    }

    private fun buzzCommand (buzzType : BuzzType){
        _buzzType.value = buzzType
        _buzzType.value = BuzzType.NO_BUZZ
    }
}