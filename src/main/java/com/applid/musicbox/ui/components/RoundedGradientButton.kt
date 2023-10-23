package com.applid.musicbox.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun RoundedGradientButton (
    text: String?,
    colors: List<Color>,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { },
    icon: ImageVector?
) {
    val gradient = Brush.horizontalGradient(colors);

    Button(
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues(),
        onClick = { onClick() },
        shape = RoundedCornerShape(50),


    ) {
        Box(
            modifier = Modifier
                .background(gradient)
                .then(modifier),


        ) {
            Row(
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if(text != null)  Text(text = text)
                if(icon != null) Icon(imageVector = icon, contentDescription = icon.name, Modifier.size(18.dp))
            }
        }
    }
}