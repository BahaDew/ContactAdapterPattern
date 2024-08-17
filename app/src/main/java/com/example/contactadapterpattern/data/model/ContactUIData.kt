package com.example.contactadapterpattern.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class ContactUIData(
    val id : String,
    val firstName : String,
    val lastName : String,
    val phone : String,
) : Parcelable