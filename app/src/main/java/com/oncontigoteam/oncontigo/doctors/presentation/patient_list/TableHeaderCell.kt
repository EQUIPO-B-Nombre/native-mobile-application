package com.oncontigoteam.oncontigo.doctors.presentation.patient_list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TableHeaderCell(text: String, modifier: Modifier) {
    Box(
        modifier = modifier.padding(4.dp)
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            fontSize = 12.sp
        )
    }
}