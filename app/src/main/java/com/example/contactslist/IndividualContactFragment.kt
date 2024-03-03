package com.example.contactslist

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.contactslist.databinding.FragmentIndividualContactBinding

/**
 * A simple fragment class to show the details page of an individual contact.
 */
class IndividualContactFragment : Fragment() {

    private lateinit var binding: FragmentIndividualContactBinding
    private val ARG_NAME = "contactName"
    private var clickCallback: () -> Unit = {}
    private val myViewModel: ContactListViewModel by viewModels({ requireActivity() })
    var contactcheck: ContactItem? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentIndividualContactBinding.inflate(layoutInflater)
        var contactName = arguments?.getString(ARG_NAME)
        savedInstanceState?.let {
            val name = savedInstanceState.getString("name") ?:""
            val email = savedInstanceState.getString("email") ?:""
            val phone = savedInstanceState.getString("phone") ?:""
            setFieldValues(name, email, phone)
        }

        // Check if the contact exist then create the view accordingly
        contactName?.let { myViewModel.searchContactByName(it) }
        myViewModel.searchResults.observe(viewLifecycleOwner) { foundContacts ->
            contactcheck = foundContacts.firstOrNull()

            // If contact is find
            if (contactcheck != null) {
                contactcheck?.let { contactItem ->
                    setFieldValues(contactItem.name, contactItem.email, contactItem.phone)
                    saveButtonClickListeners(contactItem.name, contactItem.email, contactItem.phone)
                    goBackbuttonClickListeners(
                        contactItem.name,
                        contactItem.email,
                        contactItem.phone
                    )
                }
            } else {
                setFieldValues(contactName.toString(), "", "")
                saveButtonClickListeners(contactName.toString(), "", "")
                goBackbuttonClickListeners(contactName.toString(), "", "")
            }
        }

        return binding.root
    }

    /**
     * Go back button click listeners
     */
    private fun goBackbuttonClickListeners(oldname: String, oldemail: String, oldphone: String) {
        binding.goBackbutton.setOnClickListener {
            val (name, email, phone) = getCurrentInputs()

            if (name != oldname || email != oldemail || phone != oldphone || (contactcheck == null && name.isNotEmpty())) {    // contact does not exist
                showWarningDialog("Changes unsaved. Do you want to go back?", "Go Back", "No") {
                    clickCallback.invoke()
                    clickCallback.invoke()
                }
                return@setOnClickListener
            }
            NavigatePage()
        }
    }

    /**
     * Save button click listeners
     */
    private fun saveButtonClickListeners(
        oldname: String,
        oldemail: String,
        oldphone: String,
    ) {
        binding.saveButton.setOnClickListener {
            val (name, email, phone) = getCurrentInputs()
            name?.let { myViewModel.searchContactByName(it) }
            myViewModel.searchResults.observe(viewLifecycleOwner) { foundContacts ->
                contactcheck = foundContacts.firstOrNull()
            }

            if (name.isEmpty()) {
                showWarningDialog("Name could not be empty.")
                return@setOnClickListener
            }

            if (!phone.isEmpty() && !phone.matches("^\\+?\\s*\\(?(\\d+\\s*\\)?\\s*)*$".toRegex())) {
                showWarningDialog("Invalid phone number: numbers, +, (, ) and space are considered as valid syntax.")
                return@setOnClickListener
            }

            if (!email.isEmpty() && !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                showWarningDialog("Invalid email.")
                return@setOnClickListener
            }

            if (contactcheck != null && oldname != name) {
                showWarningDialog("Contact already exist.")
                return@setOnClickListener
            }

            myViewModel.removeItem(oldname, oldemail, oldphone)
            myViewModel.newItem(name, email, phone)
            NavigatePage()
        }
    }

    /**
     * Show a warning dialog with the given message and optional positive and negative labels.
     */
    private fun showWarningDialog(
        warnMessage: String,
        positiveLabel: String = "OK",
        negativeLabel: String = "",
        onPositiveClick: () -> Unit = {}
    ) {
        AlertDialog.Builder(requireContext()).apply {
            setTitle("Warning")
            setMessage(warnMessage)
            setPositiveButton(positiveLabel) { dialog, _ ->
                onPositiveClick()
                dialog.dismiss()
            }
            if (negativeLabel.isNotEmpty()) {
                setNegativeButton(negativeLabel) { dialog, _ ->
                    dialog.dismiss()
                }
            }
            create().show()
        }
    }

    /**
     * Get the current inputs from the text fields.
     */
    private fun getCurrentInputs() = Triple(
        binding.nameInput.text.toString(),
        binding.emailInput.text.toString(),
        binding.phoneInput.text.toString()
    )

    /**
     * Set the field values for the name, email, and phone.
     */
    private fun setFieldValues(name: String, email: String, phone: String) {
        binding.nameInput.text = Editable.Factory.getInstance().newEditable(name)
        binding.emailInput.text = Editable.Factory.getInstance().newEditable(email)
        binding.phoneInput.text = Editable.Factory.getInstance().newEditable(phone)
    }

    public fun setListener(listener: () -> Unit) {
        clickCallback = listener
    }

    companion object {
        fun newInstance(contact: String): IndividualContactFragment {
            return IndividualContactFragment().apply {
                arguments = Bundle().apply {
                    putString("contactName", contact)
                }
            }
        }
    }

    /**
     * Navigate to the main fragment
     */
    private fun NavigatePage() {
        val enterContactFragment = MainFragment.newInstance()
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentView, enterContactFragment)
            .addToBackStack(null) // Add this line to add ContactFragment to the back stack
            .commit()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val (name, email, phone) = getCurrentInputs()
        outState.putString("name", name)
        outState.putString("email", email)
        outState.putString("phone", phone)

    }
}
