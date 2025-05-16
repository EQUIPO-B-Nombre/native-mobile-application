package com.oncontigoteam.oncontigo.doctors.domain.appointment

import java.time.LocalDateTime
import java.util.Date

data class CreateAppointment(
    val dateTime: LocalDateTime,
    val description: String,
    val healthTrackingId: Long
)