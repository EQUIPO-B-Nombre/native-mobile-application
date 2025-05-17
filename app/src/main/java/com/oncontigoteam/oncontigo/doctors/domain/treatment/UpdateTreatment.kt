package com.oncontigoteam.oncontigo.doctors.domain.treatment

import java.util.Date

data class UpdateTreatment(
    val name: String,
    val description: String,
    val startDate: Date,
    val endDate: Date,
)
