package     com.oncontigoteam.oncontigo.shared.api.user

import com.oncontigoteam.oncontigo.shared.api.user.request.UserUpdateRequest
import      com.oncontigoteam.oncontigo.shared.api.user.response.UserInformationResponse
import com.oncontigoteam.oncontigo.shared.api.user.response.UserUpdateResponse
import com.oncontigoteam.oncontigo.shared.user.UserWrapperSettings
import      retrofit2.Call
import retrofit2.http.Body
import      retrofit2.http.GET
import      retrofit2.http.Header
import      retrofit2.http.PUT
import retrofit2.http.Path
import      retrofit2.http.Query



public interface UserApiService
{
    @GET("/api/v1/profiles/{userId}/user")
    public fun userInformation(
        @Header("Authorization") token: String,
        @Path("userId") id: Long
    ): Call<UserInformationResponse>
}