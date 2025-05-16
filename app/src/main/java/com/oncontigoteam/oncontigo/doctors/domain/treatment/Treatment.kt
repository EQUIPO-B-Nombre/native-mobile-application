package com.oncontigoteam.oncontigo.doctors.domain.treatment

import java.time.LocalDateTime
import java.util.Date

data class Treatment(
    val id: Long,
    val name: String,
    val description: String,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val healthTrackingId: Long
)
