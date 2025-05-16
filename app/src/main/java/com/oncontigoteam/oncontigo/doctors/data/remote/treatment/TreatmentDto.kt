package com.oncontigoteam.oncontigo.doctors.data.remote.treatment

import android.os.Build
import androidx.annotation.RequiresApi
import com.oncontigoteam.oncontigo.doctors.domain.treatment.Treatment
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

data class TreatmentDto(
    val id: Long,
    val name: String,
    val description: String,
    val startDate: String,
    val endDate: String,
    val healthTrackingId: Long
)

data class CreateTreatmentDto(
    val name: String,
    val description: String,
    val startDate: String,
    val endDate: String,
    val healthTrackingId: Long
)

@RequiresApi(Build.VERSION_CODES.O)
fun TreatmentDto.toTreatment() = Treatment(
    id,
    name,
    description,
    startDate.let { LocalDateTime.parse(it, DateTimeFormatter.ISO_LOCAL_DATE_TIME) },
    endDate.let { LocalDateTime.parse(it, DateTimeFormatter.ISO_LOCAL_DATE_TIME) },
    healthTrackingId)
