package models

import database.tables.Gender
import database.tables.ShowMe

data class UserDetails(
    var id: Int? = null,
    val userId: Int,
    var token: String,
    val phone: String,
    val name: String,
    val email: String,
    val dob: Long,
    val gender: Gender,
    val orientations: String,
    val showOrientations: Boolean,
    val showGender: Boolean,
    val showMe: ShowMe,
    val university: String,
    val passions: String,
)
