package com.irfandev.todo.ui.calendar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.irfandev.todo.viewmodel.StreakViewModel
import java.text.SimpleDateFormat
import java.util.*
@Composable
fun CalendarScreen(
    modifier: Modifier = Modifier,
    streakViewModel: StreakViewModel
) {
    val uiState by streakViewModel.uiState.collectAsState()
    val currentMonth = uiState.currentMonth
    val today = uiState.today

    val streakCount = uiState.streakCount
    val quote = remember(streakCount) { getMotivationalQuote(streakCount) }

    val currentMonthStr = SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(currentMonth)
    val todayMonthStr = SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(Date())
    val isCurrentMonth = currentMonthStr == todayMonthStr

    // Calculate leading empty cells for alignment
    val calendar = Calendar.getInstance().apply {
        time = currentMonth
        set(Calendar.DAY_OF_MONTH, 1)
    }
    val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) // Sunday = 1
    val leadingEmptyCells = (firstDayOfWeek - Calendar.SUNDAY) % 7

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(start = 12.dp, end = 12.dp, top = 50.dp, bottom = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Month Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = { streakViewModel.previousMonth() }) {
                Text("< Prev")
            }
            Text(
                text = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(currentMonth),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            TextButton(onClick = { streakViewModel.nextMonth() }) {
                Text("Next >")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Weekday Header
        val daysOfWeek = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            daysOfWeek.forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        // Calendar Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(4.dp)
        ) {
            // Add empty cells before the first day
            items(leadingEmptyCells) {
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .padding(4.dp)
                ) { /* Empty cell */ }
            }

            // Render actual days
            items(uiState.daysInMonth) { day ->
                DayCell(
                    day = day,
                    today = today,
                    state = uiState.dayStates[day],
                    onYes = { streakViewModel.markDay(day, true) },
                    onNo = { streakViewModel.markDay(day, false) }
                )
            }
        }

        // Yes/No Buttons
        AnimatedVisibility(
            visible = isCurrentMonth,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Row(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = {
                    streakViewModel.markDay(today, true)
                }) {
                    Text("Yes")
                }
                Button(onClick = {
                    streakViewModel.markDay(today, false)
                }) {
                    Text("No")
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        //Streak Card
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "$streakCount-Day Streak 🔥",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = quote,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )
            }
        }
    }
}


