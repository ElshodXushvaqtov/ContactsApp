package com.example.myapplication.util.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.util.data.ContactsData

class ContactsAdapter(var contacts: MutableList<ContactsData>, var context: Context) :
    RecyclerView.Adapter<ContactsAdapter.MyHolder>() {

    var onItemClick: ((ContactsData) -> Unit)? = null

    inner class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var contact_name: TextView
        var contact_phone: TextView
        var menu: ImageView

        init {
            contact_name = itemView.findViewById(R.id.contact_full_name)
            contact_phone = itemView.findViewById(R.id.contact_phone_number)
            menu = itemView.findViewById(R.id.edit_contact)
            menu.setOnClickListener {
                popupMenu(it)
            }
        }

        private fun popupMenu(v: View) {
            val position = contacts[adapterPosition]

            val popupMenu = PopupMenu(context.applicationContext, v)
            popupMenu.inflate(R.menu.show_menu)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.editContact -> {
                        val v =
                            LayoutInflater.from(context).inflate(R.layout.fragment_add_contact, null)
                        val name = v.findViewById<TextView>(R.id.add_name_input)
                        val phone_number = v.findViewById<TextView>(R.id.add_phone_number_input)
                        AlertDialog.Builder(context).setView(v)
                            .setPositiveButton("Edit") { dialog, _ ->
                                position.contact_name = name.text.toString()
                                position.contact_phone = phone_number.text.toString()
                                notifyDataSetChanged()
                                Toast.makeText(context, "Changed", Toast.LENGTH_SHORT).show()
                                dialog.dismiss()
                            }.setNegativeButton("Cancel") { dialog, _ ->
                                Toast.makeText(context, "Cancel", Toast.LENGTH_SHORT).show()
                                dialog.dismiss()
                            }.create().show()

                        true
                    }

                    R.id.delete_contact -> {

                        AlertDialog.Builder(context).setTitle("Delete")
                            .setIcon(R.drawable.baseline_warning_24)
                            .setMessage("Are you sure delete this contact?!")
                            .setPositiveButton("Yes") { dialog, _ ->
                                contacts.removeAt(adapterPosition)
                                notifyDataSetChanged()
                                dialog.dismiss()
                                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show()
                            }.setNegativeButton("No") { dialog, _ ->
                                dialog.dismiss()
                                Toast.makeText(context, "No", Toast.LENGTH_SHORT).show()

                            }
                            .create()
                            .show()
                        true
                    }

                    else -> true
                }
            }
            popupMenu.show()
//            val popup = PopupMenu::class.java.getDeclaredField("menuPopup")
//            popup.isAccessible = true
//            val menu = popup.get(popupMenu)
//            menu.javaClass.getDeclaredMethod("setForceShowIcon", Boolean::class.java)
//                .invoke(menu,true)
        }
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