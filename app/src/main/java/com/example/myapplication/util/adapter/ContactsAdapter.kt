package com.example.myapplication.util.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.util.data.ContactsData

class ContactsAdapter(var contacts: MutableList<ContactsData>) :
    RecyclerView.Adapter<ContactsAdapter.MyHolder>() {

    var onItemClick: ((ContactsData) -> Unit)? = null

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var contact_name = itemView.findViewById<TextView>(R.id.contact_full_name)
        var contact_phone = itemView.findViewById<TextView>(R.id.contact_phone_number)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.contact_item_layout, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        var index = contacts[position]
        index.contact_name = holder.contact_name.toString()
        index.contact_phone = holder.contact_phone.toString()
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(index)
        }
    }
//    interface onItemClickListener{
//        fun onItemClick(position: Int)
//    }
}