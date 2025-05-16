package com.oncontigoteam.oncontigo.doctors.data.remote.healthtracking

import com.oncontigoteam.oncontigo.doctors.domain.healthtracking.CreateHealthTracking
import com.oncontigoteam.oncontigo.doctors.domain.healthtracking.UpdateHealthTracking
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface HealthTrackingService {
    @GET("healthtrackings/{id}")
    suspend fun getHealthTrackingById(@Path("id") id: Long, @Header("Authorization") token: String): Response<HealthTrackingDto>

    @PUT("healthtrackings/{id}")
    suspend fun updateHealthTracking(@Path("id") id: Long, @Header("Authorization") token: String, @Body healthTracking: UpdateHealthTracking): Response<HealthTrackingDto>

    @DELETE("healthtrackings/{id}")
    suspend fun deleteHealthTracking(@Path("id") id: Long, @Header("Authorization") token: String): Response<Unit>

    @POST("healthtrackings")
    suspend fun createHealthTracking(@Header("Authorization") token: String, @Body healthTracking: CreateHealthTrackingDto): Response<HealthTrackingDto>

    @GET("healthtrackings/doctor/{doctorId}")
    suspend fun getHealthTrackingByDoctorId(@Path("doctorId") doctorId: Long, @Header("Authorization") token: String): Response<List<HealthTrackingDto>>

}