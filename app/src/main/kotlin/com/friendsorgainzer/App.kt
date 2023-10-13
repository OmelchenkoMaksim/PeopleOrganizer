package com.friendsorgainzer

import android.app.Application
import android.content.Context
import com.friendsorgainzer.room.MainDatabase
import com.friendsorgainzer.room.MainRepository

class App : Application() {

    val repository: MainRepository
        get() = ServiceLocator.provideGirlRepository(this)
}

object ServiceLocator {

    fun provideGirlRepository(context: Context): MainRepository {
        val database = MainDatabase.getInstance(context)
        return MainRepository(database.mainDao())
    }
}
