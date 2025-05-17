package com.oncontigoteam.oncontigo.doctors.presentation.newProcedure

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
import com.oncontigoteam.oncontigo.doctors.data.repository.procedure.ProcedureRepository
import com.oncontigoteam.oncontigo.doctors.domain.procedure.CreateProcedure
import com.oncontigoteam.oncontigo.doctors.domain.procedure.Procedure
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class NewProcedureViewModel(
    private val procedureRepository: ProcedureRepository,
    private val healthTrackingRepository: HealthTrackingRepository
) : ViewModel() {

    private val TAG = "NewProcedureViewModel"

    private val _state = mutableStateOf(UIState<Procedure>())
    val state: State<UIState<Procedure>> get() = _state

    private val _name = mutableStateOf("")
    val name: State<String> get() = _name

    private val _selectedPatientId = mutableStateOf<Long?>(null)
    val selectedPatientId: State<Long?> get() = _selectedPatientId

    private val _description = mutableStateOf("")
    val description: State<String> get() = _description

    @RequiresApi(Build.VERSION_CODES.O)
    private val _performedAt = mutableStateOf(LocalDateTime.now())

    val performedAt: State<LocalDateTime> @RequiresApi(Build.VERSION_CODES.O)
    get() = _performedAt

    private val _showSuccessDialog = mutableStateOf(false)
    val showSuccessDialog: State<Boolean> = _showSuccessDialog

    fun showSuccessDialog() {
        _showSuccessDialog.value = true
    }

    fun hideSuccessDialog() {
        _showSuccessDialog.value = false
    }

    fun updateName(newName: String) {
        _name.value = newName
    }

    fun updateDescription(newDescription: String) {
        _description.value = newDescription
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updatePerformedAt(newDate: LocalDateTime) {
        _performedAt.value = newDate
    }

    fun setSelectedPatientId(patientId: Long) {
        Log.d(TAG, "setSelectedPatientId: $patientId")
        _selectedPatientId.value = patientId
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
    fun createProcedure(doctorId: Long) {
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

                val createProcedure = CreateProcedure(
                    name = _name.value,
                    description = _description.value,
                    performedAt = _performedAt.value,
                    healthTrackingId = healthTrackingId
                )

                _state.value = UIState(isLoading = true)
                val result = procedureRepository.createProcedure(GlobalVariables.TOKEN, createProcedure)
                Log.d(TAG, "createProcedure response: $result")

                if (result is Resource.Success) {
                    _state.value = UIState(isLoading = false)
                    // Limpiar los campos después de crear exitosamente
                    _name.value = ""
                    _description.value = ""
                    _performedAt.value = LocalDateTime.now()
                    showSuccessDialog()

                } else {
                    _state.value = UIState(message = "Error al crear la prescripción")
                }
            } catch (e: Exception) {
                _state.value = UIState(message = "Error al crear el procedimiento: ${e.message}")
            }
        }
    }
}