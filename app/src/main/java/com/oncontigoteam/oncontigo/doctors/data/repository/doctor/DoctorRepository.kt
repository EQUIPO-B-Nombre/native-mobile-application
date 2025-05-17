package com.oncontigoteam.oncontigo.doctors.data.repository.doctor

import com.oncontigoteam.oncontigo.doctors.common.Resource
import com.oncontigoteam.oncontigo.doctors.data.remote.doctor.DoctorService
import com.oncontigoteam.oncontigo.doctors.data.remote.doctor.toDoctor
import com.oncontigoteam.oncontigo.doctors.domain.doctor.Doctor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DoctorRepository(private val doctorService: DoctorService) {

    suspend fun searchAllDoctors(token: String): Resource<List<Doctor>> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"
        val response = doctorService.getAllDoctors(bearerToken)
        if (response.isSuccessful) {
            response.body()?.let { doctorList ->
                val doctors = doctorList.map { it.toDoctor() }
                return@withContext Resource.Success(doctors)
            }
            return@withContext Resource.Error(message = "No se encontraron doctores")
        }
        return@withContext Resource.Error(response.message())
    }

    suspend fun searchDoctorById(doctorId: Long, token: String): Resource<Doctor> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"
        val response = doctorService.getDoctorById(doctorId, bearerToken)
        if (response.isSuccessful) {
            response.body()?.let { doctorDto ->
                val doctor = doctorDto.toDoctor()
                return@withContext Resource.Success(doctor)
            }
            return@withContext Resource.Error(message = "No se encontró doctor")
        }
        return@withContext Resource.Error(response.message())
    }

    suspend fun deleteDoctor(doctorId: Long, token: String): Resource<Unit> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"
        val response = doctorService.deleteDoctor(doctorId, bearerToken)
        if (response.isSuccessful) {
            return@withContext Resource.Success(Unit)
        }
        return@withContext Resource.Error(response.message())
    }
}