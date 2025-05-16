package com.oncontigoteam.oncontigo.doctors.presentation.newPrescription

import android.content.ContentValues.TAG
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
import com.oncontigoteam.oncontigo.doctors.data.repository.prescription.PrescriptionRepository
import com.oncontigoteam.oncontigo.doctors.domain.prescription.Prescription
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class NewPrescriptionViewModel(
    private val prescriptionRepository: PrescriptionRepository
) : ViewModel() {
    
    private val _state = mutableStateOf(UIState<Prescription>())
    val state: State<UIState<Prescription>> get() = _state

    private val _selectedPatientId = mutableStateOf<Long?>(null)
    val selectedPatientId: State<Long?> get() = _selectedPatientId


    private val _name = mutableStateOf("")
    val name: State<String> get() = _name

    private val _dosage = mutableStateOf("")
    val dosage: State<String> get() = _dosage

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
        _selectedPatientId.value = patientId
    }

    fun updateName(newName: String) {
        _name.value = newName
    }

    fun updateDosage(newDosage: String) {
        _dosage.value = newDosage
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun createPrescription(doctorId: Long) {
        viewModelScope.launch {
            try {
                val patientId = _selectedPatientId.value ?: return@launch
                
                val prescription = Prescription(
                    id = 0,
                    patientId = patientId,
                    doctorId = doctorId,
                    medicationName = _name.value,
                    dosage = _dosage.value
                )
                
                _state.value = UIState(isLoading = true)
                val result = prescriptionRepository.createPrescription(GlobalVariables.TOKEN, prescription)
                
                if (result is Resource.Success) {
                    _state.value = UIState(isLoading = false)
                    _name.value = ""
                    _dosage.value = ""
                    _startDate.value = LocalDateTime.now()
                    _endDate.value = LocalDateTime.now().plusDays(7)
                    showSuccessDialog()
                } else {
                    _state.value = UIState(message = "Error al crear la prescripción")
                }
            } catch (e: Exception) {
                _state.value = UIState(message = "Error al crear la prescripción: ${e.message}")
            }
        }
    }
}