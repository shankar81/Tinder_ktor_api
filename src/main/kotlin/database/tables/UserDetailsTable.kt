package database.tables

import org.jetbrains.exposed.dao.IntIdTable

enum class Gender {
    Man, Woman, Other
}

enum class ShowMe {
    Man, Woman, Everyone
}

object UserDetailsTable : IntIdTable() {
    val userId = integer("userId").uniqueIndex()
    val token = varchar("token", 300)
    val userPhone = varchar("phone", 15).uniqueIndex()
    val userName = varchar("name", 20)
    val email = varchar("email", 50).uniqueIndex()
    val dob = long("dob")
    val gender = enumeration("gender", Gender::class)
    val orientation = varchar("orientation", 300)
    val showOrientations = bool("showOrientations").default(false)
    val showGender = bool("showGender").default(false)
    val showMe = enumeration("showMe", ShowMe::class)
    val university = varchar("university", 50)
    val passions = varchar("passions", 300)
}

