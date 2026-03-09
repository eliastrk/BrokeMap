package fr.nebulo9.brokemap.ui.composables

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ComposeView
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import androidx.core.graphics.createBitmap
import androidx.core.view.drawToBitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

fun BitmapFromVector(context: Context, resId: Int): BitmapDescriptor? {
    val drawable = ContextCompat.getDrawable(context,resId)

    drawable!!.setBounds(0,0,drawable.intrinsicWidth, drawable.intrinsicHeight)

    val bitmap = createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight)

    val canvas = Canvas(bitmap)

    drawable.draw(canvas)

    return BitmapDescriptorFactory.fromBitmap(bitmap)
}

fun createMarkerIconFromVector(
    context: Context,
    icon: ImageVector,
    backgroundColor: Int = Color.RED,
    iconColor: Int = Color.WHITE
): BitmapDescriptor {
    val composeView = ComposeView(context).apply {
        setContent {
            MarkerIconContent(
                icon = icon,
                backgroundColor = Color.valueOf(backgroundColor),
                iconColor = Color.valueOf(iconColor)
            )
        }
    }

    // Measure and layout the view
    val widthSpec = android.view.View.MeasureSpec.makeMeasureSpec(
        100,
        android.view.View.MeasureSpec.EXACTLY
    )
    val heightSpec = android.view.View.MeasureSpec.makeMeasureSpec(
        100,
        android.view.View.MeasureSpec.EXACTLY
    )

    composeView.measure(widthSpec, heightSpec)
    composeView.layout(0, 0, 100, 100)

    // Draw to bitmap
    val bitmap = composeView.drawToBitmap()

    return BitmapDescriptorFactory.fromBitmap(bitmap)
}

@Composable
private fun MarkerIconContent(
    icon: ImageVector,
    backgroundColor: Color,
    iconColor: Color
) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .clip(CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(60.dp)
        )
    }
}

