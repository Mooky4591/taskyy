package com.example.taskyy.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.taskyy.R
import com.example.taskyy.ui.events.RegisterEvent
import com.example.taskyy.ui.viewmodels.RegisterState

@Composable
fun RegisterScreen(state: RegisterState, onEvent: (RegisterEvent) -> Unit) {
Column(
horizontalAlignment = Alignment.CenterHorizontally,
verticalArrangement = Arrangement.SpaceAround,
modifier = Modifier
    .fillMaxHeight()
    .background(Color.Black)
) {
    TopText(stringResource(R.string.create_your_account))
    Surface(
        modifier = Modifier
            .fillMaxHeight(.80F),
        shape = AbsoluteRoundedCornerShape(30.dp, 30.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxHeight(.75f)
                .padding(30.dp)
        ) {
            CreateEmailField(state, onEvent)
            Spacer(modifier = Modifier.height(10.dp))
            CreatePasswordField(state, onEvent)
            Spacer(modifier = Modifier.height(10.dp))
            CreateLoginButton()
            Row(
            ) {
                CreateBottomText()
                CreateSignUpLink()
            }
        }
    }
}
}