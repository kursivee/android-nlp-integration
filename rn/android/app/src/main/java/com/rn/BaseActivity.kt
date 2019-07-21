package com.rn

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.google.android.material.snackbar.Snackbar
import com.kursivee.rn.bridge.nlp.NodeNlpResponse
import com.kursivee.rn.bridge.subscriber.SubscriberManager
import com.rn.adapter.MoshiProvider
import com.rn.subscriber.NlpSubscriber
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.content_base.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class BaseActivity : AppCompatActivity() {

    private val subscriberKey = "NAV"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        setSupportActionBar(toolbar)
        SubscriberManager.subscribers.getOrPut(subscriberKey, {
            NlpSubscriber().apply {
                callback = {
                    GlobalScope.launch(Dispatchers.Main) {
                        val adapter = MoshiProvider.moshi.adapter(NodeNlpResponse::class.java)
                        tv_message.text = adapter.fromJson(it).toString()
                    }
                }
            }
        })
        fab.setOnClickListener { view ->
            (application as MainApplication)
                    .reactNativeHost
                    .reactInstanceManager
                    .currentReactContext
                    ?.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
                    ?.emit("text", "Hello")
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        SubscriberManager.subscribers.remove(subscriberKey)
    }
}
