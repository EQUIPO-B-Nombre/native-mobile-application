package com.oncontigoteam.oncontigo.doctors.data.remote.doctor

import com.oncontigoteam.oncontigo.doctors.domain.doctor.Doctor

data class DoctorDto(
    val id: Long,
    val userId: Long,
    val experience: Long
)

fun DoctorDto.toDoctor() = Doctor(id, userId, experience)