package com.oncontigoteam.oncontigo.doctors.data.repository.profile

import com.oncontigoteam.oncontigo.doctors.common.Resource
import com.oncontigoteam.oncontigo.doctors.data.remote.profile.ProfileService
import com.oncontigoteam.oncontigo.doctors.data.remote.profile.toProfile
import com.oncontigoteam.oncontigo.doctors.domain.profile.Profile
import com.oncontigoteam.oncontigo.doctors.domain.profile.UpdateProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProfileRepository(private val profileService: ProfileService) {
    suspend fun searchProfileById(profileId: Long, token: String): Resource<Profile> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"
        val response = profileService.getProfileById(profileId, bearerToken)
        if (response.isSuccessful) {
            response.body()?.let { profileDto ->
                val profile = profileDto.toProfile()
                return@withContext Resource.Success(profile)
            }
            return@withContext Resource.Error(message = "No se pudo obtener el perfil")
        }
        return@withContext Resource.Error(response.message())
    }

    suspend fun updateProfile(profileId: Long, token: String, profile: UpdateProfile): Resource<Profile> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"
        val response = profileService.updateProfile(profileId, bearerToken, profile)
        if (response.isSuccessful) {
            response.body()?.let { profileDto ->
                val updatedProfile = profileDto.toProfile()
                return@withContext Resource.Success(updatedProfile)
            }
            return@withContext Resource.Error(message = "No se pudo actualizar el perfil")
        }
        return@withContext Resource.Error(response.message())
    }

    suspend fun searchProfileByUserId(userId: Long, token: String): Resource<Profile> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"
        val response = profileService.getProfileByUserId(userId, bearerToken)
        if (response.isSuccessful) {
            response.body()?.let { profileDto ->
                val profile = profileDto.toProfile()
                return@withContext Resource.Success(profile)
            }
            return@withContext Resource.Error(message = "No se pudo obtener el perfil")
        }
        return@withContext Resource.Error(response.message())
    }

    suspend fun searchAllProfiles(token: String): Resource<List<Profile>> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"
        val response = profileService.getAllProfiles(bearerToken)
        if (response.isSuccessful) {
            response.body()?.let { profileDtoList ->
                val profileList = profileDtoList.map { it.toProfile() }
                return@withContext Resource.Success(profileList)
            }
            return@withContext Resource.Error(message = "No se pudo obtener la lista de perfiles")
        }
        return@withContext Resource.Error(response.message())
    }
}