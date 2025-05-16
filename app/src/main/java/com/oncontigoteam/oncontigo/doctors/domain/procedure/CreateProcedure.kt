package com.oncontigoteam.oncontigo.doctors.domain.procedure

import java.time.LocalDateTime
import java.util.Date

data class CreateProcedure(
    val name: String,
    val description: String,
    val performedAt: LocalDateTime,
    val healthTrackingId: Long
)
