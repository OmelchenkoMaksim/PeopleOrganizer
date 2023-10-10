package com.instagirlsorgainzer.ui.home

import androidx.lifecycle.ViewModel
import com.instagirlsorgainzer.room.GirlEntity
import com.instagirlsorgainzer.room.GirlRepository
import kotlinx.coroutines.flow.Flow

class GirlViewModel(private val repository: GirlRepository) : ViewModel() {
    val allGirls: Flow<List<GirlEntity>> = repository.allGirls
}

