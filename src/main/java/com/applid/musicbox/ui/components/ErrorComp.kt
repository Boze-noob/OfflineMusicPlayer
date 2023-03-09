package com.applid.musicbox.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.applid.musicbox.services.i18n.CommonTranslations

@Composable
fun ErrorComp(message: String, stackTrace: String) {
    Column(
        modifier = Modifier
            .background(Color.Red)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        val normalTextStyle = TextStyle(color = Color.White)
        val boldTextStyle = normalTextStyle.copy(fontWeight = FontWeight.Bold)
        ProvideTextStyle(value = normalTextStyle) {
            Text(
                ":(",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = boldTextStyle.copy(fontSize = 40.sp),
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                CommonTranslations.somethingWentHorriblyWrong,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = boldTextStyle,
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                CommonTranslations.errorX(message),
                style = boldTextStyle,
            )
            Text(stackTrace)
        }
    }
}