package com.oncontigoteam.oncontigo.doctors.presentation.newTreatment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun NewTreatmentSuccess(
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color(0xFFA9B8FF), shape = RoundedCornerShape(40.dp)),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFA9B8FF))
        ) {
            Column(
                modifier = Modifier
                    .padding(32.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Text(
                    text = "¡Tratamiento agregado!",
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "El tratamiento ha sido agregado exitosamente a tu paciente",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = Color(0xFF424242)
                )

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF44476A),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Aceptar", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

