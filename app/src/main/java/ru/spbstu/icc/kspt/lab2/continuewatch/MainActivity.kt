package ru.spbstu.icc.kspt.lab2.continuewatch

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val nameArg = "NAME_ARG"

    var secondsElapsed = 0

    lateinit var task: AsyncTask<Void, Int, Void>

    override fun onPause() {
        super.onPause()
        task.cancel(true)
        val pref = getSharedPreferences(nameArg, MODE_PRIVATE)
        val editor = pref.edit()
        editor.putInt(nameArg, secondsElapsed).apply()
    }

    override fun onResume() {
        super.onResume()
        val pref = getSharedPreferences(nameArg, MODE_PRIVATE)
        secondsElapsed = pref.getInt(nameArg, 0)
        textSecondsElapsed.text = resources.getString(R.string.text, secondsElapsed)
        task = MyTask()
        task.execute()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    @SuppressLint("StaticFieldLeak")
    inner class MyTask : AsyncTask<Void, Int, Void>() {
        override fun doInBackground(vararg params: Void?): Void? {
            var time = System.currentTimeMillis() + 1000
            while (!isCancelled) {
                if (time == System.currentTimeMillis()) {
                    secondsElapsed++
                    publishProgress(secondsElapsed)
                    time = System.currentTimeMillis() + 1000
                }
            }
            return null
        }

        override fun onProgressUpdate(vararg values: Int?) {
            super.onProgressUpdate(*values)
            textSecondsElapsed.text = resources.getString(R.string.text, values[0])
        }
    }
}
