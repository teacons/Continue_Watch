package ru.spbstu.icc.kspt.lab2.continuewatch

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private val nameArg = "NAME_ARG"

    var secondsElapsed: Int = 0

    lateinit var service: ScheduledExecutorService

    override fun onPause() {
        super.onPause()
        val pref = getSharedPreferences(nameArg, MODE_PRIVATE)
        val editor = pref.edit()
        editor.putInt(nameArg, secondsElapsed).apply()
        service.shutdown()
    }

    override fun onResume() {
        super.onResume()
        service = Executors.newSingleThreadScheduledExecutor()
        service.scheduleWithFixedDelay({
            textSecondsElapsed.post {
                textSecondsElapsed.text =
                    resources.getString(R.string.text, secondsElapsed++)
            }
        }, 1, 1, TimeUnit.SECONDS)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (secondsElapsed == 0) {
            val pref = getSharedPreferences(nameArg, MODE_PRIVATE)
            secondsElapsed = pref.getInt(nameArg, 0)
        }
        setContentView(R.layout.activity_main)
        textSecondsElapsed.text = resources.getString(R.string.text, secondsElapsed)
    }
}
