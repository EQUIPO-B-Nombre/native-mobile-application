package com.oncontigoteam.oncontigo.doctors.domain.prescription

data class CreatePrescription(
    val patientId: Long,
    val doctorId: Long,
    val medicationName: String,
    val dosage: String
)