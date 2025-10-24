package clentlogic.cloy.crobotcontroller.data.communication.ble

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID


class BleHelper(private val context: Context) {

    companion object {
        private const val TARGET_DEVICE = "clent"
        private const val TAG = "BleHelper"
        private val DESCRIPTOR_UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
    }

    private val bleAdapter: BluetoothAdapter? by lazy {(context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter}
    private var bleGatt: BluetoothGatt? = null
    private var txChar: BluetoothGattCharacteristic? = null
    private var isScanning = false
    private var job: Job? = null

    private val foundDevices = mutableMapOf<String, BluetoothDevice>()

    var onDeviceFound: ((Map<String, BluetoothDevice>) -> Unit)? = null
    var onConnected: (() -> Unit)? = null
    var onDisconnected: (() -> Unit)? = null
    var onDataReceived: ((String) -> Unit)? = null
    var onError: ((String) -> Unit)? = null
    var onDebug: ((String) -> Unit)? = null
    var onTimeOut: (() -> Unit )? = null
    var onScanning: (() -> Unit)? = null
    var onBluetoothDisabled: (() -> Unit)? = null
    var onBluetoothEnabled: (() -> Unit)? = null



    private val scanCallback = object: ScanCallback(){
        override fun onScanResult(
            callbackType: Int,
            result: ScanResult?
        ) {
            super.onScanResult(callbackType, result)
            if (checkPermission(Manifest.permission.BLUETOOTH_CONNECT)){
                result?.let {
                    val device = it.device
                    val name = device.name ?: "Unknown"

                    if (foundDevices.none { d -> d.value.address == device.address}){
                        Log.d(TAG, "Found: $name (${device.address})")
                        if(name != "Unknown"){
                            foundDevices.put(name, device)
                        }

                    }
                }  ?: onError?.invoke("Result is does not exist or null!")
            }
        }

        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
            isScanning = false
            onError?.invoke("Scanning failed! Error Code: $errorCode")
        }
    }



    private val gattCallback = object: BluetoothGattCallback() {
        override fun onConnectionStateChange(
            gatt: BluetoothGatt?,
            status: Int,
            newState: Int
        ) {
            super.onConnectionStateChange(gatt, status, newState)

            when(newState){
                BluetoothProfile.STATE_CONNECTED -> {
                    onDebug?.invoke("Connected to Gatt Server!")
                    if (checkPermission(Manifest.permission.BLUETOOTH_CONNECT)){
                        gatt?.discoverServices()
                    }
                }
                BluetoothProfile.STATE_DISCONNECTED -> {
                    Log.d(TAG, "Disconnected")
                    onDisconnected?.invoke()
                    cleanUp()
                }
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {

                val foundServices = mutableListOf<UUID>()
                val foundChar = mutableListOf<UUID>()


                gatt?.services?.forEach { services ->
                    foundServices.add(services.uuid)

                    services.characteristics.forEach { characteristic ->
                        foundChar.add(characteristic.uuid)
                    }
                }

                Log.d(TAG, "Service: ${foundServices.last()}")
                Log.d(TAG, "Char: ${foundChar.last()}")

                val service = gatt?.getService(foundServices.last())
                txChar = service?.getCharacteristic(foundChar.last())

                if (txChar != null) {
                    onConnected?.invoke()
                    enableNotification(gatt!!, txChar!!)
                } else {
                    onError?.invoke("TX characteristic not found!")
                }
            }
        }

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            value: ByteArray
        ) {
            super.onCharacteristicChanged(gatt, characteristic, value)

            val data = String(value, Charsets.UTF_8)
            onDataReceived?.invoke(data)
        }

        override fun onCharacteristicWrite(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?,
            status: Int
        ) {
            super.onCharacteristicWrite(gatt, characteristic, status)

            if (status == BluetoothGatt.GATT_SUCCESS){
                Log.d(TAG,"Data sent Successfully!")
            }else{
                Log.d(TAG, "Error sending data!")
            }
        }
    }


