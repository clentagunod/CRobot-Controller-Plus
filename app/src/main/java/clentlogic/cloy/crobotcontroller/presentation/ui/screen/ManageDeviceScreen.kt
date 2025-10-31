package clentlogic.cloy.crobotcontroller.presentation.ui.screen

import android.content.Context
import android.util.Log
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
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import clentlogic.cloy.crobotcontroller.presentation.contracts.MainViewContract
import clentlogic.cloy.crobotcontroller.presentation.model.LayoutModel
import clentlogic.cloy.crobotcontroller.presentation.model.ScreenSizeModel
import clentlogic.cloy.crobotcontroller.presentation.ui.components.ToggleSystemBars
import clentlogic.cloy.crobotcontroller.presentation.ui.theme.LightBlue
import clentlogic.cloy.crobotcontroller.presentation.ui.util.getScreenSize
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch


@Composable
fun ManageDeviceScreen(
    viewModel: MainViewContract,
) {

    val device by viewModel.selectedDevice.collectAsState()
    var deviceName by remember { mutableStateOf("No Device") }

    val context = LocalContext.current
    val screenSize = getScreenSize()

    var hasError by remember { mutableStateOf(false) }

    var isToggled by remember { mutableStateOf(false) }
    val webView = getWebViewObject(context, onToggleButtonChange = { isToggled = it }, onError = { hasError = true})

    ToggleSystemBars()

    LaunchedEffect(Unit) {
        device?.let {
            deviceName = it.key
        }
    }

    AndroidView(
        factory = { webView.apply {
            loadUrl( "http://192.168.10.148/")
        } },
        modifier = Modifier.fillMaxSize()
    )

    if (hasError){
        Box(
            modifier = Modifier.fillMaxSize().background(Color.White),
            contentAlignment = Alignment.Center
        ){
            Text("Camera Offline!")
        }
    }

    ManageDeviceContent(
        screenSize,
        viewModel,
        deviceName,
        isToggled,
        {
            isToggled = it
        }
    )

}


@Composable
fun ManageDeviceContent(
    screenSize: ScreenSizeModel,
    viewModel: MainViewContract,
    deviceName: String,
    toggle: Boolean,
    onToggleButtonChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {


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


    Box(Modifier.fillMaxSize()) {

        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(layout.padding)
        ) {

            RobotController(
                layout,
                modifier = modifier.align(Alignment.BottomEnd),
                viewModel = viewModel
            )

        }


        ToggleControlConfig(
            layout,
            toggle,
            deviceName,
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
                    targetValue = if (toggleIconVector == R.drawable.toggle) LightBlue else Color.Black,
                    label = "icon toggle button color"
                )

                Icon(
                    painterResource(toggleIconVector),
                    contentDescription = null,
                    tint = iconColor,
                    modifier = modifier
                        .size(layout.imgSize * 0.3f)
                        .clickable {
                            onToggleButtonChange(!toggle)
                        }
                )

                
            }


        }

    }


}

@Composable
fun ToggleControlConfig(
    layout: LayoutModel,
    toggle: Boolean,
    deviceName: String,
    modifier: Modifier = Modifier
) {
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

        val shape = remember { RoundedCornerShape(5.dp) }


        Box(
            modifier = modifier
                .background(Color.White, shape = shape)
                .size(layout.toggleWindowSize)
                .padding(layout.padding)
                .clickable(false) {}

        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp),
                modifier = modifier
                    .matchParentSize()
                    .verticalScroll(scrollState)

            ) {
                Text(
                    deviceName,
                    modifier = modifier.align(Alignment.CenterHorizontally)
                )

                WakeCycle(layout)
                CameraConfig(layout)


            }

        }

    }

}


@Composable
fun WakeCycle(
    layout: LayoutModel,
    modifier: Modifier = Modifier,
) {

    var isSleeping by remember { mutableStateOf(false) }

    Text(
        "Sleep",
        modifier = modifier,
        color = Color.Black.copy(alpha = 0.50f),
        style = MaterialTheme.typography.labelSmall
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text("Sleep")
        Switch(
            checked = isSleeping,
            {
                isSleeping = !isSleeping

                Log.d("WakeCycle", "WakeCycle: $isSleeping ")
            },
            modifier = modifier.scale(0.7f)
        )

    }
    HorizontalDivider(thickness = 0.5.dp, color = Color.Black.copy(alpha = 0.3f))

}

@Composable
fun CameraConfig(
    layout: LayoutModel,
    modifier: Modifier = Modifier,
) {

    var isCameraOn by remember { mutableStateOf(false) }

    Text(
        "Camera",
        modifier = modifier,
        color = Color.Black.copy(alpha = 0.50f),
        style = MaterialTheme.typography.labelSmall
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text("Camera")
        Switch(
            checked = isCameraOn,
            {
                isCameraOn = !isCameraOn
            },
            modifier = modifier.scale(0.7f)
        )

    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()

    ){
        Text("Camera refresh")

        Icon(
            painterResource(R.drawable.retry),
            contentDescription = null,
            tint = Color.Unspecified
        )

    }


}


@Composable
fun RobotController(
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
            "UP",
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
                "LEFT",
                layout,
                viewModel,
                modifier.weight(1f),
            )
            ControlButton(
                R.drawable.right,
                "RIGHT",
                layout,
                viewModel,
                modifier.weight(1f),
            )

        }
        ControlButton(
            R.drawable.down,
            "DOWN",
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
            "Camera Up",
            layout,
            viewModel,
            modifier.weight(1f),
        )
        ControlButton(
            R.drawable.down,
            "Camera Down",
            layout,
            viewModel,
            modifier.weight(1f),
        )

    }

}


@Composable
fun ControlButton(
    id: Int,
    cmd: String,
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
        targetValue = if (isPressed) 0.20f else 0.40f,
        label = "Control Button",
        animationSpec = tween(200)
    )

    val coroutineScope = rememberCoroutineScope()

    Image(
        painterResource(id),
        contentDescription = null,
        modifier = modifier
            .size(layout.imgSize)
            .alpha(alpha)
            .scale(scale)
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        awaitFirstDown(requireUnconsumed = false)

                        isPressed = true
                        val job = coroutineScope.launch {
                            while (isActive) {
                                viewModel.sendDataToBleDevice(cmd)
                                delay(30)
                            }
                        }

                        waitForUpOrCancellation()
                        job.cancel()
                        isPressed = false
                    }
                }

            }

    )

}


fun getWebViewObject(
    context: Context,
    onToggleButtonChange: (Boolean) -> Unit,
    onError: () -> Unit,
): WebView {

    return object : WebView(context) {
        override fun onTouchEvent(event: MotionEvent?): Boolean {
            onToggleButtonChange(false)
            return super.onTouchEvent(event)
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

        webViewClient = object: WebViewClient() {
            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                onError()

            }
        }

    }

}

