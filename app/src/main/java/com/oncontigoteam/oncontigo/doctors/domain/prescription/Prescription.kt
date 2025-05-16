package com.oncontigoteam.oncontigo.doctors.domain.prescription

data class Prescription(
    val id: Long,
    val patientId: Long,
    val doctorId: Long,
    val medicationName: String,
    val dosage: String
)