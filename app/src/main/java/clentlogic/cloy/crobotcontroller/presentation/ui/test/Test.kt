package clentlogic.cloy.crobotcontroller.presentation.ui.test

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import clentlogic.cloy.crobotcontroller.CRobotControllerApp.Companion.GLOBAL_MODE
import clentlogic.cloy.crobotcontroller.CRobotControllerApp.Companion.LOCAL_MODE
import clentlogic.cloy.crobotcontroller.presentation.contracts.MainViewContract
import clentlogic.cloy.crobotcontroller.presentation.viewmodel.MainViewModel


@Composable
fun TestCompose(
    viewModel: MainViewContract = hiltViewModel<MainViewModel>()
) {

    val controlModes by remember { mutableStateOf(listOf(LOCAL_MODE, GLOBAL_MODE)) }

    val currentMode by viewModel.controlModeFlow.collectAsState(LOCAL_MODE)


    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.selectableGroup()
        ) {

            controlModes.forEach { mode ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = (mode == currentMode),
                            onClick = { viewModel.setControlModeState(mode) }
                        )

                ) {
                    RadioButton(
                        selected = (mode == currentMode),
                        onClick = null
                    )
                    Text(
                        text = mode
                    )

                }

            }


        }


    }

}

