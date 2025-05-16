package com.oncontigoteam.oncontigo.doctors.data.repository.patient

import com.oncontigoteam.oncontigo.doctors.common.Resource
import com.oncontigoteam.oncontigo.doctors.data.remote.patient.PatientService
import com.oncontigoteam.oncontigo.doctors.data.remote.patient.toPatient
import com.oncontigoteam.oncontigo.doctors.domain.patient.Patient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PatientRepository(private val patientService: PatientService) {
    suspend fun searchAllPatients(token: String): Resource<List<Patient>> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"
        val response = patientService.getAllPatients(bearerToken)
        if (response.isSuccessful) {
            response.body()?.let { patientList ->
                val patients = patientList.map { it.toPatient() }
                return@withContext Resource.Success(patients)
            }
            return@withContext Resource.Error(message = "No se encontró doctores")
        }
        return@withContext Resource.Error(response.message())
    }

    suspend fun searchPatientById(patientId: Long, token: String): Resource<Patient> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"
        val response = patientService.getPatientById(patientId, bearerToken)
        if (response.isSuccessful) {
            response.body()?.let { patientDto ->
                val patient = patientDto.toPatient()
                return@withContext Resource.Success(patient)
            }
            return@withContext Resource.Error(message = "No se encontró paciente")
        }
        return@withContext Resource.Error(response.message())
    }

    suspend fun deletePatient(patientId: Long, token: String): Resource<Unit> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"
        val response = patientService.deletePatient(patientId, bearerToken)
        if (response.isSuccessful) {
            return@withContext Resource.Success(Unit)
        }
        return@withContext Resource.Error(response.message())
    }

}