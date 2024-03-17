package com.example.talkey_android.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.talkey_android.R
import com.example.talkey_android.data.domain.model.chats.ChatItemListModel
import com.example.talkey_android.data.domain.model.users.UserItemListModel
import com.example.talkey_android.databinding.ItemRecyclerviewUserBinding

class ContactsAdapter(
    private val context: Context,
    private val listener: CellListener,
    private val id: String,
    private var list: List<Any> = listOf()
) : RecyclerView.Adapter<ContactsAdapter.UsersViewHolder>() {

    interface CellListener {
        fun onContactClick(token: String)
    }

    private val contactType = 1
    private val chatType = 2

    inner class UsersViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemRecyclerviewUserBinding.bind(view)

        // TODO: think about listener
        fun setListener(token: String) {
            binding.root.setOnClickListener {
                listener.onContactClick(token)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            list[position] is UserItemListModel -> contactType
            list[position] is ChatItemListModel -> chatType
            else -> throw IllegalArgumentException("Tipo de elemento desconocido en la posición $position")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_recyclerview_user, parent, false
        )
        return UsersViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        when (holder.itemViewType) {
            contactType -> showContactData(holder, position)
            chatType -> showChatData(holder, position)
        }
    }

    private fun showChatData(holder: UsersViewHolder, position: Int) {
        val chatItemModel = list[position] as (ChatItemListModel)

        with(holder.binding) {
            tvName.text = chatItemModel.contactNick
            tvDate.text = chatItemModel.dateLastMessage
            tvLastMsg.text = chatItemModel.lastMessage
            if (chatItemModel.contactOnline) {
                imgOnline.setBackgroundColor(ContextCompat.getColor(context, R.color.statusOffline))
            } else {
                imgOnline.setBackgroundColor(ContextCompat.getColor(context, R.color.statusOnline))
            }
            Glide.with(context)
                .load(chatItemModel.contactAvatar)
                .error(R.drawable.perfil)
                .apply(RequestOptions().centerCrop())
                .into(imgProfile)
        }
    }


    private fun showContactData(holder: UsersViewHolder, position: Int) {
        val user = list[position] as (UserItemListModel)
        with(holder.binding) {
            tvName.text = user.nick
            tvDate.text = ""
            tvLastMsg.text = "Dile algo a " + user.nick + "!"
            if (user.online) {
                imgOnline.setBackgroundColor(ContextCompat.getColor(context, R.color.statusOffline))
            } else {
                imgOnline.setBackgroundColor(ContextCompat.getColor(context, R.color.statusOnline))
            }
            Glide.with(context)
                .load(user.avatar)
                .error(R.drawable.perfil)
                .apply(RequestOptions().centerCrop())
                .into(imgProfile)
        }
    }

    override fun getItemCount() = list.count()
    fun refreshData(newList: List<Any>) {
        list = newList
        notifyDataSetChanged()
    }
}