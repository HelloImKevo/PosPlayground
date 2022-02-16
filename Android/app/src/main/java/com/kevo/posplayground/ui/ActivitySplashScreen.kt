package com.kevo.posplayground.ui

import android.app.Activity
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import com.kevo.posplayground.MainActivity
import com.kevo.posplayground.R
import java.lang.ref.WeakReference

class ActivitySplashScreen : Activity(), View.OnClickListener {

    companion object {
        private const val SPLASH_TIME: Long = 6 * 1000L

        /**
         * A simple `AsyncTask` extension that sleeps for a brief period, and then
         * instructs the supplied activity to finish itself.
         *
         * @constructor Used to create a new task instance.
         *
         * @param activity Required reference to the splash screen activity. Used to
         * finish the activity, once the sleep time is elapsed.
         */
        class SleepAsyncTask internal constructor(
            activity: ActivitySplashScreen
        ) : AsyncTask<Void?, Void?, Void?>() {

            private val activityRef: WeakReference<ActivitySplashScreen> =
                WeakReference(activity)

            override fun doInBackground(vararg params: Void?): Void? {
                try {
                    Thread.sleep(SPLASH_TIME)
                } catch (ignored: InterruptedException) {
                }
                return null
            }

            override fun onPostExecute(result: Void?) {
                val activity = activityRef.get()

                if (activity == null || activity.isFinishing) {
                    return
                }

                activity.endSplashScreen()
            }
        }
    }

    private lateinit var splashLayout: LinearLayout
    private lateinit var splashScreenDurationTask: AsyncTask<Void?, Void?, Void?>

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val decorView = window?.decorView
        val uiOptions = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN)
        decorView?.systemUiVisibility = uiOptions

        //actionBar?.hide()
        setContentView(R.layout.activity_splash_screen)

        splashLayout = findViewById<View>(R.id.splash_layout) as LinearLayout
        splashLayout.setOnClickListener(this)

        splashScreenDurationTask = SleepAsyncTask(this)
            .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
    }

    override fun onDestroy() {
        super.onDestroy()
        System.gc()
    }

    override fun onClick(v: View?) {
        splashScreenDurationTask.cancel(true)
        endSplashScreen()
    }

    private fun endSplashScreen() {
        if (isFinishing) return

        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
