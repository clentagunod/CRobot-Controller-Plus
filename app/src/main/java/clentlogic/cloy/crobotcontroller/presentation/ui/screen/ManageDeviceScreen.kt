package clentlogic.cloy.crobotcontroller.presentation.ui.screen

import android.content.Context
import android.view.MotionEvent
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import clentlogic.cloy.crobotcontroller.R
import clentlogic.cloy.crobotcontroller.domain.model.BleConnectionState
import clentlogic.cloy.crobotcontroller.presentation.contracts.MainViewContract
import clentlogic.cloy.crobotcontroller.presentation.model.LayoutModel
import clentlogic.cloy.crobotcontroller.presentation.ui.components.AnimatedLottieJson
import clentlogic.cloy.crobotcontroller.presentation.ui.components.ToggleSystemBars
import clentlogic.cloy.crobotcontroller.presentation.ui.theme.DeepTeal
import clentlogic.cloy.crobotcontroller.presentation.ui.theme.LightBlue
import clentlogic.cloy.crobotcontroller.presentation.ui.theme.LimeGreen
import clentlogic.cloy.crobotcontroller.presentation.ui.util.getScreenSize
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ManageDeviceScreen(
    viewModel: MainViewContract,
    onDisconnectRobot: () -> Unit,
) {

    val connState by viewModel.connectionState.collectAsState()
    var toggleDisconnectedPopup by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val screenSize = getScreenSize()

    val device by viewModel.selectedDevice.collectAsState()
    var deviceName by remember { mutableStateOf("No Device") }

    var hasError by remember { mutableStateOf(false) }

    var isToggled by remember { mutableStateOf(false) }

    var reloadKey by remember { mutableIntStateOf(0) }

    val webView = getWebViewObject(
        context,
        onToggleButtonChange = { isToggled = it },
        onError = { hasError = true },
        onSuccess = { hasError = false }
    )

    val layout = remember {
        val screenSizeH = screenSize.h * 0.58f
        val screenSizeW = screenSize.w * 0.35f
        val imgSize = (screenSize.w + screenSize.h) * 0.06f
        val padding = (screenSize.w + screenSize.h) * 0.02f
        val toggleSize = screenSize.w * 0.60f
        LayoutModel(
            screenSizeH = screenSizeH,
            screenSizeW = screenSizeW,
            imgSize = imgSize,
            padding = padding,
            toggleWindowSize = toggleSize
        )
    }

    ToggleSystemBars()

    LaunchedEffect(Unit) {
        device?.let {
            deviceName = it.key
        }
    }

    LaunchedEffect(connState) {
        if (connState == BleConnectionState.Disconnected){
            toggleDisconnectedPopup = true
            delay(4000)
            toggleDisconnectedPopup = false
        }
    }

    key(reloadKey) {
        AndroidView(
            factory = {
                webView.apply {
                    loadUrl("https://cam.ccontroller.online/")
                }
            },
            modifier = Modifier.fillMaxSize()
        )

    }

    if (hasError) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            AnimatedLottieJson(R.raw.lost_connection, 250.dp, iteration = 1)
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(layout.padding),

            ) {
            Icon(
                painterResource(R.drawable.battery_empty),
                tint = Color.Red.copy(0.50f),
                contentDescription = null,
                modifier = Modifier.align(Alignment.TopEnd)
            )

            Icon(
                painterResource(R.drawable.target),
                tint = Color.Gray.copy(0.50f),
                contentDescription = null,
                modifier = Modifier.align(Alignment.Center)

            )
        }
    }

    ManageDeviceContent(
        layout,
        viewModel,
        deviceName,
        isToggled,
        {
            isToggled = it
        },
        onRefresh = {
            hasError = false
            reloadKey++
        },
        onDisconnectRobot
    )

    DisconnectedPopUp(
        toggleDisconnectedPopup,
        layout
    )




}

@Composable
fun DisconnectedPopUp(
    toggle: Boolean,
    layout: LayoutModel,
    modifier: Modifier = Modifier
){

    AnimatedVisibility(
        visible = toggle,
        enter = slideInVertically(
            initialOffsetY = { height -> -height},
            animationSpec = tween(300)
        ),
        exit = slideOutVertically(
            targetOffsetY = { height -> -height},
            animationSpec = tween(300)

        )

    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .background(Color.Red)
                .fillMaxWidth()
                .height(layout.padding)
        ) {
            Text(
                "Disconnected",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White
            )

        }
    }


}


