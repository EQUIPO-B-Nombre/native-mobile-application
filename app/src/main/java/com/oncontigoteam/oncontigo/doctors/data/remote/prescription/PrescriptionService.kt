package com.oncontigoteam.oncontigo.doctors.data.remote.prescription

import com.oncontigoteam.oncontigo.doctors.domain.prescription.CreatePrescription
import com.oncontigoteam.oncontigo.doctors.domain.prescription.UpdatePrescription
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface PrescriptionService {
    @GET("prescriptions/{id}")
    suspend fun getPrescriptionById(@Path("id") id: Long, @Header("Authorization") token: String): Response<PrescriptionDto>

    @PUT("prescriptions/{id}")
    suspend fun updatePrescription(@Path("id") id: Long, @Header("Authorization") token: String, @Body prescription: UpdatePrescription): Response<PrescriptionDto>

    @POST("prescriptions")
    suspend fun createPrescription(@Header("Authorization") token: String, @Body prescription: CreatePrescription): Response<PrescriptionDto>

    @GET("prescriptions/patient/{patientId}")
    suspend fun getPrescriptionByPatientId(@Path("patientId") patientId: Long, @Header("Authorization") token: String): Response<List<PrescriptionDto>>
}