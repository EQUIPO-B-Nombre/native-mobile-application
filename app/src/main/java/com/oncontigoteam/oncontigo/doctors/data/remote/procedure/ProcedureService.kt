package com.oncontigoteam.oncontigo.doctors.data.remote.procedure

import com.oncontigoteam.oncontigo.doctors.domain.procedure.CreateProcedure
import com.oncontigoteam.oncontigo.doctors.domain.procedure.UpdateProcedure
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ProcedureService {
    @GET("procedures/{id}")
    suspend fun getProcedureById(@Path("id") id: Long, @Header("Authorization") token: String): Response<ProcedureDto>

    @PUT("procedures/{id}")
    suspend fun updateProcedure(@Path("id") id: Long, @Header("Authorization") token: String, @Body procedure: UpdateProcedure): Response<ProcedureDto>

    @DELETE("procedures/{id}")
    suspend fun deleteProcedure(@Path("id") id: Long, @Header("Authorization") token: String): Response<Unit>

    @POST("procedures")
    suspend fun createProcedure(@Header("Authorization") token: String, @Body procedure: CreateProcedureDto): Response<ProcedureDto>

    @GET("procedures/healthtracking/{healthTrackingId}")
    suspend fun getProcedureByHealthTrackingId(@Path("healthTrackingId") healthTrackingId: Long, @Header("Authorization") token: String): Response<List<ProcedureDto>>
}