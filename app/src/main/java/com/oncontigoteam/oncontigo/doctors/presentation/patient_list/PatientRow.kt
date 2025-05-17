package com.oncontigoteam.oncontigo.doctors.presentation.patient_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import com.oncontigoteam.oncontigo.R

data class PatientData(
    val id: Long,
    val dni: String,
    val firstName: String,
    val lastName: String,
    val photo: String,
    val country: String?,
    val city: String?,
    val phone: String?,
    val description: String?
)

@Composable
fun PatientRow(patient: PatientData) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Foto
        Box(
            modifier = Modifier
                .width(40.dp)
                .height(40.dp)
                .background(Color.Gray, RoundedCornerShape(4.dp)),
            contentAlignment = Alignment.Center
        ) {
            GlideImage(
                modifier = Modifier.size(32.dp).clip(CircleShape),
                imageModel = { patient.photo.ifBlank { R.drawable.ic_launcher_background } },
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                )
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        // DNI
        Box(
            modifier = Modifier
                .width(150.dp)
                .padding(4.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = patient.dni,
                fontSize = 12.sp
            )
        }
        // Nombre completo
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(4.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = patient.firstName + " " + patient.lastName,
                fontSize = 12.sp
            )
        }
    }
}

