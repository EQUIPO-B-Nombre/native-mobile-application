package     com.oncontigoteam.oncontigo.shared.api

import      com.oncontigoteam.oncontigo.shared.api.user.UserApiService
import      com.oncontigoteam.oncontigo.shared.api.user.request.UserLoginRequest
import      com.oncontigoteam.oncontigo.shared.api.user.request.UserRegisterProfileRequest
import com.oncontigoteam.oncontigo.shared.api.user.request.UserRegisterRequest
import      com.oncontigoteam.oncontigo.shared.api.user.request.UserUpdateRequest
import      com.oncontigoteam.oncontigo.shared.api.user.response.UserInformationResponse
import      com.oncontigoteam.oncontigo.shared.api.user.response.UserUpdateResponse
import      com.oncontigoteam.oncontigo.shared.user.UserWrapperSettings
import      okhttp3.MediaType.Companion.toMediaType
import      okhttp3.OkHttpClient
import      okhttp3.Request
import      okhttp3.RequestBody
import      retrofit2.Call
import      retrofit2.Retrofit
import      retrofit2.converter.gson.GsonConverterFactory

public final class ApiWorker
{
    companion object {
    //	|-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-|
    //				    Members and Fields
    //	|-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-|

        //	-------------------------------------------
        //					Variables
        //	-------------------------------------------
        private val m_Client = OkHttpClient()



    //	|-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-|
    //			        Functions and Methods
    //	|-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-|

        //	-------------------------------------------
        //			        Functions
        //	-------------------------------------------
        //  # User
        public fun loginUser(login: UserLoginRequest): Request {
            val requestBody: RequestBody = RequestBody.create("application/json; charset=utf-8".toMediaType(), login.toJsonString())

            return Request.Builder()
                .url(OnContigoSettings.apiUrl + "/api/v1/authentication/sign-in")
                .post(requestBody)
                .addHeader("Content-Type", "application/json")
                .build()
        }
        public fun registerUserProfile(register: UserRegisterProfileRequest): Request {
            val requestBody: RequestBody = RequestBody.create("application/json; charset=utf-8".toMediaType(), register.toJsonString())

            return Request.Builder()
                .url(OnContigoSettings.apiUrl + "/api/v1/profiles")
                .post(requestBody)
                .addHeader("Content-Type", "application/json")
                .build()
        }
        public fun registerUser(register: UserRegisterRequest): Request {
            val requestBody: RequestBody = RequestBody.create("application/json; charset=utf-8".toMediaType(), register.toJsonString())

            return Request.Builder()
                .url(OnContigoSettings.apiUrl + "/api/v1/authentication/sign-up")
                .post(requestBody)
                .addHeader("Content-Type", "application/json")
                .build()
        }
        public fun userInformation(): Call<UserInformationResponse> {
            val retrofit = Retrofit.Builder()
                .baseUrl(OnContigoSettings.apiUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val service = retrofit.create(UserApiService::class.java)
            val call = service.userInformation(
                "Bearer ${UserWrapperSettings.token}", UserWrapperSettings.userId
            )

            return call
        }

        //	-------------------------------------------
        //		        Setters and Getters
        //	-------------------------------------------
        public fun client(): OkHttpClient = this.m_Client
    }
}