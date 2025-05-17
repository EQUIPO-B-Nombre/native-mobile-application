package com.oncontigoteam.oncontigo.doctors.data.remote.treatment

import com.oncontigoteam.oncontigo.doctors.domain.treatment.CreateTreatment
import com.oncontigoteam.oncontigo.doctors.domain.treatment.UpdateTreatment
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface TreatmentService {
    @GET("treatments/{id}")
    suspend fun getTreatmentById(@Path("id") id: Long, @Header("Authorization") token: String): Response<TreatmentDto>

    @PUT("treatments/{id}")
    suspend fun updateTreatment(@Path("id") id: Long, @Header("Authorization") token: String, @Body treatment: UpdateTreatment): Response<TreatmentDto>

    @DELETE("treatments/{id}")
    suspend fun deleteTreatment(@Path("id") id: Long, @Header("Authorization") token: String): Response<Unit>

    @POST("treatments")
    suspend fun createTreatment(@Header("Authorization") token: String, @Body treatment: CreateTreatmentDto): Response<TreatmentDto>

    @GET("treatments/healthtracking/{healthTrackingId}")
    suspend fun getTreatmentByHealthTrackingId(@Path("healthTrackingId") healthTrackingId: Long, @Header("Authorization") token: String): Response<List<TreatmentDto>>
}