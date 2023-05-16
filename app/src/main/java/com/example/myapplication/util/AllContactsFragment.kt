package com.example.myapplication.util

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.myapplication.ContactInfoFragment
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentAddContactBinding
import com.example.myapplication.databinding.FragmentAllContactsBinding
import com.example.myapplication.util.adapter.ContactsAdapter
import com.example.myapplication.util.data.ContactsData
import com.example.myapplication.util.database.DBHelper
import com.example.myapplication.util.module.Contact

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AllContactsFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var db_helper: DBHelper
    private lateinit var binding: FragmentAllContactsBinding
    private lateinit var binding2: FragmentAddContactBinding
    private lateinit var contacts: MutableList<ContactsData>
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
        db_helper = DBHelper(requireContext())
        contacts = mutableListOf()
        binding = FragmentAllContactsBinding.inflate(layoutInflater, container, false)
        binding2 = FragmentAddContactBinding.inflate(layoutInflater, container, false)
        var adapter = ContactsAdapter(contacts,requireContext())
        binding.contactsRv.adapter = adapter
        binding.addContactBtn.setOnClickListener {
            addContact()
        }
        checkPermissions()
        adapter.onItemClick = {
            parentFragmentManager.beginTransaction().replace(
                R.id.fragmentContainerView,
                ContactInfoFragment.newInstance(it.contact_name, it.contact_phone)
            ).commit()
        }
        return binding.root
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

    private fun addContact() {
        val addDialog = AlertDialog.Builder(requireContext())
        addDialog.setView(
            LayoutInflater.from(requireContext()).inflate(R.layout.fragment_add_contact, null)
        )
        addDialog.setPositiveButton("Add") { dialog, _ ->
            val name = binding2.addNameInput.text.toString()
            val phone_number = binding2.addPhoneNumberInput.text.toString()
            db_helper.addContact(Contact(name, phone_number))
            contacts.add(ContactsData(name, phone_number))
            binding.contactsRv.adapter?.notifyDataSetChanged()
            Log.d("AAA", "$name and $phone_number")
            dialog.dismiss()
            Toast.makeText(requireContext(), "Added", Toast.LENGTH_SHORT).show()
        }
        addDialog.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
            Toast.makeText(requireContext(), "Cancel", Toast.LENGTH_SHORT).show()
        }
        addDialog.create()
        addDialog.show()
    }

    private fun checkPermissions() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        )
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.CALL_PHONE),
                101
            )
    }
}