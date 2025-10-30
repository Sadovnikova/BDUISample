package com.example.bduisample.bdui

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.widget.EditText

object UiUtil {
    fun dp(context: Context, v: Int): Int =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, v.toFloat(), context.resources.displayMetrics).toInt()
}

fun EditText.afterTextChanged(after: (String) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) { after(s?.toString().orEmpty()) }
    })
}
