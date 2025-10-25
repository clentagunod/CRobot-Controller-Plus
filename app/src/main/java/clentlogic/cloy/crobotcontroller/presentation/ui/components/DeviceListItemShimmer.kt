package clentlogic.cloy.crobotcontroller.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.LightGray
import clentlogic.cloy.crobotcontroller.presentation.model.LayoutModel
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer


@Composable
fun DeviceListItemShimmer(
    layout: LayoutModel

) {

    val shimmer = rememberShimmer(shimmerBounds = ShimmerBounds.Window)
    val shape = remember { RoundedCornerShape(layout.borderRadius) }


    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
            .shimmer(shimmer)
            .height(layout.screenSizeH * 0.25f)
            .background(color = LightGray, shape = shape)
            .padding(layout.padding)

    ){
        Column (
            modifier = Modifier.fillMaxWidth()
                .shimmer(shimmer)
                .padding(layout.padding)
        ) {

        }
    }

    Spacer(modifier = Modifier.height(layout.padding))

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
            .shimmer(shimmer)
            .height(layout.screenSizeH * 0.25f)
            .background(color = LightGray, shape = shape)
            .padding(layout.padding)

    ){
        Column (
            modifier = Modifier.fillMaxWidth()
                .shimmer(shimmer)
                .padding(layout.padding)
        ) {

        }
    }







}