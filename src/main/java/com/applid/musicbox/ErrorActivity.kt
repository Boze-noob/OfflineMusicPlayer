package com.applid.musicbox

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.applid.musicbox.ui.components.ErrorComp

class ErrorActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val errorMessage = intent?.extras?.getString(error_message_key) ?: "Unknown"
        val errorStackTrace = intent?.extras?.getString(error_stack_trace_key) ?: "-"

        setContent {
            ErrorComp(errorMessage, errorStackTrace)
        }
    }

    companion object {
        const val error_message_key = "error_message"
        const val error_stack_trace_key = "error_stack_trace"

        fun start(context: Context, error: Throwable) {
            val intent = Intent(context, ErrorActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_TASK_ON_HOME
            intent.putExtra(error_message_key, error.toString())
            intent.putExtra(error_stack_trace_key, error.stackTraceToString())
            context.startActivity(intent)
        }
    }
}
