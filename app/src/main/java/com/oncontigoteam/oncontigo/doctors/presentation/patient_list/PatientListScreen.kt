package com.oncontigoteam.oncontigo.doctors.presentation.patient_list

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oncontigoteam.oncontigo.doctors.data.repository.appointment.AppointmentRepository
import com.oncontigoteam.oncontigo.doctors.data.repository.healthtracking.HealthTrackingRepository
import com.oncontigoteam.oncontigo.doctors.data.repository.patient.PatientRepository
import com.oncontigoteam.oncontigo.doctors.data.repository.prescription.PrescriptionRepository
import com.oncontigoteam.oncontigo.doctors.data.repository.procedure.ProcedureRepository
import com.oncontigoteam.oncontigo.doctors.data.repository.profile.ProfileRepository
import com.oncontigoteam.oncontigo.doctors.data.repository.treatment.TreatmentRepository
import com.oncontigoteam.oncontigo.doctors.presentation.appointment_list.AppointmentListDialog
import com.oncontigoteam.oncontigo.doctors.presentation.appointment_list.AppointmentListViewModel
import com.oncontigoteam.oncontigo.doctors.presentation.medication_list.MedicationListDialog
import com.oncontigoteam.oncontigo.doctors.presentation.medication_list.MedicationListViewModel
import com.oncontigoteam.oncontigo.doctors.presentation.newAppointment.NewAppointment
import com.oncontigoteam.oncontigo.doctors.presentation.newAppointment.NewAppointmentViewModel
import com.oncontigoteam.oncontigo.doctors.presentation.newPatient.NewPatient
import com.oncontigoteam.oncontigo.doctors.presentation.newPatient.NewPatientViewModel
import com.oncontigoteam.oncontigo.doctors.presentation.newPrescription.NewPrescription
import com.oncontigoteam.oncontigo.doctors.presentation.newPrescription.NewPrescriptionViewModel
import com.oncontigoteam.oncontigo.doctors.presentation.newProcedure.NewProcedure
import com.oncontigoteam.oncontigo.doctors.presentation.newProcedure.NewProcedureViewModel
import com.oncontigoteam.oncontigo.doctors.presentation.newTreatment.NewTreatment
import com.oncontigoteam.oncontigo.doctors.presentation.newTreatment.NewTreatmentViewModel
import com.oncontigoteam.oncontigo.doctors.presentation.treatment_list.TreatmentListDialog
import com.oncontigoteam.oncontigo.doctors.presentation.treatment_list.TreatmentListViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PatientListScreen(
    viewModel: PatientListViewModel,
    prescriptionRepository: PrescriptionRepository,
    appointmentRepository: AppointmentRepository,
    healthTrackingRepository: HealthTrackingRepository,
    treatmentRepository: TreatmentRepository,
    procedureRepository: ProcedureRepository,
    profileRepository: ProfileRepository,
    patientRepository: PatientRepository,
) {
    val patientListState = viewModel.list.value
    var selectedPatient by remember { mutableStateOf<PatientData?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    var showMedicationDialog by remember { mutableStateOf(false) }
    var showTreatmentListDialog by remember { mutableStateOf(false) }
    var showAppointmentListDialog by remember { mutableStateOf(false) }
    var showPrescriptionDialog by remember { mutableStateOf(false) }
    var showAppointmentDialog by remember { mutableStateOf(false) }
    var showTreatmentDialog by remember { mutableStateOf(false) }
    var showProcedureDialog by remember { mutableStateOf(false) }
    var showNewPatientDialog by remember { mutableStateOf(false) }
    var reloadTrigger by remember { mutableStateOf(0) }
    var reloadAppointmentsTrigger by remember { mutableStateOf(0) }
    var reloadPrescriptionsTrigger by remember { mutableStateOf(0) }
    var reloadProceduresTrigger by remember { mutableStateOf(0) }
    var reloadTreatmentsTrigger by remember { mutableStateOf(0) }

    LaunchedEffect(reloadTrigger) {
        viewModel.getPatientList(81)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFDCE5)) // Fondo rosado
            .padding(16.dp)
    ) {
        // Título
        Text(
            text = "Lista de pacientes",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
            // centra el texto

        )

        // Encabezado de la tabla
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF4A547F), RoundedCornerShape(8.dp))
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TableHeaderCell(text = "Foto", modifier = Modifier.width(60.dp))
            TableHeaderCell(text = "DNI", modifier = Modifier.width(120.dp))
            TableHeaderCell(text = "Nombre completo", modifier = Modifier.weight(1f))
        }

        // Lista de pacientes
        if (patientListState.isEmpty()) {
            Text(
                text = "No hay pacientes disponibles",
                modifier = Modifier.padding(16.dp),
                color = Color.Gray
            )
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                itemsIndexed(patientListState) { index, patient ->
                    val isSelected = selectedPatient?.id == patient.id
                    val rowColor = when {
                        isSelected -> Color(0xFFFFFF00) // Color para la fila seleccionada
                        index % 2 == 0 -> Color(0xFF95A8FF) // Azul claro para pares
                        else -> Color(0xFFE3E9FF) // Más claro para impares
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(rowColor, RoundedCornerShape(8.dp))
                            .padding(8.dp)
                            .clickable { selectedPatient = patient },
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        PatientRow(patient = patient)
                    }
                }
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomButton(
                text = "Agregar Paciente",
                onClick = { showNewPatientDialog = true }
            )
            CustomButton(
                text = "Ver detalles",
                onClick = {
                    if (selectedPatient != null) {
                        showDialog = true
                    }
                }
            )
            CustomButton(
                text = "Sus citas médicas",
                onClick = {
                    if (selectedPatient != null) {
                        showAppointmentListDialog = true
                    }
                }
            )
            CustomButton(
                text = "Recetar Medicamento",
                onClick = {
                    if (selectedPatient != null) {
                        showMedicationDialog = true
                    }
                }
            )
            CustomButton(
                text = "Sus Tratamientos",
                onClick = {
                    if (selectedPatient != null) {
                        showTreatmentListDialog = true
                    }
                }
            )
            CustomButton(
                text = "Eliminar Paciente",
                onClick = { /* Acción */ },
                isDelete = true
            )
        }
    }

    // Mostrar el diálogo si showDialog es true
    if (showDialog && selectedPatient != null) {
        PatientDetailDialog(
            patient = selectedPatient!!,
            onDismiss = { showDialog = false }
        )
    }

    val medicationListViewModel = MedicationListViewModel(prescriptionRepository)
    val appointmentViewModel = NewAppointmentViewModel(appointmentRepository, healthTrackingRepository)
    val treatmentViewModel = NewTreatmentViewModel(treatmentRepository, healthTrackingRepository)
    val procedureViewModel = NewProcedureViewModel(procedureRepository, healthTrackingRepository)
    val newPatientViewModel = NewPatientViewModel(profileRepository, patientRepository, healthTrackingRepository)
    val prescriptionViewModel = NewPrescriptionViewModel(prescriptionRepository)
    val treatmentListViewModel = TreatmentListViewModel(
        treatmentRepository,
        procedureRepository,
        healthTrackingRepository
    )
    val appointmentListViewModel = AppointmentListViewModel(
        appointmentRepository,
        healthTrackingRepository
    )

    if (showPrescriptionDialog && selectedPatient != null) {
        NewPrescription(
            onDismiss = { showPrescriptionDialog = false },
            doctorId = 81,
            selectedPatientId = selectedPatient!!.id,
            viewModel = prescriptionViewModel,
            reloadTrigger = reloadPrescriptionsTrigger
        )
    }

    if (showAppointmentListDialog && selectedPatient != null) {
        AppointmentListDialog(
            onDismiss = { showAppointmentListDialog = false },
            doctorId = 81,
            patientId = selectedPatient!!.id,
            viewModel = appointmentListViewModel,
            onAddAppointment = {
                showAppointmentDialog = true
            },
            reloadTrigger = reloadAppointmentsTrigger
        )
    }

    if (showTreatmentListDialog && selectedPatient != null) {
        TreatmentListDialog(
            onDismiss = { showTreatmentListDialog = false },
            doctorId = 81,
            patientId = selectedPatient!!.id,
            viewModel = treatmentListViewModel,
            onAddTreatment = {
                showTreatmentDialog = true
            },
            onAddProcedure = {
                showProcedureDialog = true
            },
            reloadTreatmentTrigger = reloadTreatmentsTrigger,
            reloadProcedureTrigger = reloadProceduresTrigger
        )
    }

    if (showMedicationDialog && selectedPatient != null) {
        MedicationListDialog(
            onDismiss = { showMedicationDialog = false
                        reloadPrescriptionsTrigger++},
            patientId = selectedPatient!!.id,
            patientName = selectedPatient!!.firstName + " " + selectedPatient!!.lastName,
            patientDni = selectedPatient!!.dni,
            viewModel = medicationListViewModel,
            onAddPrescription = {
                showPrescriptionDialog = true
            }
        )
    }

    if (showAppointmentDialog && selectedPatient != null) {
        NewAppointment(
            onDismiss = { showAppointmentDialog = false
                reloadAppointmentsTrigger++},
            doctorId = 81,
            selectedPatientId = selectedPatient!!.id,
            viewModel = appointmentViewModel
        )
    }

    if (showTreatmentDialog && selectedPatient != null) {
        NewTreatment(
            onDismiss = { showTreatmentDialog = false
                        reloadTreatmentsTrigger++},
            doctorId = 81,
            selectedPatientId = selectedPatient!!.id,
            viewModel = treatmentViewModel
        )
    }

    if (showProcedureDialog && selectedPatient != null) {
        NewProcedure(
            onDismiss = { showProcedureDialog = false
                        reloadProceduresTrigger++},
            doctorId = 81,
            selectedPatientId = selectedPatient!!.id,
            viewModel = procedureViewModel
        )
    }

    if (showNewPatientDialog) {
        NewPatient(
            onDismiss = { 
                showNewPatientDialog = false
                reloadTrigger++ // Incrementar el trigger para recargar la lista
            },
            doctorId = 81,
            viewModel = newPatientViewModel
        )
    }
}


