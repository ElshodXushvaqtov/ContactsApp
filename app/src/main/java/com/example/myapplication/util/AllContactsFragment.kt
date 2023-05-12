package com.example.myapplication.util

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Notification.Action
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract.Contacts
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.ContactInfoFragment
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentAddContactBinding
import com.example.myapplication.databinding.FragmentAllContactsBinding
import com.example.myapplication.util.adapter.ContactsAdapter
import com.example.myapplication.util.data.ContactsData
import com.example.myapplication.util.database.DBHelper

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AllContactsFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
private lateinit var db_helper:DBHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @SuppressLint("InflateParams")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        view
        val addDialog = AlertDialog.Builder(requireContext())
        val contacts = mutableListOf<ContactsData>()
        val binding = FragmentAllContactsBinding.inflate(layoutInflater, container, false)
        val binding2 = FragmentAddContactBinding.inflate(layoutInflater, container, false)
        var adapter=ContactsAdapter(contacts)
        binding.contactsRv.adapter = adapter
        val name = binding2.addNameInput.text
        val phone_number = binding2.addPhoneNumberInput.text
        binding.addContactBtn.setOnClickListener {
            addDialog.setView(
                LayoutInflater.from(requireContext()).inflate(R.layout.fragment_add_contact, null)
            )
            addDialog.setPositiveButton("Add") { dialog, _ ->
                Log.d("AAA", "$name and $phone_number")
//                db_helper.addContact(contact = Contact(name,phone_number,0))
                contacts.add(ContactsData(name.toString(), phone_number.toString()))
                binding.contactsRv.adapter!!.notifyDataSetChanged()
                dialog.dismiss()
            }
            addDialog.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
                Toast.makeText(requireContext(), "Cancel", Toast.LENGTH_SHORT).show()
            }
            addDialog.create()
            addDialog.show()
        }
        checkPermissions()
        adapter.onItemClick={
            if(it.contact_phone.isNotEmpty()){
                Log.d("BBB", it.contact_phone)
                val callIntent= Intent(Intent.ACTION_CALL)
                callIntent.data= Uri.parse("tel:${it.contact_phone}")
                startActivity(callIntent)
            }
        }
            return binding.root
    }

private fun checkPermissions(){
if(ActivityCompat.checkSelfPermission(requireContext(),android.Manifest.permission.CALL_PHONE)!=PackageManager.PERMISSION_GRANTED )
ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.CALL_PHONE),101)
}
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AllContactsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}