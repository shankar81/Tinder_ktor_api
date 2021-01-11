package models

data class AuthResponse(val user: User, val token: String?, val isOldUser: Boolean)
data class OTPResponse(val user: User, val otp: String?)