private fun getMotivationalQuote(streak: Int): String {
    return when (streak) {
        0 -> "Start strong today — 'Indeed, with hardship comes ease.' (Quran 94:6)"
        1 -> "'Indeed, Allah does not change the condition of a people until they change what is in themselves.' (Quran 13:11)"
        2 -> "'Say, My Lord, increase me in knowledge.' (Quran 20:114)"
        3 -> "'If you are grateful, I will surely increase your favor upon you.' (Quran 14:7)"
        4 -> "'And those who strive for Us – We will surely guide them to Our ways.' (Quran 29:69)"
        5 -> "'Do not grieve. Indeed, Allah is with us.' (Quran 9:40)"
        6 -> "'So truly where there is hardship, there is also ease.' (Quran 94:6)"
        7 -> "'Indeed, the ones who have believed and done righteous deeds – We will not allow the reward of anyone who does a good deed to be lost.' (Quran 18:30)"
        8 -> "'Indeed, Allah is with those who fear Him and those who are doers of good.' (Quran 16:128)"
        9 -> "'And be patient, for indeed, Allah is with the patient.' (Quran 8:46)"
        10 -> "'You are the best nation brought forth for mankind. You enjoin what is right and forbid what is wrong.' (Quran 3:110)"
        11 -> "'Indeed, Allah is with those who are patient and trust in Him.' (Quran 2:153)"
        12 -> "'Have you seen the water that you drink? Is it you who brought it down from the clouds, or is it We who do?' (Quran 56:68-69)"
        13 -> "'Who is it that provides for you from the heaven and the earth? Say, 'Allah.' (Quran 35:3)"
        14 -> "'So be patient. Indeed, the promise of Allah is truth.' (Quran 30:60)"
        15 -> "'Indeed, with hardship comes ease.' (Quran 94:6)"
        16 -> "'Allah does not burden a soul beyond that it can bear.' (Quran 2:286)"
        17 -> "'Indeed, your ally is none but Allah and His Messenger and those who have believed — those who establish prayer and give zakah, and they bow [in worship].' (Quran 5:55)"
        18 -> "'Do not kill yourselves. Indeed, Allah is ever Merciful to you.' (Quran 4:29)"
        19 -> "'Say, 'In the bounty of Allah and in His mercy – in that let them rejoice; it is better than what they accumulate.' (Quran 10:58)"
        20 -> "'Indeed, Allah commands you to fulfill your trusts to whom they are due.' (Quran 4:58)"
        21 -> "'Indeed, Allah is with those who fear Him and those who are doers of good.' (Quran 16:128)"
        22 -> "'So be patient, for indeed, the promise of Allah is truth.' (Quran 30:60)"
        23 -> "'And those who strive for Us – We will surely guide them to Our ways.' (Quran 29:69)"
        24 -> "'Allah is the Light of the heavens and the earth.' (Quran 24:35)"
        25 -> "'Indeed, the help of Allah is near.' (Quran 2:214)"
        26 -> "'Say, 'My Lord, increase me in knowledge.' (Quran 20:114)"
        27 -> "'Whoever works righteousness, whether male or female, while he is a believer – We will surely cause him to live a good life.' (Quran 16:97)"
        28 -> "'And He found you lost and guided [you].' (Quran 93:7)"
        29 -> "'If you are grateful, I will surely increase your favor upon you.' (Quran 14:7)"
        30 -> "'Indeed, Allah is with those who fear Him and those who are doers of good.' (Quran 16:128)"
        31 -> "'And do not grieve. Indeed, Allah is with us.' (Quran 9:40)"
        32 -> "'Indeed, Allah is with those who fear Him and those who are doers of good.' (Quran 16:128)"
        33 -> "'Indeed, Allah does not change the condition of a people until they change what is in themselves.' (Quran 13:11)"
        34 -> "'And be patient, for indeed, Allah is with the patient.' (Quran 8:46)"
        35 -> "'Indeed, Allah is with those who fear Him and those who are doers of good.' (Quran 16:128)"
        36 -> "'Indeed, with hardship comes ease.' (Quran 94:6)"
        37 -> "'Indeed, Allah does not change the condition of a people until they change what is in themselves.' (Quran 13:11)"
        38 -> "'And those who strive for Us – We will surely guide them to Our ways.' (Quran 29:69)"
        39 -> "'Say, 'In the bounty of Allah and in His mercy – in that let them rejoice; it is better than what they accumulate.' (Quran 10:58)"
        40 -> "'Do not grieve. Indeed, Allah is with us.' (Quran 9:40)"
        41 -> "'Say, 'My Lord, increase me in knowledge.' (Quran 20:114)"
        42 -> "'Indeed, the help of Allah is near.' (Quran 2:214)"
        43 -> "'Whoever works righteousness, whether male or female, while he is a believer – We will surely cause him to live a good life.' (Quran 16:97)"
        44 -> "'Indeed, Allah commands you to fulfill your trusts to whom they are due.' (Quran 4:58)"
        45 -> "'If you are grateful, I will surely increase your favor upon you.' (Quran 14:7)"
        46 -> "'Indeed, Allah is with those who fear Him and those who are doers of good.' (Quran 16:128)"
        47 -> "'And He found you lost and guided [you].' (Quran 93:7)"
        48 -> "'Indeed, with hardship comes ease.' (Quran 94:6)"
        49 -> "'And be patient, for indeed, Allah is with the patient.' (Quran 8:46)"
        50 -> "'Indeed, Allah does not change the condition of a people until they change what is in themselves.' (Quran 13:11)"
        51 -> "'Say, My Lord, increase me in knowledge.' (Quran 20:114)"
        52 -> "'And those who strive for Us – We will surely guide them to Our ways.' (Quran 29:69)"
        53 -> "'Indeed, the ones who have believed and done righteous deeds – We will not allow the reward of anyone who does a good deed to be lost.' (Quran 18:30)"
        54 -> "'Indeed, Allah is with those who fear Him and those who are doers of good.' (Quran 16:128)"
        55 -> "'If you are grateful, I will surely increase your favor upon you.' (Quran 14:7)"
        56 -> "'Indeed, Allah is with those who fear Him and those who are doers of good.' (Quran 16:128)"
        57 -> "'Do not grieve. Indeed, Allah is with us.' (Quran 9:40)"
        58 -> "'Say, 'In the bounty of Allah and in His mercy – in that let them rejoice; it is better than what they accumulate.' (Quran 10:58)"
        59 -> "'Indeed, with hardship comes ease.' (Quran 94:6)"
        60 -> "'Indeed, Allah does not change the condition of a people until they change what is in themselves.' (Quran 13:11)"
        61 -> "'And those who strive for Us – We will surely guide them to Our ways.' (Quran 29:69)"
        62 -> "'Say, 'My Lord, increase me in knowledge.' (Quran 20:114)"
        63 -> "'Indeed, the ones who have believed and done righteous deeds – We will not allow the reward of anyone who does a good deed to be lost.' (Quran 18:30)"
        64 -> "'Do not grieve. Indeed, Allah is with us.' (Quran 9:40)"
        65 -> "'And He found you lost and guided [you].' (Quran 93:7)"
        66 -> "'Indeed, with hardship comes ease.' (Quran 94:6)"
        67 -> "'If you are grateful, I will surely increase your favor upon you.' (Quran 14:7)"
        68 -> "'Indeed, Allah does not change the condition of a people until they change what is in themselves.' (Quran 13:11)"
        69 -> "'And those who strive for Us – We will surely guide them to Our ways.' (Quran 29:69)"
        70 -> "'Say, 'In the bounty of Allah and in His mercy – in that let them rejoice; it is better than what they accumulate.' (Quran 10:58)"
        71 -> "'Indeed, the help of Allah is near.' (Quran 2:214)"
        72 -> "'Indeed, Allah is with those who fear Him and those who are doers of good.' (Quran 16:128)"
        73 -> "'Do not grieve. Indeed, Allah is with us.' (Quran 9:40)"
        74 -> "'Indeed, Allah is with those who fear Him and those who are doers of good.' (Quran 16:128)"
        75 -> "'Indeed, with hardship comes ease.' (Quran 94:6)"
        76 -> "'Say, 'My Lord, increase me in knowledge.' (Quran 20:114)"
        77 -> "'If you are grateful, I will surely increase your favor upon you.' (Quran 14:7)"
        78 -> "'Indeed, the ones who have believed and done righteous deeds – We will not allow the reward of anyone who does a good deed to be lost.' (Quran 18:30)"
        79 -> "'And those who strive for Us – We will surely guide them to Our ways.' (Quran 29:69)"
        80 -> "'Indeed, Allah is with those who fear Him and those who are doers of good.' (Quran 16:128)"
        81 -> "'Indeed, Allah commands you to fulfill your trusts to whom they are due.' (Quran 4:58)"
        82 -> "'Say, 'In the bounty of Allah and in His mercy – in that let them rejoice; it is better than what they accumulate.' (Quran 10:58)"
        83 -> "'Indeed, with hardship comes ease.' (Quran 94:6)"
        84 -> "'Indeed, Allah does not change the condition of a people until they change what is in themselves.' (Quran 13:11)"
        85 -> "'Do not grieve. Indeed, Allah is with us.' (Quran 9:40)"
        86 -> "'Say, 'My Lord, increase me in knowledge.' (Quran 20:114)"
        87 -> "'Indeed, the ones who have believed and done righteous deeds – We will not allow the reward of anyone who does a good deed to be lost.' (Quran 18:30)"
        88 -> "'And He found you lost and guided [you].' (Quran 93:7)"
        89 -> "'Indeed, Allah is with those who fear Him and those who are doers of good.' (Quran 16:128)"
        90 -> "'If you are grateful, I will surely increase your favor upon you.' (Quran 14:7)"
        91 -> "'And those who strive for Us – We will surely guide them to Our ways.' (Quran 29:69)"
        92 -> "'Indeed, with hardship comes ease.' (Quran 94:6)"
        93 -> "'Say, 'In the bounty of Allah and in His mercy – in that let them rejoice; it is better than what they accumulate.' (Quran 10:58)"
        94 -> "'Indeed, Allah is with those who fear Him and those who are doers of good.' (Quran 16:128)"
        95 -> "'And be patient, for indeed, Allah is with the patient.' (Quran 8:46)"
        96 -> "'Indeed, Allah does not change the condition of a people until they change what is in themselves.' (Quran 13:11)"
        97 -> "'Say, My Lord, increase me in knowledge.' (Quran 20:114)"
        98 -> "'Indeed, with hardship comes ease.' (Quran 94:6)"
        99 -> "'And those who strive for Us – We will surely guide them to Our ways.' (Quran 29:69)"
        100 -> "'Do not grieve. Indeed, Allah is with us.' (Quran 9:40)"
        else -> "Keep going — 'Indeed, with hardship comes ease.' (Quran 94:6)"
    }
}

