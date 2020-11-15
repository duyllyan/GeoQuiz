package br.com.duyllyan.geoquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private val questionBank = listOf<Question>(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )

    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

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
            nextQuestion()
            buttonEnabledToggle()
        }
        previous_button.setOnClickListener {
            previousQuestion()
            buttonEnabledToggle()
        }
        question_text_view.setOnClickListener {
            nextQuestion()
        }
        updateQuestion()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: called")
    }
    
    private fun updateQuestion() {
        question_text_view.setText(questionBank[currentIndex].textResId)
    }

    private fun nextQuestion() {
        currentIndex = (currentIndex + 1) % questionBank.size
        updateQuestion()
    }
    private fun previousQuestion() {
        currentIndex = if (currentIndex > 0) {
            (currentIndex - 1) % questionBank.size
        } else {
            questionBank.size - 1
        }
        updateQuestion()
    }

    private fun checkAnswer (userAnswer: Boolean) {
        val correctAnswer = questionBank[currentIndex].answer
        val result = userAnswer == correctAnswer
        if (checkResult()) {
            questionBank[currentIndex].result = result
        }
        val messageResId = if (result) {
            R.string.correct_toast
        } else {
            R.string.incorrect_toast
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
        val performance: Int = (questionBank.count { it.result == true }/questionBank.size) * 100
        if (checkComplete()) {
            Toast.makeText(this, resources.getString(R.string.performance, performance), Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkResult(): Boolean {
        return questionBank[currentIndex].result == null
    }

    private fun checkComplete(): Boolean {
        return !questionBank.any {it.result == null}
    }

    private fun buttonEnabledToggle() {
        true_button.isEnabled = checkResult()
        false_button.isEnabled = checkResult()
    }
}