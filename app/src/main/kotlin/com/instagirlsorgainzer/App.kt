package com.instagirlsorgainzer

import android.app.Application
import android.content.Context
import com.instagirlsorgainzer.room.GirlDatabase
import com.instagirlsorgainzer.room.GirlRepository

class App : Application() {

    val repository: GirlRepository
        get() = ServiceLocator.provideGirlRepository(this)

}


object ServiceLocator {

    fun provideGirlRepository(context: Context): GirlRepository {
        val database = GirlDatabase.getInstance(context)
        return GirlRepository(database.girlDao())
    }

}
