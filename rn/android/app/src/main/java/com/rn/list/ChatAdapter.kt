package com.rn.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.rn.R

class ChatAdapter(private val chat: MutableList<ChatMessage>): RecyclerView.Adapter<ChatViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val constraintLayout = LayoutInflater.from(parent.context)
                .inflate(R.layout.chat_log_view, parent, false) as ConstraintLayout
        return ChatViewHolder(constraintLayout)
    }

    override fun getItemCount(): Int = chat.size

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.onBind(chat[position])
    }

    fun addMessage(chatMessage: ChatMessage) {
        chat.add(chatMessage)
        notifyDataSetChanged()
    }
}