package com.irfandev.todo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.irfandev.todo.data.local.entity.StreakEntity
import com.irfandev.todo.data.repository.StreakRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class StreakViewModel(
    private val repository: StreakRepository
) : ViewModel() {

    private val dateKeyFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val monthFormatter = SimpleDateFormat("yyyy-MM-01", Locale.getDefault())

    private val _uiState = MutableStateFlow(
        buildUiState(Date(), emptyList())
    )
    val uiState: StateFlow<StreakUiState> = _uiState.asStateFlow()

    private var allStreaks: List<StreakEntity> = emptyList()

    init {
        repository.getAllStreaks()
            .onEach { list ->
                allStreaks = list
                val current = _uiState.value.currentMonth
                _uiState.value = buildUiState(current, allStreaks)
            }
            .launchIn(viewModelScope)
    }

    fun markDay(dayDateStr: String, yes: Boolean) {
        viewModelScope.launch {
            val status = if (yes) "yes" else "no"
            repository.insert(StreakEntity(date = dayDateStr, status = status))
            refreshUi()
        }
    }

    fun previousMonth() {
        val cal = Calendar.getInstance()
        cal.time = _uiState.value.currentMonth
        cal.add(Calendar.MONTH, -1)
        _uiState.value = buildUiState(cal.time, allStreaks)
    }

    fun nextMonth() {
        val cal = Calendar.getInstance()
        cal.time = _uiState.value.currentMonth
        cal.add(Calendar.MONTH, 1)
        _uiState.value = buildUiState(cal.time, allStreaks)
    }

    private fun refreshUi() {
        val current = _uiState.value.currentMonth
        _uiState.value = buildUiState(current, allStreaks)
    }

    private fun buildUiState(month: Date, streakEntities: List<StreakEntity>?): StreakUiState {
        val cal = Calendar.getInstance()
        cal.time = month

        // normalize to 1st day of month
        cal.set(Calendar.DAY_OF_MONTH, 1)
        val firstOfMonth = cal.time
        val daysInMonthCount = cal.getActualMaximum(Calendar.DAY_OF_MONTH)

        val daysList = mutableListOf<String>()
        for (d in 1..daysInMonthCount) {
            cal.set(Calendar.DAY_OF_MONTH, d)
            daysList.add(dateKeyFormat.format(cal.time))
        }

        // ✅ Collect all streaks ever recorded (not just current month)
        val allStreaks = (streakEntities ?: emptyList())
        val streakMap = allStreaks.associate { it.date to it.status }.toMutableMap()

        // Only display relevant month days, but keep map for streak count logic
        val dayStates = daysList.associateWith { streakMap[it] ?: "none" }

        // ✅ Proper streak count calculation — continuous "yes" days up to today
        val todayStr = dateKeyFormat.format(Date())
        var streakCount = 0
        val todayCal = Calendar.getInstance()

        while (true) {
            val dStr = dateKeyFormat.format(todayCal.time)
            val status = streakMap[dStr]
            if (status == "yes") {
                streakCount++
                todayCal.add(Calendar.DAY_OF_MONTH, -1)
            } else {
                break
            }
        }

        return StreakUiState(
            currentMonth = firstOfMonth,
            daysInMonth = daysList,
            today = todayStr,
            dayStates = dayStates,
            streakCount = streakCount
        )
    }
}
