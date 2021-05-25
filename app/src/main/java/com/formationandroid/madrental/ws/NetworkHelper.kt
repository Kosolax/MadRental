package com.formationandroid.madrental.ws

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

object NetworkHelper {
    fun isConnectedToNetwork(context: Context): Boolean
    {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            connectivityManager.run { getNetworkCapabilities(activeNetwork)?.run {
                return (hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                        || hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                        || hasTransport(NetworkCapabilities.TRANSPORT_VPN))
            }
            }
        }
        else
        {
            @Suppress("DEPRECATION")
            connectivityManager.run { activeNetworkInfo?.run {
                return (type == ConnectivityManager.TYPE_WIFI
                        || type == ConnectivityManager.TYPE_MOBILE
                        || type == ConnectivityManager.TYPE_VPN)
            }
            }
        }
        return false
    }
}