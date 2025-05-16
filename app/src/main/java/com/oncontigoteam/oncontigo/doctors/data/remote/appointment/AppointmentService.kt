package com.oncontigoteam.oncontigo.doctors.data.remote.appointment

import com.oncontigoteam.oncontigo.doctors.domain.appointment.UpdateAppointment
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AppointmentService {
    @GET("appointments/{id}")
    suspend fun getAppointmentById(@Path("id") id: Long, @Header("Authorization") token: String): Response<AppointmentDto>

    @PUT("appointments/{id}")
    suspend fun updateAppointment(@Path("id") id: Long, @Header("Authorization") token: String, @Body appointment: UpdateAppointment): Response<AppointmentDto>

    @DELETE("appointments/{id}")
    suspend fun deleteAppointment(@Path("id") id: Long, @Header("Authorization") token: String): Response<Unit>

    @POST("appointments")
    suspend fun createAppointment(@Header("Authorization") token: String, @Body appointment: CreateAppointmentDto): Response<AppointmentDto>

    @GET("appointments/healthtracking/{healthTrackingId}")
    suspend fun getAppointmentByHealthTrackingId(@Path("healthTrackingId") healthTrackingId: Long, @Header("Authorization") token: String): Response<List<AppointmentDto>>
}