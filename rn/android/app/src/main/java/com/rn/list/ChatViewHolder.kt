package com.rn.list

import android.content.res.ColorStateList
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.rn.R
import android.view.View


class ChatViewHolder(private val layout: LinearLayout): RecyclerView.ViewHolder(layout) {
    val textView: TextView = layout.findViewById(R.id.tv_chat_message)

    fun onBind(chatMessage: ChatMessage) {
        textView.text = chatMessage.message
        if(chatMessage.isResponse) {
            layout.gravity = Gravity.START
            textView.backgroundTintList = ColorStateList.valueOf(layout.context.resources.getColor(R.color.blue, layout.context.theme))
        } else {
            layout.gravity = Gravity.END
            textView.backgroundTintList = ColorStateList.valueOf(layout.context.resources.getColor(R.color.gray, layout.context.theme))

        }
    }
}