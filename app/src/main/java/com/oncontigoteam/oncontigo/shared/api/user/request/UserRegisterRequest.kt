package     com.oncontigoteam.oncontigo.shared.api.user.request

import org.json.JSONArray
import      org.json.JSONObject

public final data class UserRegisterRequest(
    public val username: String,
    public val password: String,
    public val roles: List<String>
) {
    public fun toJsonString(): String {
        return JSONObject().apply {
            put("username", username)
            put("password", password)
            put("roles", JSONArray(roles))
        }.toString()
    }
}