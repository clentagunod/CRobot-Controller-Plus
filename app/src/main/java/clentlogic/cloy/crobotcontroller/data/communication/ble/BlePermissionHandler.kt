package clentlogic.cloy.crobotcontroller.data.communication.ble

import android.Manifest
import android.app.Activity.RESULT_OK
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat


class BlePermissionHandler(
     private val activity: ComponentActivity,
     private val bleHelper: BleHelper
     ) {


     private val permissionCallback = activity.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){permission ->
          val isPermitted = permission.values.all { it  }
          if (isPermitted){
               Log.d("Permission", "Permitted!")
               enableBluetooth()
          }else{
               Log.d("Permission", "Permission Denied!")
          }
     }

     private val bluetoothLauncher = activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result ->
          if (result.resultCode == RESULT_OK){
               enableBluetooth()
          }else{
               Log.d("BT", "Needs bluetooth to use")
          }

     }

     fun checkBlePermission(){
          val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
               arrayOf(
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.ACCESS_FINE_LOCATION
               )
          }else{
               arrayOf(
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.ACCESS_FINE_LOCATION
               )

          }

          val missingPermission = permissions.filter {
               ActivityCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED
          }

          if (missingPermission.isNotEmpty()){
               permissionCallback.launch(missingPermission.toTypedArray())
          }else{
               enableBluetooth()
          }
     }

     private fun enableBluetooth(){
          if(!bleHelper.isBluetoothEnabled()){
               val btIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
               bluetoothLauncher.launch(btIntent)
          }
     }



}