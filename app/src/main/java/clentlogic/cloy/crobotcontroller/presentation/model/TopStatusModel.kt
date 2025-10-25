package clentlogic.cloy.crobotcontroller.presentation.model

import androidx.compose.ui.graphics.Color

data class TopStatusModel(
    val status: String = "Disconnected",
    val color: Color = Color.White,
)