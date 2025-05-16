package com.oncontigoteam.oncontigo.doctors.domain.procedure

import java.time.LocalDateTime
import java.util.Date

data class UpdateProcedure(
    val name: String,
    val description: String,
    val performedAt: LocalDateTime
)
