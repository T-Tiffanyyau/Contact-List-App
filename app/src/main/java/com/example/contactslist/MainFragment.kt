package com.example.contactslist

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.contactslist.databinding.FragmentMainBinding
import androidx.fragment.app.viewModels


class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private var clickCallback: ((String) -> Unit)? = null
    private val myViewModel: ContactListViewModel by viewModels({ requireActivity() })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        binding = FragmentMainBinding.inflate(layoutInflater)
        setupRecyclerView()
        setupViewModelObserver()
        createButtonClickListeners()

        return binding.root
    }

    /**
     * Set up the recycler view
     */
    private fun setupRecyclerView() {
        val adapter = ContactListItem(listOf(), { item ->
            myViewModel.removeItem(item)
        }).apply {
            onButtonClickListener = object : ContactListItem.OnButtonClickListener {
                override fun onNameInRecyclerClicked(ContactName: String) {
                    NagivatePage(ContactName)
                }
            }
        }

        with(binding.recycler) {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }
    }

    /**
     * Execute function when create button is clicked
     */
    private fun createButtonClickListeners() {
        binding.createButton.setOnClickListener {
            val name = binding.nameText.text.toString()
            name?.let { myViewModel.searchContactByName(it) }
            myViewModel.searchResults.observe(viewLifecycleOwner) { foundContacts ->
                val contact = foundContacts.firstOrNull()
                if (contact != null) {
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setTitle("Warning")
                    builder.setMessage("Contact with the same name exist. Do you want to edit the current contact?")

                    builder.setPositiveButton("Ok") { dialog, _ ->
                        clickCallback?.invoke(name)
                        binding.nameText.text = Editable.Factory.getInstance().newEditable("")
                        dialog.dismiss()
                    }

                    builder.setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }

                    val dialog = builder.create()
                    dialog.show()
                } else {
                    binding.nameText.text = Editable.Factory.getInstance().newEditable("")
                    NagivatePage(name)
                }
            }
        }
    }

    /**
     * Set up the view model observer
     */
    private fun setupViewModelObserver() {
        myViewModel.observableList.observe(viewLifecycleOwner) { newList ->
            (binding.recycler.adapter as ContactListItem).updateList(newList)
        }
    }

    public fun setListener(listener: ((String) -> Unit)?) {
        clickCallback = listener
    }

    companion object {
        fun newInstance() = MainFragment()
    }

    /**
     * Navigate to the details page of an individual contact
     */
    private fun NagivatePage(name: String) {
        val enterContactFragment = IndividualContactFragment.newInstance(name)
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentView, enterContactFragment)
            .addToBackStack(null) // Add this line to add ContactFragment to the back stack
            .commit()
    }
}
