@file:OptIn(ExperimentalStdlibApi::class)

package com.vanyscore.notes.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.unit.dp
import kotlin.math.abs

fun String.toColor(): Color {
    val title = this
    val hashCode = title.hashCode()

    // Extract RGB components from the hashcode
    val red = abs(hashCode % 256)
    val green = abs((hashCode / 256) % 256)
    val blue = abs((hashCode / 65536) % 256)

    // Ensure minimum brightness for readability
    val minBrightness = 50
    val adjustedRed = maxOf(red, minBrightness)
    val adjustedGreen = maxOf(green, minBrightness)
    val adjustedBlue = maxOf(blue, minBrightness)

    return Color(adjustedRed, adjustedGreen, adjustedBlue)
}

@Composable
fun NoteSection(title: String, onTap: () -> Unit) {
    val color = title.toColor()
    return Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onTap()
            }
    ) {
        Box(
            modifier = Modifier
                .size(20.dp, 50.dp)
                .background(color = color)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(title)
        Spacer(modifier = Modifier.width(8.dp))
    }
}