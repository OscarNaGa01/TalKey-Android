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
import com.example.talkey_android.data.domain.model.chats.ChatBasicInfoModel
import com.example.talkey_android.data.domain.model.chats.ChatModel
import com.example.talkey_android.data.domain.model.users.UserItemListModel
import com.example.talkey_android.databinding.ItemRecyclerviewUserBinding

class ContactsAdapter(
    private val context: Context,
    private val listener: CellListener,
    private var contacts: List<Any> = listOf()
) : RecyclerView.Adapter<ContactsAdapter.UsersViewHolder>() {

    interface CellListener {
        fun onContactClick(token: String)
    }

    private val contactType = 1
    private val chatType = 2

    inner class UsersViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemRecyclerviewUserBinding.bind(view)
        fun setListener(token: String) {
            binding.root.setOnClickListener {
                listener.onContactClick(token)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            contacts[position] is UserItemListModel -> contactType
            contacts[position] is ChatBasicInfoModel -> chatType

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
        val user = contacts[position] as (ChatModel)
        with(holder.binding) {
            tvName.text = user.targetNick
            tvDate.text = user.chatCreated
            tvLastMsg.text = "Último mensaje enviado"
            if (user.targetOnline) {
                imgOnline.setBackgroundColor(ContextCompat.getColor(context, R.color.statusOffline))
            } else {
                imgOnline.setBackgroundColor(ContextCompat.getColor(context, R.color.statusOnline))
            }
            Glide.with(context)
                .load(user.targetAvatar)
                .error(R.drawable.perfil)
                .apply(RequestOptions().centerCrop())
                .into(imgProfile)
        }
    }

    private fun showContactData(holder: UsersViewHolder, position: Int) {
        val user = contacts[position] as (UserItemListModel)
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

    override fun getItemCount() = contacts.count()
    fun refreshData(newList: List<Any>) {
        contacts = newList
        notifyDataSetChanged()
    }
}