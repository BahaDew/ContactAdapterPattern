package com.example.contactadapterpattern.data.source.remote.request

data class UserRegisterRequest(
    val firstName : String,
    val lastName : String,
    val phone : String,
    val password : String
)