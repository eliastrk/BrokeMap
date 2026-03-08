package fr.nebulo9.brokemap.ui.composables.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import fr.nebulo9.brokemap.api.BarDetail
import fr.nebulo9.brokemap.api.Business
import fr.nebulo9.brokemap.api.FastfoodDetail
import fr.nebulo9.brokemap.api.MuseumDetail
import fr.nebulo9.brokemap.api.OpeningSchedule
import fr.nebulo9.brokemap.api.RestaurantDetail
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import kotlin.math.round

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusinessDetailsSheet(
    business: Business,
    details: BusinessDetailsCache,
    onDismiss: () -> Unit
) {
    val restaurant = details.restaurantsById[business.id]
    val bar = details.barsById[business.id]
    val fastfood = details.fastfoodsById[business.id]
    val museum = details.museumsById[business.id]

    val schedule = when (business.type_name) {
        "restaurant" -> restaurant?.schedule
        "bar", "dancing_bar" -> bar?.schedule
        "fastfood" -> fastfood?.schedule
        "museum" -> museum?.schedule
        else -> business.schedule
    } ?: business.schedule
    val fallbackIsOpen = when (business.type_name) {
        "restaurant" -> restaurant?.is_open
        "bar", "dancing_bar" -> bar?.is_open
        "fastfood" -> fastfood?.is_open
        "museum" -> museum?.is_open
        else -> business.is_open
    } ?: business.is_open
    val openStatus = deriveOpenStatusFromSchedule(schedule) ?: fallbackIsOpen

    ModalBottomSheet(onDismissRequest = onDismiss) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = business.name,
                            style = MaterialTheme.typography.titleLarge
                        )
                        OpenStatusBadge(openStatus = openStatus)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = business.type_name.replace('_', ' '),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f))
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("Details", style = MaterialTheme.typography.titleMedium)
                        when (business.type_name) {
                            "restaurant" -> RestaurantCharacteristics(restaurant)
                            "bar" -> BarCharacteristics(bar, showTerrace = true)
                            "dancing_bar" -> BarCharacteristics(bar, showTerrace = false)
                            "fastfood" -> FastfoodCharacteristics(fastfood)
                            "museum" -> MuseumCharacteristics(museum)
                            else -> Text(
                                text = "No specific characteristics.",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f))
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("Info", style = MaterialTheme.typography.titleMedium)
                        GeneralInfoRows(
                            address = when (business.type_name) {
                                "restaurant" -> restaurant?.address
                                "bar", "dancing_bar" -> bar?.address
                                "fastfood" -> fastfood?.address
                                "museum" -> museum?.address
                                else -> business.address
                            } ?: business.address,
                            postalCode = when (business.type_name) {
                                "restaurant" -> restaurant?.postal_code
                                "bar", "dancing_bar" -> bar?.postal_code
                                "fastfood" -> fastfood?.postal_code
                                "museum" -> museum?.postal_code
                                else -> business.postal_code
                            } ?: business.postal_code,
                            city = when (business.type_name) {
                                "restaurant" -> restaurant?.city
                                "bar", "dancing_bar" -> bar?.city
                                "fastfood" -> fastfood?.city
                                "museum" -> museum?.city
                                else -> business.city
                            } ?: business.city,
                            country = when (business.type_name) {
                                "restaurant" -> restaurant?.country
                                "bar", "dancing_bar" -> bar?.country
                                "fastfood" -> fastfood?.country
                                "museum" -> museum?.country
                                else -> business.country
                            } ?: business.country,
                            phone = when (business.type_name) {
                                "restaurant" -> restaurant?.phone
                                "bar", "dancing_bar" -> bar?.phone
                                "fastfood" -> fastfood?.phone
                                "museum" -> museum?.phone
                                else -> business.phone
                            } ?: business.phone,
                            website = when (business.type_name) {
                                "restaurant" -> restaurant?.website
                                "bar", "dancing_bar" -> bar?.website
                                "fastfood" -> fastfood?.website
                                "museum" -> museum?.website
                                else -> business.website
                            } ?: business.website
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(20.dp)) }
        }
    }
}

@Composable
private fun OpenStatusBadge(openStatus: Boolean?) {
    val (label, color) = when (openStatus) {
        true -> "Open" to Color(0xFF2E7D32)
        false -> "Closed" to Color(0xFFC62828)
        null -> "Unknown" to MaterialTheme.colorScheme.onSurfaceVariant
    }
    Text(
        text = label,
        color = Color.White,
        modifier = Modifier
            .background(color = color, shape = MaterialTheme.shapes.small)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        style = MaterialTheme.typography.labelLarge
    )
}

