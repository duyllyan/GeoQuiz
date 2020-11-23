package br.com.duyllyan.geoquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_main.*

private const val TAG = "MainActivity"

private const val KEY_INDEX = "index"

class MainActivity : AppCompatActivity() {

    private val quizViewModel : QuizViewModel by lazy {
        ViewModelProvider(this).get(QuizViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        quizViewModel.currentIndex = currentIndex

        true_button.isEnabled = quizViewModel.checkResult()
        false_button.isEnabled = quizViewModel.checkResult()

        true_button.setOnClickListener {
            checkAnswer(true)
            it.isEnabled = false
            false_button.isEnabled = false
        }
        false_button.setOnClickListener {
            checkAnswer(false)
            it.isEnabled = false
            true_button.isEnabled = false
        }
        next_button.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
            buttonEnabledToggle()
        }
        previous_button.setOnClickListener {
            quizViewModel.moveToPrev()
            updateQuestion()
            buttonEnabledToggle()
        }
        question_text_view.setOnClickListener {
            quizViewModel.moveToNext()
        }
        updateQuestion()
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(TAG, "onSaveInstanceState")
        savedInstanceState.putInt(KEY_INDEX, quizViewModel.currentIndex)
    }
    
    private fun updateQuestion() {
        question_text_view.setText(quizViewModel.currentQuestionText)
    }

    private fun checkAnswer (userAnswer: Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer
        val result = userAnswer == correctAnswer
        if (quizViewModel.checkResult()) {
            quizViewModel.setResult(result)
        }
        val messageResId = if (result) {
            R.string.correct_toast
        } else {
            R.string.incorrect_toast
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()

        if (quizViewModel.checkComplete()) {
            Toast.makeText(this, resources.getString(R.string.performance, quizViewModel.performance()), Toast.LENGTH_SHORT).show()
        }
    }

    private fun buttonEnabledToggle() {
        true_button.isEnabled = quizViewModel.checkResult()
        false_button.isEnabled = quizViewModel.checkResult()
    }
}