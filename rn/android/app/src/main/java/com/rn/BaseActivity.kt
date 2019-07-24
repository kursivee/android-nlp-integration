package com.rn

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.react.ReactInstanceManager
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.kursivee.rn.bridge.nlp.NodeNlpResponse
import com.kursivee.rn.bridge.subscriber.SubscriberManager
import com.rn.adapter.MoshiProvider
import com.rn.list.ChatAdapter
import com.rn.list.ChatMessage
import com.rn.subscriber.NlpSubscriber
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.content_base.*
import kotlinx.coroutines.*
import java.io.File

class BaseActivity : AppCompatActivity() {

    private val subscriberKey = "NAV"
    private val chatLog = mutableListOf(ChatMessage("Welcome", true))
    private val emitter: DeviceEventManagerModule.RCTDeviceEventEmitter by lazy {
        (application as MainApplication)
        .reactNativeHost
            .reactInstanceManager
            .currentReactContext
            ?.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)!!
    }

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
                        rv_chat_log.scrollToPosition((rv_chat_log.adapter as ChatAdapter).itemCount - 1)
                    }
                }
            }
        })
        btn_send.setOnClickListener { view ->
            (rv_chat_log.adapter as ChatAdapter).addMessage(ChatMessage(et_message.text.toString()))
            emitter.emit("text", et_message.text.toString())
            hideKeyboard()
        }
        et_message.setOnEditorActionListener { textView, i, keyEvent ->
            if(EditorInfo.IME_ACTION_DONE == i) {
                (rv_chat_log.adapter as ChatAdapter).addMessage(ChatMessage(et_message.text.toString()))
                emitter.emit("text", et_message.text.toString())
                hideKeyboard()
            }
            true
        }
        rv_chat_log.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            rv_chat_log.scrollToPosition((rv_chat_log.adapter as ChatAdapter).itemCount - 1)
        }
        btn_basic_nlp.setOnClickListener {
            emitter.emit("retrain", loadFromAssets("basic.model.nlp"))
            Toast.makeText(this, "Loading basic nlp", Toast.LENGTH_SHORT).show()
            writeToFile("basic.model.nlp")
        }
        btn_microsoft_nlp.setOnClickListener {
            emitter.emit("retrain", loadFromAssets("microsoft.model.nlp"))
            Toast.makeText(this, "Loading microsoft nlp", Toast.LENGTH_SHORT).show()
            writeToFile("microsoft.model.nlp")
        }

        // Listens for onReactContextInitialized event
        (application as MainApplication).reactNativeHost.reactInstanceManager.addReactInstanceEventListener {
            readFromInternal("model.nlp")?.let {
                    Toast.makeText(this@BaseActivity, "Loading existing nlp model", Toast.LENGTH_SHORT).show()
                emitter.emit("retrain", it)
            }
        }
    }

    private fun loadFromAssets(fileName: String): String {
        return application.assets.open(fileName).bufferedReader().use{
            it.readText()
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.action_empty -> {
                Intent(this, EmptyActivity::class.java).run {
                    startActivity(this)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun writeToFile(filePath: String) {
        applicationContext.openFileOutput("model.nlp", Context.MODE_PRIVATE).use {
            it.write(loadFromAssets(filePath).toByteArray())
        }
    }

    private fun readFromInternal(filePath: String): String? =
        try {
            val file = File(applicationContext.filesDir, filePath)
            file.readText()
        } catch(e: Throwable) {
            null
        }
}
