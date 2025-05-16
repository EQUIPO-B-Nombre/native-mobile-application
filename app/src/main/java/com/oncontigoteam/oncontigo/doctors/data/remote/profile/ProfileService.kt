package com.oncontigoteam.oncontigo.doctors.data.remote.profile

import com.oncontigoteam.oncontigo.doctors.domain.profile.UpdateProfile
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ProfileService {
    @GET("profiles/{id}")
    suspend fun getProfileById(@Path("id") id: Long, @Header("Authorization") token: String): Response<ProfileDto>

    @PUT("profiles/{id}")
    suspend fun updateProfile(@Path("id") id: Long, @Header("Authorization") token: String, profile: UpdateProfile): Response<ProfileDto>

    @GET("profiles/{userId}/user")
    suspend fun getProfileByUserId(@Path("userId") id: Long, @Header("Authorization") token: String): Response<ProfileDto>

    @GET("profiles")
    suspend fun getAllProfiles(@Header("Authorization") token: String): Response<List<ProfileDto>>
}