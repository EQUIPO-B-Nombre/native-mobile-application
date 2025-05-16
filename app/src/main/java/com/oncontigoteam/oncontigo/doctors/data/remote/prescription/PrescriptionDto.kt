package com.oncontigoteam.oncontigo.doctors.data.remote.prescription

import com.oncontigoteam.oncontigo.doctors.domain.prescription.Prescription

data class PrescriptionDto(
    val id: Long,
    val patientId: Long,
    val doctorId: Long,
    val medicationName: String,
    val dosage: String
)

fun PrescriptionDto.toPrescription() = Prescription(id, patientId, doctorId, medicationName, dosage)