package com.oncontigoteam.oncontigo.doctors.presentation.newTreatment

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
import com.oncontigoteam.oncontigo.doctors.data.repository.healthtracking.HealthTrackingRepository
import com.oncontigoteam.oncontigo.doctors.data.repository.treatment.TreatmentRepository
import com.oncontigoteam.oncontigo.doctors.domain.treatment.CreateTreatment
import com.oncontigoteam.oncontigo.doctors.domain.treatment.Treatment
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneOffset

class NewTreatmentViewModel(
    private val treatmentRepository: TreatmentRepository,
    private val healthTrackingRepository: HealthTrackingRepository
) : ViewModel() {
    
    private val TAG = "NewTreatmentViewModel"
    
    private val _state = mutableStateOf(UIState<Treatment>())
    val state: State<UIState<Treatment>> get() = _state

    private val _selectedPatientId = mutableStateOf<Long?>(null)
    val selectedPatientId: State<Long?> get() = _selectedPatientId

    private val _name = mutableStateOf("")
    val name: State<String> get() = _name

    private val _description = mutableStateOf("")
    val description: State<String> get() = _description

    @RequiresApi(Build.VERSION_CODES.O)
    private val _startDate = mutableStateOf(LocalDateTime.now())
    val startDate: State<LocalDateTime> @RequiresApi(Build.VERSION_CODES.O)
    get() = _startDate

    @RequiresApi(Build.VERSION_CODES.O)
    private val _endDate = mutableStateOf(LocalDateTime.now().plusDays(7))
    val endDate: State<LocalDateTime> @RequiresApi(Build.VERSION_CODES.O)
    get() = _endDate

    private val _showSuccessDialog = mutableStateOf(false)
    val showSuccessDialog: State<Boolean> = _showSuccessDialog

    fun showSuccessDialog() {
        _showSuccessDialog.value = true
    }

    fun hideSuccessDialog() {
        _showSuccessDialog.value = false
    }

    fun setSelectedPatientId(patientId: Long) {
        Log.d(TAG, "setSelectedPatientId: $patientId")
        _selectedPatientId.value = patientId
    }

    fun updateName(newName: String) {
        Log.d(TAG, "updateName: $newName")
        _name.value = newName
    }

    fun updateDescription(newDescription: String) {
        Log.d(TAG, "updateDescription: $newDescription")
        _description.value = newDescription
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateStartDate(newStartDate: LocalDateTime) {
        Log.d(TAG, "updateStartDate: ${newStartDate.toString()}")
        _startDate.value = newStartDate
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateEndDate(newEndDate: LocalDateTime) {
        Log.d(TAG, "updateEndDate: ${newEndDate.toString()}")
        _endDate.value = newEndDate
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun createTreatment(doctorId: Long) {
        Log.d(TAG, "createTreatment - doctorId: $doctorId")
        viewModelScope.launch {
            try {
                val patientId = _selectedPatientId.value ?: run {
                    Log.e(TAG, "No patient selected")
                    _state.value = UIState(message = "Debe seleccionar un paciente")
                    return@launch
                }
                

                val healthTrackingId = getHealthTrackingId(doctorId, patientId)
                if (healthTrackingId == null) {
                    Log.e(TAG, "No health tracking found for patient: $patientId")
                    _state.value = UIState(message = "No se encontró el seguimiento de salud del paciente")
                    return@launch
                }
                Log.d(TAG, "Found healthTrackingId: $healthTrackingId")


                val utcStartDate = _startDate.value.atOffset(ZoneOffset.UTC).toLocalDateTime()
                val utcEndDate = _endDate.value.atOffset(ZoneOffset.UTC).toLocalDateTime()
                Log.d(TAG, "UTC startDate: $utcStartDate, UTC endDate: $utcEndDate")

                if (utcEndDate.isBefore(utcStartDate)) {
                    Log.e(TAG, "End date is before start date")
                    _state.value = UIState(message = "La fecha de fin debe ser posterior a la fecha de inicio")
                    return@launch
                }

                val createTreatment = CreateTreatment(
                    name = _name.value,
                    description = _description.value,
                    startDate = utcStartDate,
                    endDate = utcEndDate,
                    healthTrackingId = healthTrackingId
                )
                Log.d(TAG, "Creating treatment: $createTreatment")
                
                _state.value = UIState(isLoading = true)
                val result = treatmentRepository.createTreatment(GlobalVariables.TOKEN, createTreatment)
                Log.d(TAG, "createTreatment response: $result")
                
                if (result is Resource.Success) {
                    Log.d(TAG, "Treatment created successfully")
                    _state.value = UIState(isLoading = false)
                    _name.value = ""
                    _description.value = ""
                    _startDate.value = LocalDateTime.now()
                    _endDate.value = LocalDateTime.now().plusDays(7)
                    showSuccessDialog()
                } else {
                    Log.e(TAG, "Error creating treatment: ${result.message}")
                    _state.value = UIState(message = "Error al crear el tratamiento")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception creating treatment", e)
                _state.value = UIState(message = "Error al crear el tratamiento: ${e.message}")
            }
        }
    }
}