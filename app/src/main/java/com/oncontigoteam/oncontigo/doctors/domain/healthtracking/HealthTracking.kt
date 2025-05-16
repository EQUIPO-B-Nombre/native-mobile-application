package com.oncontigoteam.oncontigo.doctors.domain.healthtracking

import java.time.LocalDateTime

data class HealthTracking(
    val id: Long,
    val patientId: Long,
    val doctorId: Long,
    val status: String,
    val description: String,
    val lastVisit: LocalDateTime?
)