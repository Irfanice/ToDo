package com.irfandev.todo.ui.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DayCell(
    day: String,
    today: String,
    state: String?,
    onYes: (String) -> Unit,
    onNo: (String) -> Unit
) {
    val displayDay = try {
        val parser = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val d = parser.parse(day) ?: Date()
        SimpleDateFormat("d", Locale.getDefault()).format(d)
    } catch (e: Exception) {
        day.takeLast(2)
    }

    val isToday = day == today

    val bgColor = when (state) {
        "yes" -> Color(0xFF5EB090) // ✅ Strong green
        "no" -> Color(0xFFAF3557)  // ❌ Red for missed day
        else -> Color.Transparent
    }

    val textColor = when (state) {
        "yes" -> Color.White
        "no" -> Color.White
        else -> MaterialTheme.colorScheme.onSurface
    }

    Box(
        modifier = Modifier
            .padding(4.dp)
            .size(44.dp)
            .clip(CircleShape)
            .background(bgColor)
            .then(
                if (isToday)
                    Modifier.border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    )
                else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = displayDay,
            fontSize = 15.sp,
            fontWeight = if (isToday) FontWeight.Bold else FontWeight.Medium,
            color = textColor
        )
    }
}
