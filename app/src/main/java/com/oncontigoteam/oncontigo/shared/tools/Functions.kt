package     com.oncontigoteam.oncontigo.shared.tools

public final object Functions {
    public fun isValidEmail(email: String): Boolean {
        val emailRegex: String = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$"
        return Regex(emailRegex).matches(email)
    }
}