@Composable
private fun ManageDeviceContent(
    layout: LayoutModel,
    viewModel: MainViewContract,
    deviceName: String,
    toggle: Boolean,
    onToggleButtonChange: (Boolean) -> Unit,
    onRefresh: () -> Unit,
    onDisconnectRobot: () -> Unit,
    modifier: Modifier = Modifier,
) {


    val showRobotController by viewModel.toggleControlButtonFlow.collectAsState(false)
    var localShowRobotController by remember { mutableStateOf(showRobotController) }

    LaunchedEffect(showRobotController) {
        localShowRobotController = showRobotController
    }


    Box(Modifier.fillMaxSize()) {

        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(layout.padding)
        ) {
            if (localShowRobotController) {
                RobotController(
                    layout,
                    modifier = modifier.align(Alignment.BottomEnd),
                    viewModel = viewModel
                )

            }

        }


        ToggleControlConfig(
            viewModel,
            layout,
            toggle,
            deviceName,
            localShowRobotController,
            onRefresh,
            onToggleButtonChange = {
                localShowRobotController = it
            },
            onDisconnectRobot,
        )

        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(layout.padding)
        ) {

            Crossfade(
                toggle,
                animationSpec = tween(200)
            ) { targetState ->


                val toggleIconVector by remember { mutableIntStateOf(if (!targetState) R.drawable.toggle else R.drawable.toggle_out) }
                val iconColor by animateColorAsState(
                    targetValue = if (!targetState) LightBlue else Color.White,
                    label = "icon toggle button color"
                )

                Icon(
                    painterResource(toggleIconVector),
                    contentDescription = null,
                    tint = iconColor,
                    modifier = modifier
                        .size(layout.imgSize * 0.35f)
                        .clickable {
                            onToggleButtonChange(!toggle)
                        }
                )


            }


        }

    }


}

@Composable
private fun ToggleControlConfig(
    viewModel: MainViewContract,
    layout: LayoutModel,
    toggle: Boolean,
    deviceName: String,
    localShowRobotController: Boolean,
    onRefresh: () -> Unit,
    onToggleButtonChange: (Boolean) -> Unit,
    onDisconnectRobot: () -> Unit,
    modifier: Modifier = Modifier
) {

    val coroutineScope = rememberCoroutineScope()

    AnimatedVisibility(
        visible = toggle,
        enter = slideInHorizontally(
            initialOffsetX = { fullWidth -> -fullWidth },
            animationSpec = tween(400)
        ),
        exit = slideOutHorizontally(
            targetOffsetX = { fullWidth -> -fullWidth },
            animationSpec = tween(400)
        )
    ) {


        val scrollState = rememberScrollState()
        var isLoading by remember { mutableStateOf(false) }
        val shape = remember { RoundedCornerShape(5.dp) }


        Box(
            modifier = modifier
                .background(DeepTeal, shape = shape)
                .size(layout.toggleWindowSize)
                .padding(layout.padding)
                .clickable(false) {}

        ) {

            Column(
                modifier = modifier
                    .matchParentSize()
                    .verticalScroll(scrollState)


            ) {

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {


                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        deviceName,
                        color = Color.White,
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    when {
                        isLoading -> {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.White,
                                strokeWidth = 3.dp
                            )
                        }

                        else -> {
                            Icon(
                                painterResource(R.drawable.retry),
                                contentDescription = null,
                                tint = Color.White,
                                modifier = modifier
                                    .size(20.dp)
                                    .clickable {
                                        isLoading = true
                                        onRefresh()
                                        coroutineScope.launch {
                                            delay(2000)
                                            isLoading = false
                                        }


                                    }
                            )

                        }

                    }


                }


                ControlsConfig(
                    viewModel,
                    localShowRobotController,
                    onToggleButtonChange
                )
                WakeCycle()
                CameraConfig()
                Spacer(modifier.height(20.dp))
                DisconnectRobot(viewModel, onDisconnectRobot)


            }

        }

    }

}


@Composable
private fun ControlsConfig(
    viewModel: MainViewContract,
    localShowRobotController: Boolean,
    onToggleButtonChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {


    Text(
        "Control Button",
        modifier = modifier,
        color = Color.White.copy(alpha = 0.50f),
        style = MaterialTheme.typography.labelSmall
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text("Control", color = Color.White)
        ToggleSwitchElements(
            isSwitchEnabled = localShowRobotController,
            onSwitchEnabled = {
                onToggleButtonChange(it)
                viewModel.setToggleControlButtonState(it)

            }
        )

    }

    HorizontalDivider(thickness = 0.5.dp, color = Color.Black.copy(alpha = 0.3f))


}


@Composable
private fun WakeCycle(
    modifier: Modifier = Modifier,
) {

    Text(
        "Sleep",
        modifier = modifier,
        color = Color.White.copy(alpha = 0.50f),
        style = MaterialTheme.typography.labelSmall
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text("Sleep", color = Color.White)
        ToggleSwitchElements()

    }

    HorizontalDivider(thickness = 0.5.dp, color = Color.Black.copy(alpha = 0.3f))

}

@Composable
private fun CameraConfig(
    modifier: Modifier = Modifier,
) {


    Text(
        "Camera",
        modifier = modifier,
        color = Color.White.copy(alpha = 0.50f),
        style = MaterialTheme.typography.labelSmall
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text("Camera", color = Color.White)
        ToggleSwitchElements()

    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text("Light", color = Color.White)
        ToggleSwitchElements()

    }

}

@Composable
fun DisconnectRobot(
    viewModel: MainViewContract,
    onDisconnectRobot: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(

        horizontalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxWidth()
    ) {
        Button(
            onClick = {
                viewModel.disconnectDevice()
                onDisconnectRobot()

            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Red,
                contentColor = Color.White,

                )
        ) {
            Text("Disconnect", color = Color.White)

        }

    }

}

@Composable
fun ToggleSwitchElements(
    isSwitchEnabled: Boolean = false,
    onSwitchEnabled: (Boolean) -> Unit = { println("isSwitchToggled: $it") },
    modifier: Modifier = Modifier
) {

    Switch(
        checked = isSwitchEnabled,
        onCheckedChange = {
            onSwitchEnabled(!isSwitchEnabled)
        },
        colors = SwitchDefaults.colors(
            checkedTrackColor = LimeGreen,
        ),
        modifier = modifier.scale(0.7f)
    )
}

@Composable
private fun RobotController(
    layout: LayoutModel,
    viewModel: MainViewContract,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .height(layout.screenSizeH)
            .fillMaxWidth()
    ) {

        RobotControlLandMovement(layout, viewModel)
        RobotControlCamera(layout, viewModel)

    }

}

