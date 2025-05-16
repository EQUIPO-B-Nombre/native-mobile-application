package com.oncontigoteam.oncontigo.doctors.presentation.newAppointment

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oncontigoteam.oncontigo.doctors.common.GlobalVariables
import com.oncontigoteam.oncontigo.doctors.common.Resource
import com.oncontigoteam.oncontigo.doctors.common.UIState
import com.oncontigoteam.oncontigo.doctors.data.repository.appointment.AppointmentRepository
import com.oncontigoteam.oncontigo.doctors.data.repository.healthtracking.HealthTrackingRepository
import com.oncontigoteam.oncontigo.doctors.domain.appointment.Appointment
import com.oncontigoteam.oncontigo.doctors.domain.appointment.CreateAppointment
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneOffset


class NewAppointmentViewModel(
    private val appointmentRepository: AppointmentRepository,
    private val healthTrackingRepository: HealthTrackingRepository
) : ViewModel() {
    
    private val TAG = "NewAppointmentViewModel"
    
    private val _state = mutableStateOf(UIState<Unit>())
    val state: State<UIState<Unit>> = _state

    private val _selectedPatientId = mutableStateOf<Long?>(null)
    val selectedPatientId: State<Long?> get() = _selectedPatientId

    private val _description = mutableStateOf("")
    val description: State<String> get() = _description

    @RequiresApi(Build.VERSION_CODES.O)
    private val _dateTime = mutableStateOf(LocalDateTime.now())
    val dateTime: State<LocalDateTime> @RequiresApi(Build.VERSION_CODES.O)
    get() = _dateTime

    private val _showSuccessDialog = mutableStateOf(false)
    val showSuccessDialog: State<Boolean> = _showSuccessDialog

    fun setSelectedPatientId(patientId: Long) {
        Log.d(TAG, "setSelectedPatientId: $patientId")
        _selectedPatientId.value = patientId
    }

    fun updateDescription(newDescription: String) {
        Log.d(TAG, "updateDescription: $newDescription")
        _description.value = newDescription
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateDateTime(newDateTime: LocalDateTime) {
        Log.d(TAG, "updateDateTime: ${newDateTime.toString()}")
        _dateTime.value = newDateTime
    }

    private suspend fun getHealthTrackingId(doctorId: Long, patientId: Long): Long? {
        Log.d(TAG, "getHealthTrackingId - doctorId: $doctorId, patientId: $patientId")
        return try {
            val result = healthTrackingRepository.getHealthTrackingByDoctorId(doctorId, GlobalVariables.TOKEN)
            Log.d(TAG, "getHealthTrackingByDoctorId response: $result")
            
            if (result is Resource.Success) {
                val healthTracking = result.data?.find { it.patientId == patientId }
                Log.d(TAG, "Found healthTracking: $healthTracking")
                healthTracking?.id
            } else {
                Log.e(TAG, "Error getting health tracking: ${result.message}")
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception getting health tracking", e)
            null
        }
    }

    fun showSuccessDialog() {
        _showSuccessDialog.value = true
    }

    fun hideSuccessDialog() {
        _showSuccessDialog.value = false
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createAppointment(doctorId: Long) {
        Log.d(TAG, "createAppointment - doctorId: $doctorId")
        viewModelScope.launch {
            try {
                val patientId = _selectedPatientId.value ?: run {
                    Log.e(TAG, "No patient selected")
                    return@launch
                }
                
                
                val healthTrackingId = getHealthTrackingId(doctorId, patientId)
                if (healthTrackingId == null) {
                    Log.e(TAG, "No health tracking found for patient: $patientId")
                    _state.value = UIState(message = "No se encontró el seguimiento de salud del paciente")
                    return@launch
                }
                Log.d(TAG, "Found healthTrackingId: $healthTrackingId")


                val utcDateTime = _dateTime.value.atOffset(ZoneOffset.UTC).toLocalDateTime()
                Log.d(TAG, "UTC dateTime: $utcDateTime")

                val createAppointment = CreateAppointment(
                    dateTime = utcDateTime,
                    description = _description.value,
                    healthTrackingId = healthTrackingId
                )
                Log.d(TAG, "Creating appointment: $createAppointment")
                
                _state.value = UIState(isLoading = true)
                val result = appointmentRepository.createAppointment(GlobalVariables.TOKEN, createAppointment)
                Log.d(TAG, "createAppointment response: $result")
                
                when (result) {
                    is Resource.Success -> {
                        Log.d(TAG, "Appointment created successfully")
                        _state.value = UIState(data = Unit)
                        showSuccessDialog()
                        // Limpiar los campos después de crear exitosamente
                        _description.value = ""
                        _dateTime.value = LocalDateTime.now()
                    }
                    is Resource.Error -> {
                        Log.e(TAG, "Error creating appointment: ${result.message}")
                        _state.value = UIState(message = "Error al crear la cita: ${result.message}")
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception creating appointment", e)
                _state.value = UIState(message = "Error al crear la cita: ${e.message}")
            }
        }
    }
}