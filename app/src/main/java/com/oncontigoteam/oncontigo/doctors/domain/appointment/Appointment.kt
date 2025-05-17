package com.oncontigoteam.oncontigo.doctors.domain.appointment

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class Appointment(
    val id: Long,
    val dateTime: LocalDateTime,
    val description: String,
    val healthTrackingId: Long,
)