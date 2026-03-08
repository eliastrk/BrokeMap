package fr.nebulo9.brokemap.ui.composables.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.background
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.math.round

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriceSliderFilterRow(
    label: String,
    bounds: PriceBounds,
    selectedCap: Double?,
    onCapChange: (Double?) -> Unit
) {
    val min = bounds.min
    val max = bounds.max
    val safeCap = selectedCap ?: max
    val normalizedValue = snapToTenth(safeCap.coerceIn(min, max))
    val steps = sliderSteps(min, max)

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = if (selectedCap == null) "MAX" else "${formatPrice(selectedCap)} EUR",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Slider(
            modifier = Modifier.fillMaxWidth(),
            value = normalizedValue.toFloat(),
            onValueChange = { onCapChange(snapToTenth(it.toDouble())) },
            valueRange = min.toFloat()..max.toFloat(),
            steps = steps,
            thumb = {
                Spacer(
                    modifier = Modifier
                        .size(18.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = CircleShape
                        )
                )
            },
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.primary,
                activeTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.35f),
                inactiveTrackColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.25f)
            )
        )
    }
}

private fun formatPrice(value: Double): String {
    val rounded = round(value * 100.0) / 100.0
    return if (rounded % 1.0 == 0.0) rounded.toInt().toString() else rounded.toString()
}

private fun snapToTenth(value: Double): Double {
    return round(value * 10.0) / 10.0
}

private fun sliderSteps(min: Double, max: Double): Int {
    val intervals = ((max - min) / 0.1).toInt()
    return (intervals - 1).coerceAtLeast(0)
}
