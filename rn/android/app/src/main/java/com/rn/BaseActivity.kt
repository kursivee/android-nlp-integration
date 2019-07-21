package com.rn

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
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
                        tv_message.text = adapter.fromJson(it)?.answer
                    }
                }
            }
        })
        btn_send.setOnClickListener { view ->
            (application as MainApplication)
                    .reactNativeHost
                    .reactInstanceManager
                    .currentReactContext
                    ?.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
                    ?.emit("text", et_message.text.toString())
            hideKeyboard()
        }
        et_message.setOnEditorActionListener { textView, i, keyEvent ->
            if(EditorInfo.IME_ACTION_DONE == i) {
                (application as MainApplication)
                        .reactNativeHost
                        .reactInstanceManager
                        .currentReactContext
                        ?.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
                        ?.emit("text", textView.text.toString())
                hideKeyboard()
            }
            true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        SubscriberManager.subscribers.remove(subscriberKey)
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm!!.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
    }
}
