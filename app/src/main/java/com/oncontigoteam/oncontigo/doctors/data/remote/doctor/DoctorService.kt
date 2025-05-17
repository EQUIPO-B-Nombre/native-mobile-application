package com.oncontigoteam.oncontigo.doctors.data.remote.doctor

import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface DoctorService {
    @GET("doctors")
    suspend fun getAllDoctors(@Header("Authorization") token: String): Response<List<DoctorDto>>

    @GET("doctors/{id}")
    suspend fun getDoctorById(@Path("id") id: Long, @Header("Authorization") token: String): Response<DoctorDto>

    @DELETE("doctors/{id}")
    suspend fun deleteDoctor(@Path("id") id: Long, @Header("Authorization") token: String): Response<Unit>

}