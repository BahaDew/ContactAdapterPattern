package com.example.contactadapterpattern.data.mapper

import com.example.contactadapterpattern.data.model.ContactUIData
import com.example.contactadapterpattern.data.source.remote.request.CreateContactRequest
import com.example.contactadapterpattern.data.source.remote.request.UpdateContactRequest
import com.example.contactadapterpattern.data.source.remote.response.ContactResponse

object ContactMapper {
//    fun ContactResponse.toUIData(): ContactUIData =
//        ContactUIData(
//            id = this.id,
//            firstName = this.firstName,
//            lastName = this.lastName,
//            phone = this.phone,
//        )

    fun ContactUIData.toUpdateContactRequest(): UpdateContactRequest = UpdateContactRequest(
        firstName = this.firstName,
        lastName = this.lastName,
        phone = this.phone
    )

    fun ContactUIData.toCreateContactRequest(): CreateContactRequest = CreateContactRequest(
        firstName = this.firstName,
        lastName = this.lastName,
        phone = this.phone
    )

}