@Composable
private fun RestaurantCharacteristics(restaurant: RestaurantDetail?) {
    if (restaurant == null) {
        Text("Details unavailable.", style = MaterialTheme.typography.bodyMedium)
        return
    }
    InfoRow("Average price", restaurant.average_price)
    InfoRow("Terrace", yesNo(restaurant.terrace))
    InfoRow("Student discount", yesNo(restaurant.student_discount))
    if (restaurant.food_types.isNotEmpty()) {
        InfoRow("Food types", restaurant.food_types.joinToString { it.name })
    }
}

@Composable
private fun BarCharacteristics(bar: BarDetail?, showTerrace: Boolean) {
    if (bar == null) {
        Text("Details unavailable.", style = MaterialTheme.typography.bodyMedium)
        return
    }

    findAlcoholPrice(bar, "beer", "biere")?.let { InfoRow("Beer price", "${formatPrice(it)} EUR") }
    findAlcoholPrice(bar, "wine", "vin")?.let { InfoRow("Wine price", "${formatPrice(it)} EUR") }
    findAlcoholPrice(bar, "cocktail")?.let { InfoRow("Cocktail price", "${formatPrice(it)} EUR") }
    findAlcoholPrice(bar, "vodka")?.let { InfoRow("Vodka price", "${formatPrice(it)} EUR") }
    findAlcoholPrice(bar, "whisky", "whiskey")?.let { InfoRow("Whisky price", "${formatPrice(it)} EUR") }
    findAlcoholPrice(bar, "shot", "shots")?.let { InfoRow("Shot price", "${formatPrice(it)} EUR") }

    if (showTerrace) InfoRow("Terrace", yesNo(bar.terrace))
    bar.student_discount?.let { InfoRow("Student discount", yesNo(it)) }
}

@Composable
private fun FastfoodCharacteristics(fastfood: FastfoodDetail?) {
    if (fastfood == null) {
        Text("Details unavailable.", style = MaterialTheme.typography.bodyMedium)
        return
    }

    fastfood.items
        .sortedBy { it.name.lowercase() }
        .forEach { item ->
            InfoRow("${item.name} price", "${formatPrice(item.price)} EUR")
        }
    InfoRow("Terrace", yesNo(fastfood.terrace))
    InfoRow("Student discount", yesNo(fastfood.student_discount))
}

@Composable
private fun MuseumCharacteristics(museum: MuseumDetail?) {
    if (museum == null) {
        Text("Details unavailable.", style = MaterialTheme.typography.bodyMedium)
        return
    }

    val ticketLabel = museum.ticket_price?.let { "${formatPrice(it)} EUR" } ?: "Free"
    InfoRow("Ticket price", ticketLabel)
    museum.student_discount?.let { InfoRow("Student discount", yesNo(it)) }
}

