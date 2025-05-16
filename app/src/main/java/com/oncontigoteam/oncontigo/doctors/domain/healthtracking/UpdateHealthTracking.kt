package com.oncontigoteam.oncontigo.doctors.domain.healthtracking

import java.time.LocalDateTime

data class UpdateHealthTracking(
    val status: String,
    val description: String,
    val lastVisit: LocalDateTime
)
