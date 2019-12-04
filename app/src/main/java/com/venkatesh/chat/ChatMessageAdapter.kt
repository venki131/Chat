package com.venkatesh.chat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseListAdapter
import kotlinx.android.synthetic.main.chat_row_item.view.*

class ChatMessageAdapter(
    val chatItems: ArrayList<FirebaseListAdapter<ChatMessage>>,
    val context: Context
) : RecyclerView.Adapter<ChatMessageAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //val userName = view.txtUserName
        val message = view.txtMessageReceiving
        val time = view.txtMsgTime
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.chat_row_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return chatItems.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //holder?.userName?.text = chatItems.get(position).getRef(position).key.
    }
}