package com.friendsorgainzer

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.widget.Toast
import com.friendsorgainzer.room.MainDatabase
import com.friendsorgainzer.room.MainRepository

class App : Application() {

    val repository: MainRepository
        get() = provideMainRepository(this)

    private lateinit var networkCallback: ConnectivityManager.NetworkCallback

    private val connectivityManager: ConnectivityManager
            by lazy(LazyThreadSafetyMode.NONE) {
                getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            }


    override fun onCreate() {
        super.onCreate()
        setupNetworkListener()
        initPrefs(this)
    }

    private fun setupNetworkListener() {
        networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                Toast.makeText(this@App, "Internet connection established", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onLost(network: Network) {
                Toast.makeText(this@App, "Lost internet connection", Toast.LENGTH_SHORT).show()
            }
        }
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }

    override fun onTerminate() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
        super.onTerminate()
    }


    companion object MainProvider {
        lateinit var sharedPreferences: SharedPreferences
        private fun initPrefs(context: Context) {
            sharedPreferences =
                context.getSharedPreferences(
                    "person_app_prefs", Context.MODE_PRIVATE
                )
        }

        private fun provideMainRepository(context: Context): MainRepository {
            val database = MainDatabase.getInstance(context)
            return MainRepository(database.mainDao())
        }
    }

}
