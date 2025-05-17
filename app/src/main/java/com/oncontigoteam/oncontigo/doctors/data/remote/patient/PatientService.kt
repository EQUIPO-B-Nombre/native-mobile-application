package com.oncontigoteam.oncontigo.doctors.data.remote.patient

import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface PatientService {
    @GET("patients")
    suspend fun getAllPatients(@Header("Authorization") token: String): Response<List<PatientDto>>

    @GET("patients/{id}")
    suspend fun getPatientById(@Path("id") id: Long, @Header("Authorization") token: String): Response<PatientDto>

    @DELETE("patients/{id}")
    suspend fun deletePatient(@Path("id") id: Long, @Header("Authorization") token: String): Response<Unit>
}