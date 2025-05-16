package com.oncontigoteam.oncontigo.doctors.data.remote.healthtracking

import android.os.Build
import androidx.annotation.RequiresApi
import com.oncontigoteam.oncontigo.doctors.domain.healthtracking.HealthTracking
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class HealthTrackingDto(
    val id: Long,
    val patientId: Long,
    val doctorId: Long,
    val status: String,
    val description: String,
    val lastVisit: String?
)

data class CreateHealthTrackingDto(
    val patientId: Long,
    val doctorId: Long,
    val status: String,
    val description: String,
    val lastVisit: String?
)

@RequiresApi(Build.VERSION_CODES.O)
fun HealthTrackingDto.toHealthTracking() = HealthTracking(
    id = id,
    patientId = patientId,
    doctorId = doctorId,
    status = status,
    description = description,
    lastVisit = lastVisit?.let { LocalDateTime.parse(it, DateTimeFormatter.ISO_LOCAL_DATE_TIME) }
)