package com.kevo.posplayground.ui

import android.os.Bundle
import android.view.View
import com.kevo.posplayground.R

class CardAcquisitionScreen : BaseActivity() {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.card_acquisition_screen)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.title = "SALE"

        hideSystemNavigationBar()
        registerHideSystemUiListener()

        findViewById<View>(R.id.btn_cancel).setOnClickListener {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        System.gc()
    }
}
