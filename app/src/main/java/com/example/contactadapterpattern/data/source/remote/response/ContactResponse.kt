package com.example.contactadapterpattern.data.source.remote.response

data class ContactResponse(
    var id : Long,
    val firstName : String,
    val lastName : String,
    val phone : String
)