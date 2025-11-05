package com.example.bduisample

import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import app.cash.paparazzi.Paparazzi
import org.junit.Rule
import org.junit.Test

class LoginScreenSnapshotTest {

    @get:Rule
    val paparazzi = Paparazzi()

    @Test
    fun loginScreen_snapshot() {
        val context = paparazzi.context
        val root = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.WHITE)
            setPadding(48, 64, 48, 64)
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        val title = TextView(context).apply {
            text = "Вход в аккаунт"
            textSize = 20f
            setTextColor(Color.BLACK)
            gravity = Gravity.START
        }

        val email = EditText(context).apply {
            hint = "Email"
            setPadding(24, 24, 24, 24)
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply { topMargin = 32 }
        }

        val password = EditText(context).apply {
            hint = "Пароль"
            setPadding(24, 24, 24, 24)
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply { topMargin = 16 }
        }

        val button = Button(context).apply {
            text = "Войти"
            setBackgroundColor(Color.parseColor("#6651D4"))
            setTextColor(Color.WHITE)
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = 24
            }
        }

        root.addView(title)
        root.addView(email)
        root.addView(password)
        root.addView(button)

        paparazzi.snapshot(root)
    }
}
