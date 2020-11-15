package br.com.duyllyan.geoquiz

import androidx.annotation.StringRes

data class Question (
        @StringRes
    val textResId: Int,
    val answer: Boolean,
    var result: Boolean? = null
)