package com.oncontigoteam.oncontigo.doctors.presentation.treatment_list

import android.os.Build
import androidx.annotation.RequiresApi
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

import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TreatmentListDialog(
    doctorId: Long,
    patientId: Long,
    viewModel: TreatmentListViewModel,
    onDismiss: () -> Unit,
    onAddTreatment: () -> Unit,
    onAddProcedure: () -> Unit,
    reloadTreatmentTrigger : Int,
    reloadProcedureTrigger: Int
) {
    val treatments by viewModel.treatments
    val procedures by viewModel.procedures
    val isLoading by viewModel.isLoading
    val error by viewModel.error

    val dateFormatter = remember { DateTimeFormatter.ofPattern("dd/MM/yyyy") }

    LaunchedEffect(doctorId, patientId, reloadTreatmentTrigger, reloadProcedureTrigger) {
        viewModel.loadData(doctorId, patientId)
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
                // Tratamientos
                Text(
                    text = "Tratamientos",
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
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
                                text = "Tratamiento",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f).padding(8.dp),
                                fontSize = 16.sp
                            )
                            Text(
                                text = "Fecha de inicio",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f).padding(8.dp),
                                fontSize = 16.sp
                            )
                            Text(
                                text = "Fecha de fin",
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
                        } else if (treatments.isEmpty()) {
                            Text(
                                text = "No hay tratamientos registrados",
                                color = Color.Black,
                                modifier = Modifier.padding(16.dp)
                            )
                        } else {
                            treatments.forEach { treatment ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color.White),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = treatment.name,
                                        color = Color.Black,
                                        modifier = Modifier.weight(1f).padding(8.dp),
                                        fontSize = 15.sp
                                    )
                                    Text(
                                        text = treatment.startDate.format(dateFormatter),
                                        color = Color.Black,
                                        modifier = Modifier.weight(1f).padding(8.dp),
                                        fontSize = 15.sp
                                    )
                                    Text(
                                        text = treatment.endDate.format(dateFormatter),
                                        color = Color.Black,
                                        modifier = Modifier.weight(1f).padding(8.dp),
                                        fontSize = 15.sp
                                    )
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Button(
                        onClick = onAddTreatment,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF44476A),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Agregar tratamiento")
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
                        Icon(Icons.Default.Edit, contentDescription = "Editar tratamiento")
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
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar tratamiento")
                    }
                }

                // Procedimientos
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Procedimiento",
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
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
                                text = "Procedimiento",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f).padding(8.dp),
                                fontSize = 16.sp
                            )
                            Text(
                                text = "Fecha",
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
                        } else if (procedures.isEmpty()) {
                            Text(
                                text = "No hay procedimientos registrados",
                                color = Color.Black,
                                modifier = Modifier.padding(16.dp)
                            )
                        } else {
                            procedures.forEach { procedure ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color.White),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = procedure.name,
                                        color = Color.Black,
                                        modifier = Modifier.weight(1f).padding(8.dp),
                                        fontSize = 15.sp
                                    )
                                    Text(
                                        text = procedure.performedAt.format(dateFormatter),
                                        color = Color.Black,
                                        modifier = Modifier.weight(1f).padding(8.dp),
                                        fontSize = 15.sp
                                    )
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Button(
                        onClick = onAddProcedure,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF44476A),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Agregar procedimiento")
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
                        Icon(Icons.Default.Edit, contentDescription = "Editar procedimiento")
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
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar procedimiento")
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