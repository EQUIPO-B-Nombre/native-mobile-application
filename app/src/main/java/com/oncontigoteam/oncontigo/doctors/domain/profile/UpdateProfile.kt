package com.oncontigoteam.oncontigo.doctors.domain.profile

import java.util.Date

data class UpdateProfile(
    val firstName: String,
    val lastName: String,
    val city: String,
    val country: String,
    val birthDate: Date,
    val description: String,
    val photo: String,
    val experience: Long,
    val dni: String,
    val phone: String
)
