package com.oncontigoteam.oncontigo.doctors.presentation.appointment_list

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oncontigoteam.oncontigo.doctors.common.GlobalVariables
import com.oncontigoteam.oncontigo.doctors.common.Resource
import com.oncontigoteam.oncontigo.doctors.data.repository.appointment.AppointmentRepository
import com.oncontigoteam.oncontigo.doctors.data.repository.healthtracking.HealthTrackingRepository
import com.oncontigoteam.oncontigo.doctors.domain.appointment.Appointment
import kotlinx.coroutines.launch

class AppointmentListViewModel(
    private val appointmentRepository: AppointmentRepository,
    private val healthTrackingRepository: HealthTrackingRepository
): ViewModel() {

    val appointments = mutableStateOf<List<Appointment>>(emptyList())
    val isLoading = mutableStateOf(false)
    val error = mutableStateOf<String?>(null)

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadData(doctorId: Long, patientId: Long) {
        isLoading.value = true
        error.value = null
        viewModelScope.launch {
            try {
                val htResult = healthTrackingRepository.getHealthTrackingByDoctorId(doctorId, GlobalVariables.TOKEN)
                val healthTrackingId = if (htResult is Resource.Success) {
                    htResult.data?.find { it.patientId == patientId }?.id
                } else {
                    error.value = (htResult as? Resource.Error)?.message ?: "Error al obtener healthTracking"
                    null
                }
                if (healthTrackingId == null) {
                    appointments.value = emptyList()
                    isLoading.value = false
                    return@launch
                }

                val aResult = appointmentRepository.searchAppointmentByHealthTrackingId(healthTrackingId, GlobalVariables.TOKEN)
                appointments.value = if (aResult is Resource.Success) aResult.data ?: emptyList() else emptyList()
                if (aResult is Resource.Error) error.value = aResult.message

            } catch (e: Exception) {
                error.value = e.message
                appointments.value = emptyList()
            } finally {
                isLoading.value = false
            }
        }
    }
}