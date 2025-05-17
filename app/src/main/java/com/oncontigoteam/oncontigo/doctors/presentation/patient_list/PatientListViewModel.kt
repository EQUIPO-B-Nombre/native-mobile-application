package com.oncontigoteam.oncontigo.doctors.presentation.patient_list

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.oncontigoteam.oncontigo.doctors.common.GlobalVariables
import com.oncontigoteam.oncontigo.doctors.common.Resource
import com.oncontigoteam.oncontigo.doctors.common.UIState
import com.oncontigoteam.oncontigo.doctors.data.repository.healthtracking.HealthTrackingRepository
import com.oncontigoteam.oncontigo.doctors.data.repository.patient.PatientRepository
import com.oncontigoteam.oncontigo.doctors.data.repository.profile.ProfileRepository
import com.oncontigoteam.oncontigo.doctors.domain.healthtracking.HealthTracking
import kotlinx.coroutines.launch

class PatientListViewModel(
    private val navController: NavController,
    private val profileRepository: ProfileRepository,
    private val patientRepository: PatientRepository,
    private val healthTrackingRepository: HealthTrackingRepository
): ViewModel() {

    private val TAG = "PatientListViewModel"

    private val _state = mutableStateOf(UIState<List<PatientData>>())
    val state: State<UIState<List<PatientData>>> = _state

    private val _list = mutableStateOf<List<PatientData>>(emptyList())
    val list: State<List<PatientData>> = _list

    private val _lastAppointmentState = mutableStateOf<UIState<HealthTracking?>>(UIState())
    val lastAppointmentState: State<UIState<HealthTracking?>> = _lastAppointmentState

    fun goBack() {
        navController.popBackStack()
    }

    fun getPatientList(doctorId: Long) {
        _state.value = UIState(isLoading = true)
        viewModelScope.launch {
            try {
                // 1. Obtener todos los health trackings del doctor
                val healthTrackingsResult = healthTrackingRepository.getHealthTrackingByDoctorId(doctorId, GlobalVariables.TOKEN)
                if (healthTrackingsResult is Resource.Error) {
                    _state.value = UIState(message = "Error al obtener los seguimientos: ${healthTrackingsResult.message}")
                    return@launch
                }

                val healthTrackings = (healthTrackingsResult as Resource.Success).data ?: run {
                    _state.value = UIState(message = "No se encontraron seguimientos")
                    return@launch
                }

                // 2. Para cada health tracking, obtener el paciente y su perfil
                val patientCards = mutableListOf<PatientData>()
                for (healthTracking in healthTrackings) {
                    // Obtener el paciente
                    val patientResult = patientRepository.searchPatientById(healthTracking.patientId, GlobalVariables.TOKEN)
                    if (patientResult is Resource.Error) {
                        Log.e(TAG, "Error al obtener paciente ${healthTracking.patientId}: ${patientResult.message}")
                        continue
                    }

                    val patient = (patientResult as Resource.Success).data ?: continue

                    // Obtener el perfil del paciente
                    val profileResult = profileRepository.searchProfileByUserId(patient.userId, GlobalVariables.TOKEN)
                    if (profileResult is Resource.Error) {
                        Log.e(TAG, "Error al obtener perfil para userId ${patient.userId}: ${profileResult.message}")
                        continue
                    }

                    val profile = (profileResult as Resource.Success).data ?: continue

                    // Crear el PatientData
                    patientCards.add(
                        PatientData(
                            id = patient.id,
                            dni = profile.dni,
                            firstName = profile.firstName,
                            lastName = profile.lastName,
                            photo = profile.photo,
                            phone = profile.phone,
                            city = profile.city,
                            country = profile.country,
                            description = profile.description
                        )
                    )
                }

                _state.value = UIState(data = patientCards)
                _list.value = patientCards

            } catch (e: Exception) {
                Log.e(TAG, "Error al obtener la lista de pacientes", e)
                _state.value = UIState(message = "Error inesperado: ${e.message}")
            }
        }
    }
}