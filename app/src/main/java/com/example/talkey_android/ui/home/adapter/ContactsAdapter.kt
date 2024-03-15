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
import com.example.talkey_android.data.domain.model.users.UserItemListModel
import com.example.talkey_android.databinding.ItemRecyclerviewUserBinding

class ContactsAdapter(
    private val context: Context,
    private val listener: CellListener,
    private var contacts: List<UserItemListModel> = listOf()
) : RecyclerView.Adapter<ContactsAdapter.UsersViewHolder>() {

    interface CellListener {
        fun onContactClick(token: String)
    }

    inner class UsersViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemRecyclerviewUserBinding.bind(view)
        fun setListener(token: String) {
            binding.root.setOnClickListener {
                listener.onContactClick(token)
            }
        }
    }

    inner class ChatsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemRecyclerviewUserBinding.bind(view)
        fun setListener(token: String) {
            binding.root.setOnClickListener {
                listener.onContactClick(token)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_recyclerview_user, parent, false
        )
        return UsersViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        with(holder.binding) {
            tvName.text = contacts[position].nick
            tvDate.text = contacts[position].created
            tvLastMsg.text = "Dile algo a " + contacts[position].nick + "!"
            if (contacts[position].online) {
                imgOnline.setBackgroundColor(ContextCompat.getColor(context, R.color.statusOffline))
            } else {
                imgOnline.setBackgroundColor(ContextCompat.getColor(context, R.color.statusOnline))
            }
            Glide.with(context)
                .load(contacts[position].avatar)
                .error(R.drawable.perfil)
                .apply(RequestOptions().centerCrop())
                .into(imgProfile)

        }
    }

    override fun getItemCount() = contacts.count()
    fun refreshData(newList: List<UserItemListModel>) {
        contacts = newList
        notifyDataSetChanged()
    }
}