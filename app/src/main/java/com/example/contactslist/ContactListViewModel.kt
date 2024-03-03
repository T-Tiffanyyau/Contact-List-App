package com.example.contactslist

import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.random.Random

data class ContactItem(val name: String, val email: String, val phone: String, val color: Int)

class ContactListViewModel : ViewModel() {

    private val rand = Random.Default

    //MODEL part of MVVM
    private val actualList = MutableLiveData(
        mutableListOf(
            ContactItem(
                "sample", "someone@google.com", "123 456 7890",
                Color.rgb(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256))
            )
        )
    )

    //Part of the VM that the view see
    val observableList = actualList as LiveData<out List<ContactItem>>

    fun newItem(name: String, email: String, phone: String) {
        val color = Color.rgb(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256))
        actualList.value!!.add(ContactItem(name, email, phone, color))
        actualList.value = actualList.value
    }

    fun removeItem(item: ContactItem) {
        actualList.value!!.remove(item)
        actualList.value = actualList.value
    }

    fun removeItem(name: String, email: String, phone: String) {
        val itemToRemove = actualList.value?.find {
            it.name == name && it.email == email && it.phone == phone
        }

        // if the item is found or not null, remove it
        itemToRemove?.let {
            removeItem(it)
        }
    }

    /**
     * Stores the search result (ContactItem) from searchContactByName for main fragment to observe
     */
    private val _searchResults = MutableLiveData<List<ContactItem>>()

    /**
     * Make a mutable copy of _searchResults to be observed by the main fragment
     */
    val searchResults: LiveData<List<ContactItem>> = _searchResults

    /**
     * Search for a contact by name
     */
    fun searchContactByName(name: String) {
        val results = if (name.isNotEmpty()) {
            actualList.value?.filter { it.name.equals(name, ignoreCase = false) }
        } else {
            emptyList()
        }
        _searchResults.value = results.orEmpty()
    }

}
