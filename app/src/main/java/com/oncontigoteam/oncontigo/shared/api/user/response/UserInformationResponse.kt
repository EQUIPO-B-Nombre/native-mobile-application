package     com.oncontigoteam.oncontigo.shared.api.user.response

public final data class UserInformationResponse(
    public val id: Long,
    public val userId: String,
    public val firstName: String,
    public val lastName: String,
    public val city: String,
    public val country: String,
    public val birthDate: String,
    public val description: String,
    public val photo: String,
    public val experience: Int,
    public val dni: String,
    public val phone: String
) {
    public fun formatAndName(): String {
        return "$firstName $lastName"
    }
    public fun formatAndPhone(): String {
        return "+51 $phone"
    }
}
