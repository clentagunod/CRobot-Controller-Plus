package clentlogic.cloy.crobotcontroller.data.communication.wifi

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.util.Log
import clentlogic.cloy.crobotcontroller.domain.model.WifiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable.isActive
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class WifiHelper(
    private val context: Context
) {

    private var internetCheckJob: Job? = null
    private val coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    var onWifiStateEnabled: (() -> Unit)? = null
    var onWifiStateDisabled: (() -> Unit)? = null

    var onWifiConnected: (() -> Unit)? = null
    var onWifiDisconnected: (() -> Unit)? = null

    var onWifiHasInternet: (() -> Unit)? = null
    var onWifiNoInternet: (() -> Unit)? = null

    private val wifiStateChangedListener = object: BroadcastReceiver(){
        override fun onReceive(p0: Context?, p1: Intent?) {
            when(p1?.action){

                WifiManager.WIFI_STATE_CHANGED_ACTION -> {
                    val wifiState = p1.getIntExtra(
                        WifiManager.EXTRA_WIFI_STATE,
                        WifiManager.WIFI_STATE_UNKNOWN
                    )


                    when(wifiState){
                        WifiManager.WIFI_STATE_ENABLED ->  {
                            onWifiStateEnabled?.invoke()

                        }
                        WifiManager.WIFI_STATE_DISABLED -> {
                            onWifiStateDisabled?.invoke()
                        }
                    }
                }


                WifiManager.NETWORK_STATE_CHANGED_ACTION -> {
                    val info = p1.getParcelableExtra<NetworkInfo>(WifiManager.EXTRA_NETWORK_INFO)
                    if (info?.isConnected == true) {
                        onWifiConnected?.invoke()
                        startInternetCheck()

                    } else {
                        onWifiDisconnected?.invoke()
                        onWifiNoInternet?.invoke()
                        stopInternetCheck()

                    }
                }
            }
        }

    }

    fun registerWifiStateListener(){
        val filter = IntentFilter().apply {
            addAction(WifiManager.WIFI_STATE_CHANGED_ACTION)
            addAction(  WifiManager.NETWORK_STATE_CHANGED_ACTION )
        }
        context.registerReceiver(wifiStateChangedListener, filter)
    }

    fun unregisterWifiStateListener(){
        try {
            context.unregisterReceiver(wifiStateChangedListener)
        } catch (_: IllegalArgumentException) {
            // receiver already unregistered
        }
    }

    fun isWifiEnabled(): Boolean {
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as? WifiManager
        return wifiManager?.isWifiEnabled == true
    }

    fun isWifiConnected(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
    }

    fun hasWifiInternet(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork ?: return false
        val capabilities = cm.getNetworkCapabilities(network) ?: return false

        val hasInternet =
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        val validated =
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)

        return hasInternet && validated
    }

    private fun startInternetCheck() {
        if (internetCheckJob?.isActive == true) return // Already checking

        internetCheckJob = coroutineScope.launch {
            while (isActive) {
                if (hasWifiInternet()) {
                    onWifiHasInternet?.invoke()
                    stopInternetCheck()
                    break
                } else {
                    onWifiNoInternet?.invoke()
                }
                delay(1000) // Check every 1 second
            }
        }
    }

    private fun stopInternetCheck() {
        internetCheckJob?.cancel()
        internetCheckJob = null
    }


}