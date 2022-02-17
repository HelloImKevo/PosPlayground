package com.kevo.posplayground.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import com.kevo.posplayground.R

class MainLaunchActivity : BaseActivity() {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        setContentView(R.layout.main_launch_activity)

        findViewById<View>(R.id.btn_card_acquisition_screen).setOnClickListener {
            startActivity(Intent(this, CardAcquisitionScreen::class.java))
        }

        findViewById<View>(R.id.btn_navigation_bar_override_screen).setOnClickListener {
            startActivity(Intent(this, NavigationBarOverrideScreen::class.java))
        }

        findViewById<View>(R.id.btn_fragment_demo).setOnClickListener {
            startActivity(Intent(this, FragmentDemoActivity::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        System.gc()
    }
}
