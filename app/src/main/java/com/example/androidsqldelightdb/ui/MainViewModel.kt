package com.example.androidsqldelightdb.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidsqldelightdb.data.PersonDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import persondb.PersonEntity
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor (
    private val personDataSource: PersonDataSource
): ViewModel() {

    private var person = MutableStateFlow<PersonEntity?>(null)
    val personDetails = person.asStateFlow()

    var firstName: String = ""
    var lastName: String = ""

    val persons: Flow<List<PersonEntity>> = personDataSource.getAllPersons()

    fun onInsertPersonClick() {
        if (firstName.isBlank() || lastName.isBlank()) {
            return
        }

        viewModelScope.launch {
            personDataSource.insertPerson(firstName, lastName)
            firstName = ""
            lastName = ""
        }
    }

    fun onDeleteClick(id: Long) {
        viewModelScope.launch {
            personDataSource.deletePersonById(id)
        }
    }

    fun getPersonById(id: Long) {
        viewModelScope.launch {
            person.value = personDataSource.getPersonById(id)
        }
    }
}