    private val bluetoothStateReceiver = object: BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            when(p1?.action){
                BluetoothAdapter.ACTION_STATE_CHANGED -> {
                    val state = p1.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)
                    when (state) {
                        BluetoothAdapter.STATE_OFF -> {
                            Log.d(TAG, "Bluetooth Disabled!")
                            onBluetoothDisabled?.invoke()
                            onDeviceFound?.invoke(emptyMap())
                            onDisconnected?.invoke()
                            disconnect()

                        }
                        BluetoothAdapter.STATE_ON -> {
                            Log.d(TAG, "Bluetooth Enabled")
                            onBluetoothEnabled?.invoke()
                        }
                    }
                }
            }
        }

    }

    fun registerBluetoothStateReceiver(){
        val filter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
        context.registerReceiver(bluetoothStateReceiver, filter)
    }

    fun unregisterBluetoothStateReceiver(){
        try {
            context.unregisterReceiver(bluetoothStateReceiver)
        }catch (e: IllegalStateException){
            //
        }
    }

    fun connect(device: BluetoothDevice){
        if (!checkPermission(Manifest.permission.BLUETOOTH_CONNECT)) return

        bleGatt = device.connectGatt(context, false, gattCallback)
        Log.d(TAG, "Connected to device: ${device.name} successfully!")
    }

    fun disconnect(){
        if (!checkPermission(Manifest.permission.BLUETOOTH_CONNECT)) return

        bleGatt?.disconnect()
        cleanUp()
    }

    fun sendData(data: String){
        if (txChar == null || bleGatt == null) return
        if (!checkPermission(Manifest.permission.BLUETOOTH_CONNECT)) return

        try {
            val bytes = data.toByteArray(Charsets.UTF_8)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                bleGatt?.writeCharacteristic(txChar!!, bytes, BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT)
            }else{
                txChar?.value = bytes
                bleGatt?.writeCharacteristic(txChar)
            }
        } catch (e: Exception){
            onError?.invoke("Error sending data! Exception: ${e.message}")
        }
    }

    fun startScan(wait: Long){
        if (isScanning) return
        if (!checkPermission(Manifest.permission.BLUETOOTH_SCAN)) return
        if (!isBluetoothEnabled()){
            onBluetoothDisabled?.invoke()
        }

        onScanning?.invoke()

        val scanner = bleAdapter?.bluetoothLeScanner
        scanner?.let {
            onDebug?.invoke("Scanning..")
            val settings = ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).build()
            isScanning = true
            it.startScan(null, settings, scanCallback)
        } ?: onError?.invoke("Error: Scanner is null or not found!")

        job?.cancel()

        job = CoroutineScope(Dispatchers.Main).launch {
            delay(wait)
            stopScan()
            onTimeOut?.invoke()
        }

    }

    fun stopScan(){
        if (!isScanning) return
        if (!checkPermission(Manifest.permission.BLUETOOTH_SCAN)) return

        isScanning = false
        bleAdapter?.bluetoothLeScanner?.stopScan(scanCallback)
        job?.cancel()
        job = null

        if (foundDevices.isNotEmpty()){
            onDeviceFound?.invoke(foundDevices.toMap())
        }else{
            Log.d(TAG, "No Ble Device found!")
        }

    }

    private fun cleanUp() {
        txChar = null
        bleGatt = null
        bleGatt?.close()

    }

    private fun enableNotification(
        gatt: BluetoothGatt,
        characteristic: BluetoothGattCharacteristic){
        gatt.setCharacteristicNotification(characteristic, true)

        val descriptor = characteristic.getDescriptor(DESCRIPTOR_UUID)

        descriptor?.let{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                gatt.writeDescriptor(descriptor, BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE)
            }else{
                descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                gatt.writeDescriptor(descriptor)
            }
        } ?: onError?.invoke("Descriptor does not exist or null!")

    }

    private fun checkPermission(p: String): Boolean {
       return ActivityCompat.checkSelfPermission(context, p) == PackageManager.PERMISSION_GRANTED
    }

    fun isBluetoothEnabled(): Boolean {
        return bleAdapter?.isEnabled == true
    }
}