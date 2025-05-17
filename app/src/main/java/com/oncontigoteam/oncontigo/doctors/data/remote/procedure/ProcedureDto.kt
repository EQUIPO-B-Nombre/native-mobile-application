package com.oncontigoteam.oncontigo.doctors.data.remote.procedure

import android.os.Build
import androidx.annotation.RequiresApi
import com.oncontigoteam.oncontigo.doctors.domain.procedure.Procedure
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

data class ProcedureDto(
    val id: Long,
    val name: String,
    val description: String,
    val performedAt: String,
    val healthTrackingId: Long
)

data class CreateProcedureDto(
    val name: String,
    val description: String,
    val performedAt: String,
    val healthTrackingId: Long
)

@RequiresApi(Build.VERSION_CODES.O)
fun ProcedureDto.toProcedure() = Procedure(
    id,
    name,
    description,
    performedAt.let { LocalDateTime.parse(it, DateTimeFormatter.ISO_LOCAL_DATE_TIME) },
    healthTrackingId)