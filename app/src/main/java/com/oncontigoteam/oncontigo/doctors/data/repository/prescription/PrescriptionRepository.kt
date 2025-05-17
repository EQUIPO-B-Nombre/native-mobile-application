package com.oncontigoteam.oncontigo.doctors.data.repository.prescription

import com.oncontigoteam.oncontigo.doctors.common.Resource
import com.oncontigoteam.oncontigo.doctors.data.remote.prescription.PrescriptionService
import com.oncontigoteam.oncontigo.doctors.data.remote.prescription.toPrescription
import com.oncontigoteam.oncontigo.doctors.domain.prescription.CreatePrescription
import com.oncontigoteam.oncontigo.doctors.domain.prescription.Prescription
import com.oncontigoteam.oncontigo.doctors.domain.prescription.UpdatePrescription
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PrescriptionRepository(private val prescriptionService: PrescriptionService) {
    suspend fun searchPrescriptionById(prescriptionId: Long, token: String): Resource<Prescription> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"
        val response = prescriptionService.getPrescriptionById(prescriptionId, bearerToken)
        if (response.isSuccessful) {
            response.body()?.let { prescriptionDto ->
                val prescription = prescriptionDto.toPrescription()
                return@withContext Resource.Success(prescription)
            }
            return@withContext Resource.Error(message = "No se encontró prescripción")
        }
        return@withContext Resource.Error(response.message())
    }

    suspend fun updatePrescription(prescriptionId: Long, token: String, prescription: UpdatePrescription): Resource<Prescription> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"
        val response = prescriptionService.updatePrescription(prescriptionId, bearerToken, prescription)
        if (response.isSuccessful) {
            response.body()?.let { prescriptionDto ->
                val updatedPrescription = prescriptionDto.toPrescription()
                return@withContext Resource.Success(updatedPrescription)
            }
            return@withContext Resource.Error(message = "No se pudo actualizar la prescripción")
        }
        return@withContext Resource.Error(response.message())
    }

    suspend fun createPrescription(token: String, prescription: Prescription): Resource<Prescription> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"
        val response = prescriptionService.createPrescription(bearerToken,
            CreatePrescription(prescription.patientId, prescription.doctorId, prescription.medicationName, prescription.dosage)
        )
        if (response.isSuccessful) {
            response.body()?.let { prescriptionDto ->
                val newPrescription = prescriptionDto.toPrescription()
                return@withContext Resource.Success(newPrescription)
            }
            return@withContext Resource.Error(message = "No se pudo crear la prescripción")
        }
        return@withContext Resource.Error(response.message())
    }

    suspend fun searchPrescriptionByPatientId(patientId: Long, token: String): Resource<List<Prescription>> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"
        val response = prescriptionService.getPrescriptionByPatientId(patientId, bearerToken)
        if (response.isSuccessful) {
            response.body()?.let { prescriptionList ->
                val prescriptions = prescriptionList.map { it.toPrescription() }
                return@withContext Resource.Success(prescriptions)
            }
            return@withContext Resource.Error(message = "No se pudo obtener la prescripción")
        }
        return@withContext Resource.Error(response.message())
    }
}