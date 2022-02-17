package com.kevo.posplayground.ui

import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

abstract class BaseActivity : AppCompatActivity() {

    protected val hideSystemNavigationBarFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
            View.SYSTEM_UI_FLAG_FULLSCREEN or
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

    protected fun hideSystemNavigationBar() {
        // Ref: https://developer.android.com/training/system-ui/navigation
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            hideSystemNavigationBarHigherApi()
        } else {
            window.decorView.apply {
                // Hide both the navigation bar and the status bar.
                // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher,
                // but as a general rule, you should design your app to hide the status
                // bar whenever you hide the navigation bar.
                systemUiVisibility = hideSystemNavigationBarFlags
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    protected fun hideSystemNavigationBarHigherApi() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(
            window,
            window.decorView.findViewById(android.R.id.content)).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())

            // When the screen is swiped up at the bottom
            // of the application, the navigationBar shall
            // appear for some time.
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    protected fun registerHideSystemUiListener() {
        window.decorView.setOnSystemUiVisibilityChangeListener { visibility ->
            // Note that system bars will only be "visible" if none of the
            // LOW_PROFILE, HIDE_NAVIGATION, or FULLSCREEN flags are set.
            if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
                hideSystemNavigationBar()
            }
        }
    }
}
