package com.rn

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.kursivee.rn.bridge.nlp.NodeNlpResponse
import com.kursivee.rn.bridge.subscriber.SubscriberManager
import com.rn.adapter.MoshiProvider
import com.rn.list.ChatAdapter
import com.rn.list.ChatMessage
import com.rn.list.ChatViewHolder
import com.rn.subscriber.NlpSubscriber
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.content_base.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BaseActivity : AppCompatActivity() {

    private val subscriberKey = "NAV"
    private val chatLog = mutableListOf(ChatMessage("Welcome", true))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        setSupportActionBar(toolbar)
        rv_chat_log.apply {
            layoutManager = LinearLayoutManager(this@BaseActivity)
            adapter = ChatAdapter(chatLog)
        }
        SubscriberManager.subscribers.getOrPut(subscriberKey, {
            NlpSubscriber().apply {
                callback = {
                    GlobalScope.launch(Dispatchers.Main) {
                        val adapter = MoshiProvider.moshi.adapter(NodeNlpResponse::class.java)
                        (rv_chat_log.adapter as ChatAdapter).addMessage(
                                ChatMessage(adapter.fromJson(it)?.answer ?: "Sorry, unrecognized message", true)
                        )
                    }
                }
            }
        })
        btn_send.setOnClickListener { view ->
            (rv_chat_log.adapter as ChatAdapter).addMessage(ChatMessage(et_message.text.toString()))
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
                (rv_chat_log.adapter as ChatAdapter).addMessage(ChatMessage(et_message.text.toString()))
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
        et_message.text.clear()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
    }
}
