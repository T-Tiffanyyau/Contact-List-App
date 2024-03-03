package com.example.contactslist

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: ContactListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(ContactListViewModel::class.java)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentView, MainFragment.newInstance())
                .commitNow()
        }
    }

    /**
     * Navigate to the details page of an individual contact
     */
    fun navigateToDetails(contact: String) {
        val detailsFragment = IndividualContactFragment.newInstance(contact)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentView, detailsFragment)
            .addToBackStack(null)
            .commit()
    }
}