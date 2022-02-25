package com.example.androidsqldelightdb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.androidsqldelightdb.databinding.ActivityMainBinding
import com.example.androidsqldelightdb.ui.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Adding @AndroidEntryPoint annotation so that dagger hilt can inject dependencies in it.
 * */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel>()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeData()
        setupUIListeners()
    }

    private fun setupUIListeners() {
        with(binding) {
            btnSearch.setOnClickListener {
                val id: Long = etPersonId.text.toString().toLongOrNull() ?: 0
                viewModel.getPersonById(id)
            }

            btnDelete.setOnClickListener {
                val id: Long = etPersonId.text.toString().toLongOrNull() ?: 0
                viewModel.onDeleteClick(id)
            }

            btnAdd.setOnClickListener {
                viewModel.firstName = etFName.text.toString().trim()
                viewModel.lastName = etLName.text.toString().trim()
                viewModel.onInsertPersonClick()
            }
        }
    }

    private fun observeData() {
        lifecycleScope.launch {
            viewModel.personDetails.collectLatest {
                Toast.makeText(
                    this@MainActivity,
                    it.toString(),
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        lifecycleScope.launch {
            viewModel.persons.collect {
                // Resetting person list
                binding.tvPersons.text = ""

                it.forEach { person ->
                    binding.tvPersons.text = binding.tvPersons.text.toString().plus("\n")
                        .plus(person.id).plus(" ")
                        .plus(person.firstname).plus(" ")
                        .plus(person.lastname)
                }
            }
        }
    }
}