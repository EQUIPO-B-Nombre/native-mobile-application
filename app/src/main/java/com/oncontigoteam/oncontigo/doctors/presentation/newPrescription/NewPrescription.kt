package com.oncontigoteam.oncontigo.doctors.presentation.newPrescription

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.oncontigoteam.oncontigo.doctors.presentation.newPatient.NewPatientSuccess
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewPrescription(
    onDismiss: () -> Unit,
    doctorId: Long,
    selectedPatientId: Long,
    viewModel: NewPrescriptionViewModel,
    reloadTrigger: Int
) {
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }
    val dateTimeFormatter = remember { DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm") }
    val showSuccessDialog by viewModel.showSuccessDialog
    val state by viewModel.state

    if (showSuccessDialog) {
        NewPrescriptionSuccess(
            onDismiss = {
                viewModel.hideSuccessDialog()
                onDismiss()
            }
        )
    }

    LaunchedEffect(selectedPatientId, reloadTrigger) {
        viewModel.setSelectedPatientId(selectedPatientId)
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(Color(0xFFA9B8FF), shape = RoundedCornerShape(40.dp)),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFA9B8FF))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Text(
                    text = "Nueva Prescripción",
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Ingrese el nombre y la dosis del medicamento",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF424242),
                    textAlign = TextAlign.Center
                )

                TextField(
                    value = viewModel.name.value,
                    onValueChange = { viewModel.updateName(it) },
                    label = { Text("Nombre del Medicamento") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, shape = RoundedCornerShape(24.dp)),
                    shape = RoundedCornerShape(24.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        disabledContainerColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    )
                )

                TextField(
                    value = viewModel.dosage.value,
                    onValueChange = { viewModel.updateDosage(it) },
                    label = { Text("Dosis") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, shape = RoundedCornerShape(24.dp)),
                    shape = RoundedCornerShape(24.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        disabledContainerColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    )
                )

                Text(
                    text = "Seleccione fecha de inicio",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF424242),
                    textAlign = TextAlign.Center
                )
                OutlinedButton(
                    onClick = { showStartDatePicker = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.White,
                        contentColor = Color(0xFF44476A)
                    )
                ) {
                    Text(
                        text = viewModel.startDate.value.format(dateTimeFormatter),
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = "Seleccione fecha de fin",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF424242),
                    textAlign = TextAlign.Center
                )
                OutlinedButton(
                    onClick = { showEndDatePicker = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.White,
                        contentColor = Color(0xFF44476A)
                    )
                ) {
                    Text(
                        text = viewModel.endDate.value.format(dateTimeFormatter),
                        fontWeight = FontWeight.Bold
                    )
                }

                if (showStartDatePicker) {
                    val initialMillis = viewModel.startDate.value
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
                        .toEpochMilli()
                    val datePickerState = rememberDatePickerState(
                        initialSelectedDateMillis = initialMillis
                    )
                    DatePickerDialog(
                        onDismissRequest = { showStartDatePicker = false },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    datePickerState.selectedDateMillis?.let { millis ->
                                        val selectedDateTime = LocalDateTime.ofInstant(
                                            java.time.Instant.ofEpochMilli(millis),
                                            ZoneId.systemDefault()
                                        )
                                        viewModel.updateStartDate(selectedDateTime)
                                    }
                                    showStartDatePicker = false
                                }
                            ) {
                                Text("OK")
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = { showStartDatePicker = false }
                            ) {
                                Text("Cancelar")
                            }
                        }
                    ) {
                        DatePicker(state = datePickerState)
                    }
                }

                if (showEndDatePicker) {
                    val initialMillis = viewModel.endDate.value
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
                        .toEpochMilli()
                    val datePickerState = rememberDatePickerState(
                        initialSelectedDateMillis = initialMillis
                    )
                    DatePickerDialog(
                        onDismissRequest = { showEndDatePicker = false },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    datePickerState.selectedDateMillis?.let { millis ->
                                        val selectedDateTime = LocalDateTime.ofInstant(
                                            java.time.Instant.ofEpochMilli(millis),
                                            ZoneId.systemDefault()
                                        )
                                        viewModel.updateEndDate(selectedDateTime)
                                    }
                                    showEndDatePicker = false
                                }
                            ) {
                                Text("OK")
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = { showEndDatePicker = false }
                            ) {
                                Text("Cancelar")
                            }
                        }
                    ) {
                        DatePicker(state = datePickerState)
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF44476A),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Cancelar", fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(
                        onClick = { viewModel.createPrescription(doctorId) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF44476A),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Ok", fontWeight = FontWeight.Bold)
                    }
                }

                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                } else if (state.message.isNotEmpty()) {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}