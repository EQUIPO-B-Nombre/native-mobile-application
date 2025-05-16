package com.oncontigoteam.oncontigo.doctors.data.remote.appointment

import android.os.Build
import androidx.annotation.RequiresApi
import com.oncontigoteam.oncontigo.doctors.domain.appointment.Appointment
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class AppointmentDto(
    val id: Long,
    val dateTime: String,
    val description: String,
    val healthTrackingId: Long,
)

data class CreateAppointmentDto(
    val dateTime: String,
    val description: String,
    val healthTrackingId: Long
)

@RequiresApi(Build.VERSION_CODES.O)
fun AppointmentDto.toAppointment() = Appointment(
    id,
    dateTime = dateTime.let { LocalDateTime.parse(it, DateTimeFormatter.ISO_LOCAL_DATE_TIME) },
    description,
    healthTrackingId)

