package uk.ac.tees.mad.blisstimer.domain.model

data class User(
    val uid: String = "",
    val profilePhoto : String = "",
    val name : String = "",
    val email: String = "",
    val password : String = "",
    val number : String = ""
)
