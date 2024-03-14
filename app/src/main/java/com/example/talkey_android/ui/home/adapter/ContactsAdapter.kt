package com.example.talkey_android.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.talkey_android.R
import com.example.talkey_android.data.domain.model.users.UserItemListModel
import com.example.talkey_android.databinding.ItemRecyclerviewUserBinding

class ContactsAdapter(
    private val context: Context,
    private val listener: CellListener,
    private val contacts: MutableList<UserItemListModel> = mutableListOf()
) : RecyclerView.Adapter<ContactsAdapter.ViewHolder>() {

    interface CellListener {
        fun onContactClick(token: String)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemRecyclerviewUserBinding.bind(view)
        fun setListener(token: String) {
            binding.root.setOnClickListener {
                listener.onContactClick(token)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_recyclerview_user, parent, false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding) {
            tvName.text = contacts[position].nick
            tvDate.text = contacts[position].created
            if (contacts[position].online) {
                imgOnline.setBackgroundColor(ContextCompat.getColor(context, R.color.statusOffline))
            } else {
                imgOnline.setBackgroundColor(ContextCompat.getColor(context, R.color.statusOnline))
            }
            // TODO: falta cargar la imagen del usuario
        }
    }

    override fun getItemCount() = contacts.count()
}