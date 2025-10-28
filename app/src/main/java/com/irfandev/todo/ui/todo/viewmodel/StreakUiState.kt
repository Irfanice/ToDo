package com.irfandev.todo.viewmodel

import java.util.*

/**
 * UI state exposed by StreakViewModel
 *
 * - currentMonth: Date pointing to the first day of the month displayed (use SimpleDateFormat to format)
 * - daysInMonth: List<String> each as "yyyy-MM-dd" for each day of the current month in order
 * - today: String date "yyyy-MM-dd"
 * - dayStates: Map of date-string -> status ("yes"/"no"/"none")
 * - streakCount: Int (consecutive yes days ending at today)
 */
data class StreakUiState(
    val currentMonth: Date,
    val daysInMonth: List<String>,
    val today: String,
    val dayStates: Map<String, String>,
    val streakCount: Int
)
