package com.example.bduisample.bdui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

class ActionExecutor(private val context: Context) {
    fun run(action: Action) {
        when (action) {
            is Action.Toast -> Toast.makeText(context, action.message, Toast.LENGTH_SHORT).show()
            is Action.OpenUrl -> context.startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse(action.url)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        }
    }
}
