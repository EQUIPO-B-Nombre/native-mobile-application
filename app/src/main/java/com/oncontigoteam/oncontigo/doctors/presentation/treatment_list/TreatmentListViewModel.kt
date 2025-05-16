package com.oncontigoteam.oncontigo.doctors.presentation.treatment_list

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oncontigoteam.oncontigo.doctors.common.GlobalVariables
import com.oncontigoteam.oncontigo.doctors.common.Resource
import com.oncontigoteam.oncontigo.doctors.data.repository.treatment.TreatmentRepository
import com.oncontigoteam.oncontigo.doctors.data.repository.procedure.ProcedureRepository
import com.oncontigoteam.oncontigo.doctors.data.repository.healthtracking.HealthTrackingRepository
import com.oncontigoteam.oncontigo.doctors.domain.treatment.Treatment
import com.oncontigoteam.oncontigo.doctors.domain.procedure.Procedure
import kotlinx.coroutines.launch

class TreatmentListViewModel(
    private val treatmentRepository: TreatmentRepository,
    private val procedureRepository: ProcedureRepository,
    private val healthTrackingRepository: HealthTrackingRepository
) : ViewModel() {
    val treatments = mutableStateOf<List<Treatment>>(emptyList())
    val procedures = mutableStateOf<List<Procedure>>(emptyList())
    val isLoading = mutableStateOf(false)
    val error = mutableStateOf<String?>(null)

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadData(doctorId: Long, patientId: Long) {
        isLoading.value = true
        error.value = null
        viewModelScope.launch {
            try {
                // 1. Obtener todos los healthTracking del doctor
                val htResult = healthTrackingRepository.getHealthTrackingByDoctorId(doctorId, GlobalVariables.TOKEN)
                val healthTrackingId = if (htResult is Resource.Success) {
                    htResult.data?.find { it.patientId == patientId }?.id
                } else {
                    error.value = (htResult as? Resource.Error)?.message ?: "Error al obtener healthTracking"
                    null
                }
                if (healthTrackingId == null) {
                    treatments.value = emptyList()
                    procedures.value = emptyList()
                    isLoading.value = false
                    return@launch
                }

                // 2. Obtener tratamientos y procedimientos usando el healthTrackingId
                val tResult = treatmentRepository.searchTreatmentByHealthTrackingId(healthTrackingId, GlobalVariables.TOKEN)
                val pResult = procedureRepository.searchProcedureByHealthTrackingId(healthTrackingId, GlobalVariables.TOKEN)
                treatments.value = if (tResult is Resource.Success) tResult.data ?: emptyList() else emptyList()
                procedures.value = if (pResult is Resource.Success) pResult.data ?: emptyList() else emptyList()
                if (tResult is Resource.Error) error.value = tResult.message
                if (pResult is Resource.Error) error.value = pResult.message
            } catch (e: Exception) {
                error.value = e.message
                treatments.value = emptyList()
                procedures.value = emptyList()
            } finally {
                isLoading.value = false
            }
        }
    }
}