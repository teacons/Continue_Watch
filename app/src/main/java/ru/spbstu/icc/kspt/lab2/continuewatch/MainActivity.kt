package ru.spbstu.icc.kspt.lab2.continuewatch

import android.os.AsyncTask
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val nameArg = "NAME_ARG"

    lateinit var task: AsyncTask<Void, Int, Void>

    override fun onPause() {
        super.onPause()
        task.cancel(true)
    }

    override fun onResume() {
        super.onResume()
        val pref = getSharedPreferences(nameArg, MODE_PRIVATE)
        val secondsElapsed = pref.getInt(nameArg, 0)
        textSecondsElapsed.text = resources.getString(R.string.text, secondsElapsed)
        task = MyTask(this, secondsElapsed)
        task.execute()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    class MyTask(private val context: MainActivity, private var secondsElapsed: Int) :
        AsyncTask<Void, Int, Void>() {
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
            context.apply {
                textSecondsElapsed.text = resources.getString(R.string.text, values[0])
            }
        }

        override fun onCancelled() {
            super.onCancelled()
            context.apply {
                val pref = getSharedPreferences(nameArg, MODE_PRIVATE)
                val editor = pref.edit()
                editor.putInt(nameArg, secondsElapsed).apply()
            }
        }
    }
}
