package com.oncontigoteam.oncontigo.doctors.presentation.newPatient

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
import com.oncontigoteam.oncontigo.doctors.data.repository.patient.PatientRepository
import com.oncontigoteam.oncontigo.doctors.domain.healthtracking.CreateHealthTracking
import com.oncontigoteam.oncontigo.doctors.data.repository.profile.ProfileRepository
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class NewPatientViewModel(
    private val profileRepository: ProfileRepository,
    private val patientRepository: PatientRepository,
    private val healthTrackingRepository: HealthTrackingRepository
) : ViewModel() {

    private val TAG = "NewPatientViewModel"

    private val _state = mutableStateOf(UIState<Unit>())
    val state: State<UIState<Unit>> = _state

    private val _dni = mutableStateOf("")
    val dni: State<String> = _dni

    private val _showSuccessDialog = mutableStateOf(false)
    val showSuccessDialog: State<Boolean> = _showSuccessDialog

    fun showSuccessDialog() {
        _showSuccessDialog.value = true
    }

    fun hideSuccessDialog() {
        _showSuccessDialog.value = false
    }

    fun updateDni(newDni: String) {
        Log.d(TAG, "updateDni: $newDni")
        _dni.value = newDni
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addNewPatient(doctorId: Long) {
        Log.d(TAG, "addNewPatient - Iniciando proceso con DNI: ${_dni.value}, doctorId: $doctorId")
        viewModelScope.launch {
            try {
                _state.value = UIState(isLoading = true)

                Log.d(TAG, "Buscando perfiles...")
                val profilesResult = profileRepository.searchAllProfiles(GlobalVariables.TOKEN)
                Log.d(TAG, "Resultado de búsqueda de perfiles: $profilesResult")

                if (profilesResult is Resource.Error) {
                    Log.e(TAG, "Error al obtener perfiles: ${profilesResult.message}")
                    _state.value = UIState(message = "Error al obtener perfiles: ${profilesResult.message}")
                    return@launch
                }

                val profile = (profilesResult as Resource.Success).data?.find { it.dni == _dni.value }
                Log.d(TAG, "Perfil encontrado: $profile")

                if (profile == null) {
                    Log.e(TAG, "No se encontró perfil con DNI: ${_dni.value}")
                    _state.value = UIState(message = "No se encontró un perfil con ese DNI")
                    return@launch
                }

                Log.d(TAG, "Buscando pacientes...")
                val patientsResult = patientRepository.searchAllPatients(GlobalVariables.TOKEN)
                Log.d(TAG, "Resultado de búsqueda de pacientes: $patientsResult")

                if (patientsResult is Resource.Error) {
                    Log.e(TAG, "Error al obtener pacientes: ${patientsResult.message}")
                    _state.value = UIState(message = "Error al obtener pacientes: ${patientsResult.message}")
                    return@launch
                }

                val patient = (patientsResult as Resource.Success).data?.find { it.userId == profile.userId }
                Log.d(TAG, "Paciente encontrado: $patient")

                if (patient == null) {
                    Log.e(TAG, "No se encontró paciente para userId: ${profile.userId}")
                    _state.value = UIState(message = "No se encontró un paciente asociado a ese perfil")
                    return@launch
                }

                Log.d(TAG, "Creando health tracking para paciente: ${patient.id}")
                val createHealthTracking = CreateHealthTracking(
                    doctorId = doctorId,
                    patientId = patient.id,
                    status = "ACTIVE",
                    description = "Health tracking for ${profile.dni}",
                    lastVisit = LocalDateTime.now(),
                )
                Log.d(TAG, "Health tracking a crear: $createHealthTracking")

                val healthTrackingResult = healthTrackingRepository.createHealthTracking(
                    GlobalVariables.TOKEN,
                    createHealthTracking
                )
                Log.d(TAG, "Resultado de creación de health tracking: $healthTrackingResult")

                when (healthTrackingResult) {
                    is Resource.Success -> {
                        Log.d(TAG, "Health tracking creado exitosamente")
                        _state.value = UIState(data = Unit)
                        _dni.value = "" // Limpiar el campo DNI
                        showSuccessDialog()
                    }
                    is Resource.Error -> {
                        Log.e(TAG, "Error al crear health tracking: ${healthTrackingResult.message}")
                        _state.value = UIState(message = "Error al crear el seguimiento: ${healthTrackingResult.message}")
                    }
                }

            } catch (e: Exception) {
                Log.e(TAG, "Error inesperado al agregar nuevo paciente", e)
                _state.value = UIState(message = "Error inesperado: ${e.message}")
            }
        }
    }
}