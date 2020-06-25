package com.easywashing.dynamic

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.RtcEngine

class MyDynamicActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_dynamic)
        RtcEngine.create(baseContext, "e3a9afca70b14b6abd1ef64789ba557f", object :
            IRtcEngineEventHandler() {})
    }
}