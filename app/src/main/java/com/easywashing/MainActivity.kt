package com.easywashing

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    fun openAgora(view: View) {
        DynamicFeatureManager.getInstance(this@MainActivity)
            .launch(DynamicFeatureManager.DYNAMIC_FEATURE_VIDEO_TOUR)
    }
}