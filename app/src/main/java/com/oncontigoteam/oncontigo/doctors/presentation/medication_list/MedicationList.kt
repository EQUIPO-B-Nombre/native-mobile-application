package com.oncontigoteam.oncontigo.doctors.presentation.medication_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun MedicationListDialog(
    patientName: String,
    patientDni: String,
    patientId: Long,
    viewModel: MedicationListViewModel,
    onDismiss: () -> Unit,
    onAddPrescription: () -> Unit
) {
    val prescriptions by viewModel.prescriptions
    val isLoading by viewModel.isLoading
    val error by viewModel.error

    LaunchedEffect(patientId) {
        viewModel.loadPrescriptions(patientId)
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFA9B8FF), shape = RoundedCornerShape(40.dp)),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFA9B8FF))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Medicamentos",
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "Nombre completo:",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = patientName,
                        color = Color.Black,
                        fontSize = 18.sp
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "DNI:",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = patientDni,
                        color = Color.Black,
                        fontSize = 18.sp
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                // Tabla de medicamentos
                Box(
                    modifier = Modifier
                        .background(Color(0xFF7D8CC4), shape = RoundedCornerShape(16.dp))
                        .fillMaxWidth()
                ) {
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF44476A)),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Medicamento",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f).padding(8.dp),
                                fontSize = 16.sp
                            )
                            Text(
                                text = "Instrucciones",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f).padding(8.dp),
                                fontSize = 16.sp
                            )
                        }
                        if (isLoading) {
                            Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator()
                            }
                        } else if (error != null) {
                            Text(
                                text = error ?: "Error",
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(16.dp)
                            )
                        } else if (prescriptions.isEmpty()) {
                            Text(
                                text = "No hay medicamentos recetados",
                                color = Color.Black,
                                modifier = Modifier.padding(16.dp)
                            )
                        } else {
                            prescriptions.forEach { prescription ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color.White),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = prescription.medicationName,
                                        color = Color.Black,
                                        modifier = Modifier.weight(1f).padding(8.dp),
                                        fontSize = 15.sp
                                    )
                                    Text(
                                        text = prescription.dosage,
                                        color = Color.Black,
                                        modifier = Modifier.weight(1f).padding(8.dp),
                                        fontSize = 15.sp
                                    )
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Button(
                        onClick = onAddPrescription,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF44476A),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Agregar")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { /* editar */ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF44476A),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { /* eliminar */ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF44476A),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF44476A),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Cerrar", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

