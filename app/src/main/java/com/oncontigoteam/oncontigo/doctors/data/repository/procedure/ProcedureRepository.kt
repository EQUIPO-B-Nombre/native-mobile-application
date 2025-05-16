package com.oncontigoteam.oncontigo.doctors.data.repository.procedure

import android.os.Build
import androidx.annotation.RequiresApi
import com.oncontigoteam.oncontigo.doctors.common.Resource
import com.oncontigoteam.oncontigo.doctors.data.remote.procedure.CreateProcedureDto
import com.oncontigoteam.oncontigo.doctors.data.remote.procedure.ProcedureService
import com.oncontigoteam.oncontigo.doctors.data.remote.procedure.toProcedure
import com.oncontigoteam.oncontigo.doctors.domain.procedure.CreateProcedure
import com.oncontigoteam.oncontigo.doctors.domain.procedure.Procedure
import com.oncontigoteam.oncontigo.doctors.domain.procedure.UpdateProcedure
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.format.DateTimeFormatter

class ProcedureRepository(private val procedureService: ProcedureService) {
    suspend fun searchProcedureById(procedureId: Long, token: String): Resource<Procedure> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"
        val response = procedureService.getProcedureById(procedureId, bearerToken)
        if (response.isSuccessful) {
            response.body()?.let { procedureDto ->
                val procedure = procedureDto.toProcedure()
                return@withContext Resource.Success(procedure)
            }
            return@withContext Resource.Error(message = "No se pudo obtener el procedimiento")
        }
        return@withContext Resource.Error(response.message())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun updateProcedure(procedureId: Long, token: String, procedure: UpdateProcedure): Resource<Procedure> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"
        val response = procedureService.updateProcedure(procedureId, bearerToken, procedure)
        if (response.isSuccessful) {
            response.body()?.let { procedureDto ->
                val updatedProcedure = procedureDto.toProcedure()
                return@withContext Resource.Success(updatedProcedure)
            }
            return@withContext Resource.Error(message = "No se pudo actualizar el procedimiento")
        }
        return@withContext Resource.Error(response.message())
    }

    suspend fun deleteProcedure(procedureId: Long, token: String): Resource<Unit> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"
        val response = procedureService.deleteProcedure(procedureId, bearerToken)
        if (response.isSuccessful) {
            return@withContext Resource.Success(Unit)
        }
        return@withContext Resource.Error(response.message())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun createProcedure(token: String, procedure: CreateProcedure): Resource<Procedure> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"

        val formattedPerformedAt = procedure.performedAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)

        val createProcedureDto = CreateProcedureDto(
            name = procedure.name,
            description = procedure.description,
            performedAt = formattedPerformedAt,
            healthTrackingId = procedure.healthTrackingId
        )

        val response = procedureService.createProcedure(bearerToken, createProcedureDto)
        if (response.isSuccessful) {
            response.body()?.let { procedureDto ->
                val newProcedure = procedureDto.toProcedure()
                return@withContext Resource.Success(newProcedure)
            }
            return@withContext Resource.Error(message = "No se pudo crear el procedimiento")
        }
        return@withContext Resource.Error(response.message())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun searchProcedureByHealthTrackingId(healthTrackingId: Long, token: String): Resource<List<Procedure>> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"
        val response = procedureService.getProcedureByHealthTrackingId(healthTrackingId, bearerToken)
        if (response.isSuccessful) {
            response.body()?.let { procedureList ->
                val procedures = procedureList.map { it.toProcedure() }
                return@withContext Resource.Success(procedures)
            }
            return@withContext Resource.Error(message = "No se pudo obtener el procedimiento")
        }
        return@withContext Resource.Error(response.message())
    }
}