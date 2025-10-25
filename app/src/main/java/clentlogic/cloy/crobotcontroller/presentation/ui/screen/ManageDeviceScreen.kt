package clentlogic.cloy.crobotcontroller.presentation.ui.screen

import android.bluetooth.BluetoothDevice
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import clentlogic.cloy.crobotcontroller.presentation.contracts.MainViewContract


@Composable
fun ManageDeviceScreen(
    device: Map.Entry<String, BluetoothDevice>,
    viewModel: MainViewContract){

    Column{
        Text("${device.key} and ${device.value}")
    }


}

@Composable
fun TopNameView(){



}