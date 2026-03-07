package fr.nebulo9.brokemap.ui.composables

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import androidx.core.graphics.createBitmap

fun BitmapFromVector(context: Context, resId: Int): BitmapDescriptor? {
    val drawable = ContextCompat.getDrawable(context,resId)

    drawable!!.setBounds(0,0,drawable.intrinsicWidth, drawable.intrinsicHeight)

    val bitmap = createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight)

    val canvas = Canvas(bitmap)

    drawable.draw(canvas)

    return BitmapDescriptorFactory.fromBitmap(bitmap)
}