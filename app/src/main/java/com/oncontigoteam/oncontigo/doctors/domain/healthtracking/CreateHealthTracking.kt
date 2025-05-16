package com.oncontigoteam.oncontigo.doctors.domain.healthtracking

import java.time.LocalDateTime

data class CreateHealthTracking(
    val patientId: Long,
    val doctorId: Long,
    val status: String,
    val description: String,
    val lastVisit: LocalDateTime
)