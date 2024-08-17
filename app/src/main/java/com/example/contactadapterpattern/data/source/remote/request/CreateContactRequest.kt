package com.example.contactadapterpattern.data.source.remote.request

data class CreateContactRequest(
    val firstName : String,
    val lastName : String,
    val phone : String
)