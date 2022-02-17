package com.kevo.posplayground.ui

import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.kevo.posplayground.R
import java.lang.ref.WeakReference

class SplashScreenActivity : BaseActivity() {

    companion object {
        private const val SPLASH_TIME: Long = 12 * 1000L

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
            activity: SplashScreenActivity
        ) : AsyncTask<Void?, Void?, Void?>() {

            private val activityRef: WeakReference<SplashScreenActivity> =
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

    private lateinit var splashScreenDurationTask: AsyncTask<Void?, Void?, Void?>

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        setContentView(R.layout.splash_screen_activity)

        hideSystemNavigationBar()
        registerHideSystemUiListener()

        findViewById<View>(R.id.btn_skip_timer).setOnClickListener {
            splashScreenDurationTask.cancel(true)
            endSplashScreen()
        }

        splashScreenDurationTask = SleepAsyncTask(this)
            .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
    }

    override fun onDestroy() {
        super.onDestroy()
        System.gc()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && hasFocus) {
            window.decorView.systemUiVisibility = hideSystemNavigationBarFlags
        }
    }

    private fun endSplashScreen() {
        if (isFinishing) return

        startActivity(Intent(this, MainLaunchActivity::class.java))
        finish()
    }
}
