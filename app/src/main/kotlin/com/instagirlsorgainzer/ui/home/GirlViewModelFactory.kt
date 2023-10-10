package com.instagirlsorgainzer.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.instagirlsorgainzer.room.GirlRepository

class GirlViewModelFactory(private val repository: GirlRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GirlViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GirlViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}