package com.example.coroutine.realPro.net

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

fun Context.isConnectedNetwork():Boolean = run {
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager ?: return false
    val network = cm.activeNetwork ?: return false
    val capabilities = cm.getNetworkCapabilities(network) ?: return false
    capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}