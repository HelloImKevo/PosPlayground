package com.kevo.posplayground.ui

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import com.kevo.posplayground.R
import com.kevo.posplayground.ui.utils.ActivityUtils

class NavigationBarOverrideScreen : BaseActivity() {

    companion object {
        const val TAG = "NavigationBarOverride"
    }

    /**
     * Lifecycle:
     * onUserInteraction -> onUserLeaveHint -> onPause -> onStop
     */
    var isUserLeavingApp = false

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.navigation_bar_override_screen)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.title = "NAVIGATION OVERRIDE"

        findViewById<View>(R.id.btn_go_back).setOnClickListener {
            finish()
        }
    }

    override fun onPause() {
        super.onPause()

        if (isUserLeavingApp) {
            Toast.makeText(this,
                getString(R.string.navigation_override_home_or_recent_apps_button_message),
                Toast.LENGTH_LONG).show()

            isUserLeavingApp = false

            // Immediately re-launch ourself.
            ActivityUtils.pushToForeground(this, this::class.java)
        }
    }

    override fun onBackPressed() {
        // Override the BACK button behavior (the button on the left).
        Toast.makeText(this,
            getString(R.string.navigation_override_back_button_message), Toast.LENGTH_LONG).show()
    }

    override fun onUserLeaveHint() {
        // The user is leaving our application (via the HOME or RECENT APPS button).
        // We can't prevent it, but we can counteract the behavior by bringing our app
        // back to the foreground as quickly as possible.
        isUserLeavingApp = true
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        Log.i(TAG, "onKeyDown: Key Event: $event")

        // Note: This approach was disabled in Android 4.0 for security reasons.
        // if (KeyEvent.KEYCODE_HOME == keyCode) {
        //     return true
        // }

        return super.onKeyDown(keyCode, event)
    }
}
