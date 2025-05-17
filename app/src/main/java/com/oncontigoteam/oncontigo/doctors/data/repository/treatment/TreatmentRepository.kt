package com.oncontigoteam.oncontigo.doctors.data.repository.treatment

import android.os.Build
import androidx.annotation.RequiresApi
import com.oncontigoteam.oncontigo.doctors.common.Resource
import com.oncontigoteam.oncontigo.doctors.data.remote.treatment.CreateTreatmentDto
import com.oncontigoteam.oncontigo.doctors.data.remote.treatment.TreatmentService
import com.oncontigoteam.oncontigo.doctors.data.remote.treatment.toTreatment
import com.oncontigoteam.oncontigo.doctors.domain.treatment.CreateTreatment
import com.oncontigoteam.oncontigo.doctors.domain.treatment.Treatment
import com.oncontigoteam.oncontigo.doctors.domain.treatment.UpdateTreatment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.format.DateTimeFormatter

class TreatmentRepository(private val treatmentService: TreatmentService) {
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun searchTreatmentById(treatmentId: Long, token: String): Resource<Treatment> = withContext(
        Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"
        val response = treatmentService.getTreatmentById(treatmentId, bearerToken)
        if (response.isSuccessful) {
            response.body()?.let { treatmentDto ->
                val treatment = treatmentDto.toTreatment()
                return@withContext Resource.Success(treatment)
            }
            return@withContext Resource.Error(message = "No se pudo obtener el tratamiento")
        }
        return@withContext Resource.Error(response.message())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun updateTreatment(treatmentId: Long, token: String, treatment: UpdateTreatment): Resource<Treatment> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"
        val response = treatmentService.updateTreatment(treatmentId, bearerToken, treatment)
        if (response.isSuccessful) {
            response.body()?.let { treatmentDto ->
                val updatedTreatment = treatmentDto.toTreatment()
                return@withContext Resource.Success(updatedTreatment)
            }
            return@withContext Resource.Error(message = "No se pudo actualizar el tratamiento")
        }
        return@withContext Resource.Error(response.message())
    }

    suspend fun deleteTreatment(treatmentId: Long, token: String): Resource<Unit> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"
        
        val response = treatmentService.deleteTreatment(treatmentId, bearerToken)
        if (response.isSuccessful) {
            return@withContext Resource.Success(Unit)
        }
        return@withContext Resource.Error(response.message())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun createTreatment(token: String, treatment: CreateTreatment): Resource<Treatment> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"

        val formattedStartDate = treatment.startDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        val formattedEndDate = treatment.endDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)

        val createTreatmentDto = CreateTreatmentDto(
            name = treatment.name,
            description = treatment.description,
            startDate = formattedStartDate,
            endDate = formattedEndDate,
            healthTrackingId = treatment.healthTrackingId
        )

        val response = treatmentService.createTreatment(bearerToken, createTreatmentDto)
        if (response.isSuccessful) {
            response.body()?.let { treatmentDto ->
                val newTreatment = treatmentDto.toTreatment()
                return@withContext Resource.Success(newTreatment)
            }
            return@withContext Resource.Error(message = "No se pudo crear el tratamiento")
        }
        return@withContext Resource.Error(response.message())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun searchTreatmentByHealthTrackingId(healthTrackingId: Long, token: String): Resource<List<Treatment>> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"
        val response = treatmentService.getTreatmentByHealthTrackingId(healthTrackingId, bearerToken)
        if (response.isSuccessful) {
            response.body()?.let { treatmentList ->
                val treatments = treatmentList.map { it.toTreatment() }
                return@withContext Resource.Success(treatments)
            }
            return@withContext Resource.Error(message = "No se pudo obtener el tratamiento")
        }
        return@withContext Resource.Error(response.message())
    }
}