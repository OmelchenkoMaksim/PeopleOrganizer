package com.friendsorgainzer

import android.app.Application
import android.content.Context
import com.friendsorgainzer.room.MainDatabase
import com.friendsorgainzer.room.MainRepository

class App : Application() {

    val repository: MainRepository
        get() = ServiceLocator.provideMainRepository(this)
}

object ServiceLocator {

    fun provideMainRepository(context: Context): MainRepository {
        val database = MainDatabase.getInstance(context)
        return MainRepository(database.mainDao())
    }
}
