package com.oncontigoteam.oncontigo.doctors.data.remote.profile

import com.oncontigoteam.oncontigo.doctors.domain.profile.Profile
import java.util.Date

data class ProfileDto(
    val id: Long,
    val userId: Long,
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

fun ProfileDto.toProfile() = Profile(id, userId, firstName, lastName, city, country, birthDate, description, photo, experience, dni, phone)
