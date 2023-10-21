package com.friendsorgainzer.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.friendsorgainzer.room.MainRepository
import com.friendsorgainzer.room.PersonEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class DetailsViewModel(private val repository: MainRepository) : ViewModel() {

    fun getPersonDetails(id: Int): Flow<PersonEntity?> {
        return repository.getPersonById(id)
    }

    fun updatePersonPhotoLocalUri(id: Int, photoLocalUri: String) {
        viewModelScope.launch {
            repository.updatePersonPhotoLocalUri(id, photoLocalUri)
        }
    }
}