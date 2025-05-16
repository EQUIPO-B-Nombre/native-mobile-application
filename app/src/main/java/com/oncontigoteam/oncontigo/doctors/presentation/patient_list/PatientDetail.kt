package com.oncontigoteam.oncontigo.doctors.presentation.patient_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import com.oncontigoteam.oncontigo.R

@Composable
fun PatientDetailDialog(patient: PatientData, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp)
                .background(Color(0xFFA9B8FF), shape = RoundedCornerShape(40.dp)),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFA9B8FF))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Text(
                    text = "Detalles del Paciente",
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Información del paciente
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 16.dp)
                        ) {
                            PatientInfoItem("Nombre", "${patient.firstName} ${patient.lastName}")
                            PatientInfoItem("DNI", patient.dni)
                            patient.phone?.let { PatientInfoItem("Teléfono", it) }
                            patient.city?.let { PatientInfoItem("Ciudad", it) }
                            patient.country?.let { PatientInfoItem("País", it) }
                            patient.description?.let { PatientInfoItem("Descripción", it) }
                        }

                        // Foto del paciente
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                        ) {
                            GlideImage(
                                modifier = Modifier
                                    .size(150.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE0E0E0)),
                                imageModel = {
                                    patient.photo.ifBlank { R.drawable.ic_launcher_background }
                                },
                                imageOptions = ImageOptions(
                                    contentScale = ContentScale.Crop,
                                    alignment = Alignment.Center
                                )
                            )
                        }
                    }
                }

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF44476A),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        "Cerrar",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun PatientInfoItem(label: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color(0xFF757575),
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value,
            fontSize = 16.sp,
            color = Color(0xFF424242),
            fontWeight = FontWeight.Normal
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}