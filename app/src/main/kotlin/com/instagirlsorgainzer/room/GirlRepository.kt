package com.instagirlsorgainzer.room

import kotlinx.coroutines.flow.Flow

class GirlRepository(private val girlDao: GirlDao) {
    val allGirls: Flow<List<GirlEntity>> = girlDao.getAllGirls()
}