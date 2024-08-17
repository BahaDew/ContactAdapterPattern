package com.example.contactadapterpattern.data.source.remote.request

data class UserVerifyRequest(
    val phone : String,
    val code : String
)