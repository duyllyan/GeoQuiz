package br.com.duyllyan.geoquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_cheat.*

private const val EXTRA_ANSWER_IS_TRUE = "br.com.duyllyan.geoquiz.answer_is_true"
const val EXTRA_ANSWER_SHOWN = "br.com.duyllyan.geoquiz.answer_shown"
private const val CHEAT_ENABLED = "br.com.duyllyan.geoquiz.cheat_enabled"

class CheatActivity : AppCompatActivity() {

    private var answerIsTrue = false

    private var wasCheated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)
        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)
        wasCheated = savedInstanceState?.getBoolean(CHEAT_ENABLED, false) ?: false
        if (wasCheated) {
            cheatEnabled()
        }
        show_answer_button.setOnClickListener {
            cheatEnabled()
            wasCheated = true
        }
    }

    private fun cheatEnabled() {
        val answerText = when {
            answerIsTrue -> R.string.true_button
            else -> R.string.false_button
        }
        answer_text_view.setText(answerText)
        setAnswerShownResult(true)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(CHEAT_ENABLED, wasCheated)
    }

    private fun setAnswerShownResult(isAnswerShown: Boolean) {
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown)
        }
        setResult(Activity.RESULT_OK, data)
    }

    companion object {
        fun newIntent(packageContext: Context, answerIsTrue: Boolean): Intent {
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
            }
        }
    }
}