package br.com.duyllyan.geoquiz

import android.util.Log
import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"

class QuizViewModel : ViewModel() {
    /*init {
        Log.d(TAG, "ViewModel instance created")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "ViewModel instance about to be destroyed")
    }*/

    private val questionBank = listOf<Question>(
            Question(R.string.question_australia, true),
            Question(R.string.question_oceans, true),
            Question(R.string.question_mideast, false),
            Question(R.string.question_africa, false),
            Question(R.string.question_americas, true),
            Question(R.string.question_asia, true)
    )

    var currentIndex = 0

    val currentQuestionAnswer : Boolean get() = questionBank[currentIndex].answer
    val currentQuestionText : Int get() = questionBank[currentIndex].textResId

    fun performance() : Float {
       return ((countCorrect() / questionBank.size) * 100f)
    }

    private fun countCorrect() = questionBank.count { it.result == true } * 1f

    fun getResult() : Boolean? {
        return questionBank[currentIndex].result
    }

    fun setResult(result : Boolean?) {
        questionBank[currentIndex].result = result
    }

    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    fun moveToPrev() {
        currentIndex = if (currentIndex > 0) {
            (currentIndex - 1) % questionBank.size
        } else {
            questionBank.size - 1
        }
    }

    fun checkResult(): Boolean {
        return questionBank[currentIndex].result == null
    }

    fun checkComplete(): Boolean {
        return !questionBank.any {it.result == null}
    }
}