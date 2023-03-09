package com.applid.musicbox.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.applid.musicbox.R
import com.applid.musicbox.ui.helpers.ViewContext

@Composable
fun RateUsDialog(
    context: ViewContext,
    onDismissRequest: (isAccepted : Boolean) -> Unit,
) {
    ScaffoldDialog(
        onDismissRequest = {onDismissRequest(false)},
        title = {
            Text( context.symphony.t.rateUsTitle
            )
        },
        content = {
            Box(modifier = Modifier.padding(16.dp, 12.dp)) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Image(
                        painter = painterResource(id = R.drawable.rate_us_stars),
                        contentDescription = ""
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        textAlign = TextAlign.Center,

                        text =  context.symphony.t.rateUsContent
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    ElevatedButton(
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp),
                        onClick = {
                            onDismissRequest(true)
                        },

                    ) {
                        Text(
                            fontWeight = FontWeight.Bold,
                            text = context.symphony.t.rateUsOnGooglePlay)
                    }
                    Button(
                        onClick = {
                            onDismissRequest(false)
                        },
                        colors = ButtonDefaults.buttonColors(
                            Color.Transparent,
                            contentColor = Color.Gray
                        )
                    ) {
                        Text(
                            style = MaterialTheme.typography.titleSmall,
                            text = context.symphony.t.notNow
                        )
                    }

                }


            }
        }
    )
}
