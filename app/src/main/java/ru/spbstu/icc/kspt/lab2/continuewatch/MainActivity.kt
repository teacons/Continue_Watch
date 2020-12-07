package ru.spbstu.icc.kspt.lab2.continuewatch

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.coroutineScope
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private val nameArg = "NAME_ARG"

    var secondsElapsed: Int = 0

    private var coroutineJob: Job? = null

    override fun onPause() {
        super.onPause()
        coroutineJob!!.cancel()
        val pref = getSharedPreferences(nameArg, MODE_PRIVATE)
        val editor = pref.edit()
        editor.putInt(nameArg, secondsElapsed).apply()
    }

    override fun onResume() {
        super.onResume()
        coroutineJob = lifecycle.coroutineScope.launchWhenResumed {
            withContext(Dispatchers.Default) {
                var time = System.currentTimeMillis() + 1000
                while (isActive) {
                    if (System.currentTimeMillis() == time) {
                        textSecondsElapsed.post {
                            textSecondsElapsed.text =
                                resources.getString(R.string.text, secondsElapsed++)
                        }
                        time = System.currentTimeMillis() + 1000
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pref = getSharedPreferences(nameArg, MODE_PRIVATE)
        secondsElapsed = pref.getInt(nameArg, 0)
        setContentView(R.layout.activity_main)
        textSecondsElapsed.text = resources.getString(R.string.text, secondsElapsed)
    }
}
