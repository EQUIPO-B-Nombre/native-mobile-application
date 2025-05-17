package com.oncontigoteam.oncontigo.doctors.presentation.medication_list

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oncontigoteam.oncontigo.doctors.common.GlobalVariables
import com.oncontigoteam.oncontigo.doctors.common.Resource
import com.oncontigoteam.oncontigo.doctors.data.repository.prescription.PrescriptionRepository
import com.oncontigoteam.oncontigo.doctors.domain.prescription.Prescription
import kotlinx.coroutines.launch

class MedicationListViewModel(
    private val prescriptionRepository: PrescriptionRepository
) : ViewModel() {
    val prescriptions = mutableStateOf<List<Prescription>>(emptyList())
    val isLoading = mutableStateOf(false)
    val error = mutableStateOf<String?>(null)

    fun loadPrescriptions(patientId: Long) {
        isLoading.value = true
        error.value = null
        viewModelScope.launch {
            try {
                val result = prescriptionRepository.searchPrescriptionByPatientId(patientId, GlobalVariables.TOKEN)
                when (result) {
                    is Resource.Success -> {
                        prescriptions.value = result.data ?: emptyList()
                    }
                    is Resource.Error -> {
                        error.value = result.message ?: "Error desconocido"
                        prescriptions.value = emptyList()
                    }
                    else -> {
                        prescriptions.value = emptyList()
                    }
                }
            } catch (e: Exception) {
                error.value = e.message
                prescriptions.value = emptyList()
            } finally {
                isLoading.value = false
            }
        }
    }
}