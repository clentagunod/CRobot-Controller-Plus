package clentlogic.cloy.crobotcontroller.data.communication.wifi

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.util.Log
import clentlogic.cloy.crobotcontroller.domain.model.WifiState

class WifiHelper(
    private val context: Context
) {

    var onWifiStateChange: ((WifiState) -> Unit)? = null

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
                            onWifiStateChange?.invoke(WifiState.WifiOn)

                        }
                        WifiManager.WIFI_STATE_DISABLED -> {
                            onWifiStateChange?.invoke(WifiState.WifiOff)
                        }
                    }
                }


                WifiManager.NETWORK_STATE_CHANGED_ACTION -> {
                    val info = p1.getParcelableExtra<NetworkInfo>(WifiManager.EXTRA_NETWORK_INFO)
                    if (info?.isConnected == true) {
                        Log.d("WifiState", "WiFi Connected")

                    } else {
                        Log.d("WifiState", "WiFi Disconnected")

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
}