package com.oncontigoteam.oncontigo.doctors.data.repository.healthtracking

import android.os.Build
import androidx.annotation.RequiresApi
import com.oncontigoteam.oncontigo.doctors.common.Resource
import com.oncontigoteam.oncontigo.doctors.data.remote.healthtracking.CreateHealthTrackingDto
import com.oncontigoteam.oncontigo.doctors.data.remote.healthtracking.HealthTrackingService
import com.oncontigoteam.oncontigo.doctors.data.remote.healthtracking.toHealthTracking
import com.oncontigoteam.oncontigo.doctors.domain.healthtracking.CreateHealthTracking
import com.oncontigoteam.oncontigo.doctors.domain.healthtracking.HealthTracking
import com.oncontigoteam.oncontigo.doctors.domain.healthtracking.UpdateHealthTracking
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.format.DateTimeFormatter

class HealthTrackingRepository(private val healthTrackingService: HealthTrackingService) {

    suspend fun searchHealthTrackingById(healthTrackingId: Long, token: String): Resource<HealthTracking> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"
        val response = healthTrackingService.getHealthTrackingById(healthTrackingId, bearerToken)
        if (response.isSuccessful) {
            response.body()?.let { healthTrackingDto ->
                val healthTracking = healthTrackingDto.toHealthTracking()
                return@withContext Resource.Success(healthTracking)
            }
            return@withContext Resource.Error(message = "No se encontró health tracking")
        }
        return@withContext Resource.Error(response.message())
    }

    suspend fun updateHealthTracking(healthTrackingId: Long, token: String, healthTracking: UpdateHealthTracking): Resource<HealthTracking> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"
        val response = healthTrackingService.updateHealthTracking(healthTrackingId, bearerToken, healthTracking)
        if (response.isSuccessful) {
            response.body()?.let { healthTrackingDto ->
                val updatedHealthTracking = healthTrackingDto.toHealthTracking()
                return@withContext Resource.Success(updatedHealthTracking)
            }
            return@withContext Resource.Error(message = "No se pudo actualizar el health tracking")
        }
        return@withContext Resource.Error(response.message())
    }

    suspend fun deleteHealthTracking(healthTrackingId: Long, token: String): Resource<Unit> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"
        val response = healthTrackingService.deleteHealthTracking(healthTrackingId, bearerToken)
        if (response.isSuccessful) {
            return@withContext Resource.Success(Unit)
        }
        return@withContext Resource.Error(response.message())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun createHealthTracking(token: String, healthTracking: CreateHealthTracking): Resource<HealthTracking> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"

        val formattedLastVisit = healthTracking.lastVisit
            .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)

        val createHealthTrackingDto = CreateHealthTrackingDto(
            patientId = healthTracking.patientId,
            doctorId = healthTracking.doctorId,
            status = healthTracking.status,
            description = healthTracking.description,
            lastVisit = formattedLastVisit
        )


        val response = healthTrackingService.createHealthTracking(bearerToken, createHealthTrackingDto)
        if (response.isSuccessful) {
            response.body()?.let { healthTrackingDto ->
                val newHealthTracking = healthTrackingDto.toHealthTracking()
                return@withContext Resource.Success(newHealthTracking)
            }
            return@withContext Resource.Error(message = "No se pudo crear el health tracking")
        }
        return@withContext Resource.Error(response.message())
    }

    suspend fun getHealthTrackingByDoctorId(doctorId: Long, token: String): Resource<List<HealthTracking>> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"
        val response = healthTrackingService.getHealthTrackingByDoctorId(doctorId, bearerToken)
        if (response.isSuccessful) {
            response.body()?.let { healthTrackingList ->
                val healthTrackings = healthTrackingList.map { it.toHealthTracking() }
                return@withContext Resource.Success(healthTrackings)
            }
            return@withContext Resource.Error(message = "No se encontró el health tracking")
        }
        return@withContext Resource.Error(response.message())
    }

    suspend fun searchHealthTrackingByDoctorAndPatient(
        doctorId: Long,
        patientId: Long,
        token: String
    ): Resource<List<HealthTracking>> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"
        val response = healthTrackingService.getHealthTrackingByDoctorId(doctorId, bearerToken)
        if (response.isSuccessful) {
            response.body()?.let { healthTrackingList ->
                val healthTrackings = healthTrackingList
                    .map { it.toHealthTracking() }
                    .filter { it.patientId == patientId }
                return@withContext Resource.Success(healthTrackings)
            }
            return@withContext Resource.Error(message = "No se encontraron registros de HealthTracking")
        }
        return@withContext Resource.Error(response.message())
    }
}