@Composable
private fun GeneralInfoRows(
    address: String?,
    postalCode: String?,
    city: String?,
    country: String?,
    phone: String?,
    website: String?
) {
    var shown = false

    val location = buildLocationLine(
        address = address,
        postalCode = postalCode,
        city = city,
        country = country
    )
    if (!location.isNullOrBlank()) {
        InfoRow("Location", location)
        shown = true
    }

    if (!phone.isNullOrBlank()) {
        InfoRow("Phone", phone)
        shown = true
    }

    if (!website.isNullOrBlank()) {
        WebsiteRow(website = website)
        shown = true
    }

    if (!shown) {
        Text("No additional info available.", style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun WebsiteRow(website: String) {
    val uriHandler = LocalUriHandler.current
    val targetUrl = normalizeWebsiteUrl(website)
    val shortLabel = shortenWebsiteLabel(website)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = "Website",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = shortLabel,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.clickable {
                uriHandler.openUri(targetUrl)
            }
        )
    }
}

private fun yesNo(value: Boolean): String = if (value) "Yes" else "No"

private fun formatPrice(value: Double): String {
    val rounded = round(value * 100.0) / 100.0
    return if (rounded % 1.0 == 0.0) rounded.toInt().toString() else rounded.toString()
}

private fun findAlcoholPrice(bar: BarDetail, vararg keywords: String): Double? {
    val matches = bar.alcohols.filter { alcohol ->
        val name = alcohol.name.lowercase()
        keywords.any { keyword -> keyword in name }
    }
    return matches.minOfOrNull { it.price }
}

private fun buildLocationLine(
    address: String?,
    postalCode: String?,
    city: String?,
    country: String?
): String? {
    val cityPart = listOfNotNull(postalCode?.takeIf { it.isNotBlank() }, city?.takeIf { it.isNotBlank() })
        .joinToString(" ")
    val parts = listOfNotNull(
        address?.takeIf { it.isNotBlank() },
        cityPart.takeIf { it.isNotBlank() },
        country?.takeIf { it.isNotBlank() }
    )
    if (parts.isEmpty()) return null
    return parts.joinToString(", ")
}

private fun normalizeWebsiteUrl(raw: String): String {
    val trimmed = raw.trim()
    if (trimmed.startsWith("http://", ignoreCase = true) || trimmed.startsWith("https://", ignoreCase = true)) {
        return trimmed
    }
    return "https://$trimmed"
}

private fun shortenWebsiteLabel(raw: String): String {
    var label = raw.trim()
    label = label.removePrefix("https://").removePrefix("http://")
    label = label.removePrefix("www.")
    val slashIndex = label.indexOf('/')
    if (slashIndex >= 0) label = label.substring(0, slashIndex)
    return label
}

private fun deriveOpenStatusFromSchedule(schedule: List<OpeningSchedule>?): Boolean? {
    if (schedule.isNullOrEmpty()) return null

    val nowTime = LocalTime.now()
    val today = LocalDate.now().dayOfWeek
    val yesterday = today.minus(1)

    fun matchesDay(scheduleDay: String?, day: DayOfWeek): Boolean {
        if (scheduleDay.isNullOrBlank()) return false
        val v = scheduleDay.trim().lowercase()
        return when (day) {
            DayOfWeek.MONDAY -> v in setOf("1", "mon", "monday", "lundi")
            DayOfWeek.TUESDAY -> v in setOf("2", "tue", "tuesday", "mardi")
            DayOfWeek.WEDNESDAY -> v in setOf("3", "wed", "wednesday", "mercredi")
            DayOfWeek.THURSDAY -> v in setOf("4", "thu", "thursday", "jeudi")
            DayOfWeek.FRIDAY -> v in setOf("5", "fri", "friday", "vendredi")
            DayOfWeek.SATURDAY -> v in setOf("6", "sat", "saturday", "samedi")
            DayOfWeek.SUNDAY -> v in setOf("0", "7", "sun", "sunday", "dimanche")
        }
    }

    fun parseTime(raw: String?): LocalTime? {
        if (raw.isNullOrBlank()) return null
        val clean = raw.trim()
        val parts = clean.split(':')
        if (parts.size < 2) return null
        val hour = parts[0].toIntOrNull() ?: return null
        val minute = parts[1].toIntOrNull() ?: return null
        return LocalTime.of(hour.coerceIn(0, 23), minute.coerceIn(0, 59))
    }

    fun isOpenInEntry(entry: OpeningSchedule, day: DayOfWeek, time: LocalTime): Boolean {
        if (!matchesDay(entry.dayOfWeek, day)) return false
        val open = parseTime(entry.openTime) ?: return false
        val close = parseTime(entry.closeTime) ?: return false

        return if (close >= open) {
            time >= open && time <= close
        } else {
            // Overnight window (e.g. 18:00 -> 02:00)
            time >= open || time <= close
        }
    }

    val openToday = schedule.any { isOpenInEntry(it, today, nowTime) }
    if (openToday) return true

    // Handle overnight schedules defined on previous day (e.g. saturday closes after midnight).
    val openFromYesterdayOvernight = schedule.any { entry ->
        if (!matchesDay(entry.dayOfWeek, yesterday)) return@any false
        val open = parseTime(entry.openTime) ?: return@any false
        val close = parseTime(entry.closeTime) ?: return@any false
        close < open && nowTime <= close
    }
    if (openFromYesterdayOvernight) return true

    val hasParsableTodayEntry = schedule.any { entry ->
        matchesDay(entry.dayOfWeek, today) && parseTime(entry.openTime) != null && parseTime(entry.closeTime) != null
    }
    val hasParsableYesterdayOvernight = schedule.any { entry ->
        if (!matchesDay(entry.dayOfWeek, yesterday)) return@any false
        val open = parseTime(entry.openTime)
        val close = parseTime(entry.closeTime)
        open != null && close != null && close < open
    }

    return if (hasParsableTodayEntry || hasParsableYesterdayOvernight) false else null
}
