package com.example.taskyy.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskyy.R
import com.example.taskyy.ui.events.ReminderEvent
import com.example.taskyy.ui.viewmodels.ReminderState

@Composable
fun ReminderScreen(state: ReminderState, onEvent: (ReminderEvent) -> Unit) {
    Scaffold(
        content = {
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .padding(it.calculateLeftPadding(layoutDirection = LayoutDirection.Ltr))
            ) {
                TopBar(dateString = state.dateString, onEvent = onEvent)
                Surface(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(),
                    shape = AbsoluteRoundedCornerShape(30.dp, 30.dp)
                ) {
                    Body(itemBoxTitle = "", reminderText = "", dateString = state.dateString)
                }
            }
        }
    )
}

@Composable
fun Body(itemBoxTitle: String, reminderText: String, dateString: String) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 25.dp, start = 20.dp)
    ) {
        ItemBox("#f2f6ff", 30, borderColor = "#d0d2d9")
        ItemBoxTitle("Reminder")
    }
    Column {
        NewItemRow()
        RowDivider(30)
        ItemDescription()
        RowDivider(25)
        TimeAndDateRow(dateString = dateString)
        RowDivider(topPadding = 25)
        Alarm()
    }
}

@Composable
fun Alarm() {
    Row(
        modifier =
        Modifier
            .padding(start = 20.dp, top = 20.dp)
            .clickable {
                //navigate to alarm selection screen
            }
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AlarmIconWithBackground()
        Text(
            text = "1 hour before",
            color = Color.Black,
            fontSize = 15.sp,
            modifier = Modifier.padding(start = 5.dp)
        )
        ChevronForward(0)
    }
}

@Composable
fun TimeAndDateRow(dateString: String) {
    //two columns inside one row. One column for time and one for date
    //need time and date pickers
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(start = 20.dp, top = 20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable {
                //navigate to time picker
            }
        ) {
            Text(
                text = "From",
                modifier = Modifier.padding(end = 40.dp),
                fontSize = 15.sp
            )
            Text(
                text = "02:33",
                modifier = Modifier.padding(end = 40.dp),
                fontSize = 15.sp
            )
            Image(
                painter = painterResource(id = R.drawable.chevron_forward),
                contentDescription = "proceed forward arrow",
                modifier = Modifier.padding(end = 40.dp)
            )
        }
        Row(
            modifier = Modifier.clickable {
                //navigate to date picker
            }
        ) {
            Text(
                text = dateString,
                color = Color.Black,
                fontSize = 15.sp
            )

            ChevronForward(topPadding = 0)
        }
    }
}

@Composable
fun ItemDescription() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable
        {
            //navigate to item description page
        }
    ) {
        Text(
            text = "Reminder Description",
            modifier = Modifier.padding(start = 20.dp, top = 20.dp),
            fontSize = 15.sp
        )
        ChevronForward(20)
    }
}

@Composable
fun RowDivider(topPadding: Int) {
    Row(
        modifier = Modifier.padding(top = topPadding.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp) // Adjust the height of the divider as needed
                .padding(
                    bottom = 4.dp,
                    start = 20.dp,
                    end = 20.dp
                ) // Adjust the padding to control the distance between the divider and the content
        ) {
            HorizontalDivider(color = Color.LightGray) // Divider with black color
        }
    }
}

@Composable
fun NewItemRow() {
    Row(
        modifier =
        Modifier
            .padding(top = 100.dp, start = 20.dp)
            .clickable {
                //navigate to name screen
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        WhiteCircleWithBlackBorder()
        Text(
            text = "New Reminder",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 10.dp)
        )
        ChevronForward(0)
    }
}

@Composable
fun ChevronForward(topPadding: Int) {
    Column(
        horizontalAlignment = Alignment.End,
        modifier = Modifier
            .padding(top = topPadding.dp)
            .fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = R.drawable.chevron_forward),
            contentDescription = "proceed forward arrow",
            modifier = Modifier.padding(end = 15.dp)
        )
    }
}

@Composable
fun AlarmIconWithBackground() {
    Box(
        modifier = Modifier
            .size(32.dp) // Adjust the size of the vector asset as needed
            .background(
                color = Color(android.graphics.Color.parseColor("#f2f6fc")),
                shape = AbsoluteRoundedCornerShape(5.dp, 5.dp, 5.dp, 5.dp)
            ),
        contentAlignment = Alignment.Center

    ) {
        Image(
            painter = painterResource(
                id = R.drawable.alarm_icon
            ),
            contentDescription = "Alarm icon",
            colorFilter = ColorFilter.tint(color = Color(android.graphics.Color.parseColor("#aeb4bf")))
        )
    }
}

@Composable
fun WhiteCircleWithBlackBorder() {
    Box(
        modifier = Modifier
            .size(20.dp) // Adjust the size of the circle
            .background(color = Color.White, shape = CircleShape)
            .border(width = 2.dp, color = Color.Black, shape = CircleShape)
    )
}

@Composable
fun ItemBoxTitle(title: String) {
    Text(
        text = title,
        fontSize = 18.sp,
        color = Color.Black,
        modifier = Modifier.padding(start = 10.dp, top = 5.dp)
    )
}

@Composable
fun ItemBox(hexColor: String, size: Int, borderColor: String) {
    Box(
        modifier =
        Modifier
            .background(
                color = Color(android.graphics.Color.parseColor(hexColor)),
                shape = AbsoluteRoundedCornerShape(5.dp, 5.dp, 5.dp, 5.dp)
            )
            .border(
                width = 2.dp,
                color = Color(android.graphics.Color.parseColor(borderColor)),
                shape = AbsoluteRoundedCornerShape(5.dp, 5.dp, 5.dp, 5.dp)
            )
            .size(size.dp)
    )
}

@Composable
fun TopBar(dateString: String, onEvent: (ReminderEvent) -> Unit) {
    Row(
        modifier =
        Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        LaunchedEffect(Unit) {
            onEvent(ReminderEvent.SetDateString)
        }
        Text(
            text = "X",
            fontSize = 20.sp,
            color = Color.White,
            modifier =
            Modifier
                .clickable {
                    //onEvent(ReminderEvent.Close)
                    //navigate to backstack
                }
                .padding(start = 10.dp)
        )
        Text(
            text = dateString,
            fontSize = 25.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "SAVE",
            fontSize = 15.sp,
            color = Color.White,
            modifier =
            Modifier
                .clickable {
                    //onEvent(ReminderEvent.Save)
                    //save item
                }
                .padding(end = 10.dp, top = 5.dp)
        )
    }
}
