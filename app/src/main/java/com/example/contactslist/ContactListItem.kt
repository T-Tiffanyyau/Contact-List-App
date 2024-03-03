package com.example.contactslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.contactslist.databinding.ContactListItemBinding

/**
 * Reponsible for the RecyclerView in MainFragment
 */
class ContactListItem(private var ContactItems: List<ContactItem>,   // ContactItem is a data class in view model
                         private val onClick : (item: ContactItem) -> Unit
) : RecyclerView.Adapter<ContactListItem.ViewHolder>() {

    /**
     * Abstract class that must be implemented for the RecyclerView
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ContactListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    /**
     * Inner class that holds the view for the RecyclerView
     * to improve the performance of RecyclerView
     */
    inner class ViewHolder(val binding: ContactListItemBinding): RecyclerView.ViewHolder(binding.root)

    /**
     * Abstract class that must be implemented for the RecyclerView
     * Responsible for taking a ViewHolder and populating it with the data for a specific item at a given position.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = ContactItems[position]
        holder.binding.nameText.text = item.name
        holder.binding.nameText.setTextColor(item.color)
        holder.binding.deleteButton.setOnClickListener{
            onClick(item)
        }
        holder.binding.nameText.setOnClickListener{
            // send contact information
            val ContactName = item.name
            onButtonClickListener?.onNameInRecyclerClicked(ContactName)
        }
    }

    /**
     * Abstract class that must be implemented for the RecyclerView
     * Returns the number of items in the list
     */
    override fun getItemCount(): Int = ContactItems.size

    /**
     * Used in the main fragment tp update the list of contacts from the model in recycler view
     */
    fun updateList(newList: List<ContactItem>){
        ContactItems = newList
        notifyDataSetChanged()
    }

    /**
     * Interface for the button click listener in order to override WHAT TO DO when a name is clicked
     * in main fragment
     */
    interface OnButtonClickListener {
        /**
         * Store the name in the function
         */
        fun onNameInRecyclerClicked(ContactName: String)
    }

    /**
     * Variable to store the function to be able to access in main fragment
     */
    var onButtonClickListener: OnButtonClickListener? = null
}