@Composable
fun RobotControlLandMovement(
    layout: LayoutModel,
    viewModel: MainViewContract,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .size(layout.screenSizeW)
    ) {
        ControlButton(
            R.drawable.up,
            byteArrayOf(0x0A, 0x00, 0x00, 0x00),
            layout,
            viewModel,
            modifier.weight(1f),
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .width(layout.screenSizeW)
        ) {
            ControlButton(
                R.drawable.left,
                byteArrayOf(0x08, 0x00, 0x00, 0x00),
                layout,
                viewModel,
                modifier.weight(1f),
            )
            ControlButton(
                R.drawable.right,
                byteArrayOf(0x07, 0x00, 0x00, 0x00),
                layout,
                viewModel,
                modifier.weight(1f),
            )

        }
        ControlButton(
            R.drawable.down,
            byteArrayOf(0x09, 0x00, 0x00, 0x00),
            layout,
            viewModel,
            modifier.weight(1f),
        )


    }

}

@Composable
fun RobotControlCamera(
    layout: LayoutModel,
    viewModel: MainViewContract,
    modifier: Modifier = Modifier,
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .size(layout.screenSizeW)
    ) {

        ControlButton(
            R.drawable.up,
            byteArrayOf(0x14, 0x00, 0x00, 0x00),
            layout,
            viewModel,
            modifier.weight(1f),
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .width(layout.screenSizeW)
        ) {
            ControlButton(
                R.drawable.left,
                byteArrayOf(0x12, 0x00, 0x00, 0x00),
                layout,
                viewModel,
                modifier.weight(1f),
            )
            ControlButton(
                R.drawable.right,
                byteArrayOf(0x11, 0x00, 0x00, 0x00),
                layout,
                viewModel,
                modifier.weight(1f),
            )

        }
        ControlButton(
            R.drawable.down,
            byteArrayOf(0x13, 0x00, 0x00, 0x00),
            layout,
            viewModel,
            modifier.weight(1f),
        )


    }

}


@Composable
private fun ControlButton(
    id: Int,
    cmd: ByteArray,
    layout: LayoutModel,
    viewModel: MainViewContract,
    modifier: Modifier = Modifier,
) {


    var isPressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.90f else 1f,
        label = "Control Button",
        animationSpec = tween(200)
    )
    val alpha by animateFloatAsState(
        targetValue = if (isPressed) 0.15f else 0.30f,
        label = "Control Button",
        animationSpec = tween(200)
    )

    val coroutineScope = rememberCoroutineScope()

    Icon(
        painterResource(id),
        contentDescription = null,
        tint = Color.Unspecified,
        modifier = modifier
            .size(layout.imgSize)
            .alpha(alpha)
            .scale(scale)
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        awaitFirstDown(requireUnconsumed = false)
                        isPressed = true

                        viewModel.sendDataToBleDevice(cmd)


                        waitForUpOrCancellation()
                        viewModel.sendDataToBleDevice(byteArrayOf(0x00, 0x00, 0x00, 0x00)) //
                        isPressed = false
                    }
                }

            }

    )

}

@Composable
private fun getWebViewObject(
    context: Context,
    onToggleButtonChange: (Boolean) -> Unit,
    onError: () -> Unit,
    onSuccess: () -> Unit,
): WebView {


    return object : WebView(context) {
        override fun onTouchEvent(event: MotionEvent?): Boolean {
            onToggleButtonChange(false)
            return true
        }

    }.apply {
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true
        settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        settings.cacheMode = WebSettings.LOAD_NO_CACHE
        settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL
        setInitialScale(1)

        webViewClient = object : WebViewClient() {
            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                println(" Error: $error")
                onError()

            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                view.evaluateJavascript(
                    "(function() { return document.body.innerText; })();"
                ) { result ->
                    if (result.contains("Cloudflare Tunnel error")) {
                        onError()
                    }
                }
            }
        }

    }

}

