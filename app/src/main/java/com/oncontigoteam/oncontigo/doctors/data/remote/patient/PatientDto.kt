package com.oncontigoteam.oncontigo.doctors.data.remote.patient

import com.oncontigoteam.oncontigo.doctors.domain.patient.Patient

data class PatientDto(
    val id: Long,
    val userId: Long
)

fun PatientDto.toPatient() = Patient(id, userId)