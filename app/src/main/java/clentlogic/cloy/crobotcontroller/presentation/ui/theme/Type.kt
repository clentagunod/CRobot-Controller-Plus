package clentlogic.cloy.crobotcontroller.presentation.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import clentlogic.cloy.crobotcontroller.R


val BrandonGrotesque = FontFamily(Font(R.font.brandon_grotesque))
val MontserratExtraBold = FontFamily(Font(R.font.montserrat_extrabold))

val OutfitLight = FontFamily(Font(R.font.outfit_light))
val RobotoMedium = FontFamily(Font(R.font.roboto_medium))

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = BrandonGrotesque,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(
        color = Color.White,
        fontFamily = MontserratExtraBold,
        fontWeight = FontWeight.Bold,
        fontSize = 30.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = OutfitLight,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.5.sp,
        textAlign = TextAlign.Justify
    ),
    displayMedium = TextStyle(
        fontFamily = RobotoMedium,
        fontWeight = FontWeight.Normal,
        fontSize = 25.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.5.sp,
    ),
    displayLarge = TextStyle(
        fontFamily = OutfitLight,
        fontWeight = FontWeight.Normal,
        fontSize = 40.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.5.sp,
    ),
    headlineSmall = TextStyle(
        fontFamily = OutfitLight,
        fontWeight = FontWeight.Normal,
        fontSize = 17.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.5.sp
    ),
    displaySmall = TextStyle(
        fontFamily = BrandonGrotesque,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.5.sp
    ),

)