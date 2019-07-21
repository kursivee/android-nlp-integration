package com.rn.list

import android.drm.DrmStore
import android.text.Layout
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.rn.R
import kotlinx.android.synthetic.main.chat_log_view.view.*
import org.w3c.dom.Text

class ChatViewHolder(private val layout: ConstraintLayout): RecyclerView.ViewHolder(layout) {
    val textView: TextView = layout.findViewById(R.id.tv_chat_message)

    fun onBind(chatMessage: ChatMessage) {
        textView.text = chatMessage.message
        if(chatMessage.isResponse) {
            textView.gravity = Gravity.START
        } else {
            textView.gravity = Gravity.END
        }
    }
}