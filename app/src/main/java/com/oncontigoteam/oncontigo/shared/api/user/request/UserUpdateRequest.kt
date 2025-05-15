package     com.oncontigoteam.oncontigo.shared.api.user.request


public final data class UserUpdateRequest(
    public val userId: Int,
    public val companyName: String,
    public val email: String,
    public val ruc: String
)