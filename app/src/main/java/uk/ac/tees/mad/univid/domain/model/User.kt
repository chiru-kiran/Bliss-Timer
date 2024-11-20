package uk.ac.tees.mad.univid.domain.model

data class User(
    val uid: String = "",
    val name : String = "",
    val email: String = "",
    val password : String = "",
    val number : String = ""
)
