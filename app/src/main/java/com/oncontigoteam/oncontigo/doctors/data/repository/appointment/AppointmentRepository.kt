package com.oncontigoteam.oncontigo.doctors.data.repository.appointment

import android.os.Build
import androidx.annotation.RequiresApi
import com.oncontigoteam.oncontigo.doctors.common.Resource
import com.oncontigoteam.oncontigo.doctors.data.remote.appointment.AppointmentService
import com.oncontigoteam.oncontigo.doctors.data.remote.appointment.CreateAppointmentDto
import com.oncontigoteam.oncontigo.doctors.data.remote.appointment.toAppointment
import com.oncontigoteam.oncontigo.doctors.domain.appointment.Appointment
import com.oncontigoteam.oncontigo.doctors.domain.appointment.CreateAppointment
import com.oncontigoteam.oncontigo.doctors.domain.appointment.UpdateAppointment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.format.DateTimeFormatter

class AppointmentRepository(private val appointmentService: AppointmentService) {

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun searchAppointmentByHealthTrackingId(healthTrackingId: Long, token: String): Resource<List<Appointment>> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"
        val response = appointmentService.getAppointmentByHealthTrackingId(healthTrackingId, bearerToken)
        if (response.isSuccessful) {
            response.body()?.let { appointmentList ->
                val appointments = appointmentList.map { it.toAppointment() }
                return@withContext Resource.Success(appointments)
            }
            return@withContext Resource.Error(message = "No se encontró cita")
        }
        return@withContext Resource.Error(response.message())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun createAppointment(token: String, createAppointment: CreateAppointment): Resource<Appointment> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"
        
        // Convertir la fecha a string en formato ISO-8601 sin offset
        val formattedDateTime = createAppointment.dateTime
            .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        
        val createAppointmentDto = CreateAppointmentDto(
            dateTime = formattedDateTime,
            description = createAppointment.description,
            healthTrackingId = createAppointment.healthTrackingId
        )
        
        val response = appointmentService.createAppointment(bearerToken, createAppointmentDto)
        if (response.isSuccessful) {
            response.body()?.let { appointmentDto ->
                val createdAppointment = appointmentDto.toAppointment()
                return@withContext Resource.Success(createdAppointment)
            }
            return@withContext Resource.Error(message = "No se pudo crear la cita")
        }
        return@withContext Resource.Error(response.message())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun updateAppointment(appointmentId: Long, token: String, appointment: UpdateAppointment): Resource<Appointment> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"
        val response = appointmentService.updateAppointment(appointmentId, bearerToken, appointment)
        if (response.isSuccessful) {
            response.body()?.let { appointmentDto ->
                val updatedAppointment = appointmentDto.toAppointment()
                return@withContext Resource.Success(updatedAppointment)
            }
            return@withContext Resource.Error(message = "No se pudo actualizar la cita")
        }
        return@withContext Resource.Error(response.message())
    }

    suspend fun deleteAppointment(appointmentId: Long, token: String): Resource<Unit> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"
        val response = appointmentService.deleteAppointment(appointmentId, bearerToken)
        if (response.isSuccessful) {
            return@withContext Resource.Success(Unit)
        }
        return@withContext Resource.Error(response.message())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun searchAppointmentById(appointmentId: Long, token: String): Resource<Appointment> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"
        val response = appointmentService.getAppointmentById(appointmentId, bearerToken)
        if (response.isSuccessful) {
            response.body()?.let { appointmentDto ->
                val appointment = appointmentDto.toAppointment()
                return@withContext Resource.Success(appointment)
            }
            return@withContext Resource.Error(message = "No se encontró cita")
        }
        return@withContext Resource.Error(response.message())
    }
}