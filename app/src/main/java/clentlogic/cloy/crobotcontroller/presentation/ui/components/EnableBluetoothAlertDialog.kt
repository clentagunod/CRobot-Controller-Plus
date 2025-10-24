package clentlogic.cloy.crobotcontroller.presentation.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import clentlogic.cloy.crobotcontroller.presentation.ui.theme.DeepTeal

@Composable
fun EnableBluetoothAlertDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    if (show){
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = {
                Text("Enable Bluetooth")
            },
            text = {

                Text("Bluetooth is disabled. Please turn on your Bluetooth to start scanning.")

            },
            confirmButton = {
                TextButton(onClick = {
                    onConfirm()
                }) {
                    Text("Yes")
                }},
            dismissButton = {
                TextButton(onClick = {
                    onDismiss()
                }) {
                    Text("No")
                }},
            shape = RoundedCornerShape(4.dp),
            containerColor = DeepTeal,
            titleContentColor = Color.White,
            textContentColor = Color.White,
        )

    }

}