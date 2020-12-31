package models

data class AuthResponse(val user: User, val token: String?)
data class OTPResponse(val user: User, val otp: String?)