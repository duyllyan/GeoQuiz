package br.com.duyllyan.geoquiz

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_main.*

private const val TAG = "MainActivity"

private const val KEY_INDEX = "index"

private const val REQUEST_CODE_CHEAT = 0

class MainActivity : AppCompatActivity() {

    private val quizViewModel : QuizViewModel by lazy {
        ViewModelProvider(this).get(QuizViewModel::class.java)
    }

    @SuppressLint("NewApi")
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
        cheat_button.setOnClickListener { view ->
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val options =
                    ActivityOptions.makeClipRevealAnimation(view, 0, 0, view.width, view.height)
                startActivityForResult(intent, REQUEST_CODE_CHEAT, options.toBundle())
            } else {
                startActivityForResult(intent, REQUEST_CODE_CHEAT)
            }
        }
        updateQuestion()
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putInt(KEY_INDEX, quizViewModel.currentIndex)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            quizViewModel.isCheater = data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
        }
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
        val messageResId = when {
            quizViewModel.isCheater -> R.string.judgment_toast
            userAnswer == correctAnswer -> R.string.correct_toast
            else -> R.string.incorrect_toast
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