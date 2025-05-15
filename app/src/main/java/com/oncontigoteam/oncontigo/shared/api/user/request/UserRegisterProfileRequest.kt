package     com.oncontigoteam.oncontigo.shared.api.user.request

import      com.oncontigoteam.oncontigo.shared.user.UserWrapperSettings
import      org.json.JSONObject

public final data class UserRegisterProfileRequest(
    public val firstName: String,
    public val lastName: String,
    public val city: String,
    public val country: String,
    public val birthDate: String,
    public val description: String,
    public val photo: String,
    public val experience: Long,
    public val dni: String,
    public val phone: String
) {
    public fun toJsonString(): String {
        return JSONObject().apply {
            put("userId", UserWrapperSettings.userId)
            put("firstName", firstName)
            put("lastName", lastName)
            put("city", city)
            put("country", country)
            put("birthDate", birthDate)
            put("description", description)
            put("photo", photo)
            put("experience", experience)
            put("dni", dni)
            put("phone", phone)
        }.toString()
    }
}