package com.example.contactadapterpattern.utils

import android.widget.EditText
import com.google.android.material.textfield.TextInputEditText
import com.vicmikhailau.maskededittext.MaskedEditText
import timber.log.Timber

fun <T> T.myApply(block : T.() -> Unit) {
    block(this)
}

fun myLogger(message : String, tag : String = "BAHA") {
    Timber.tag(tag).d(message)
}

fun EditText.text2() : String {
    return this.text.toString()
}

fun MaskedEditText.toPhoneHandMade() : String {
    return "+998" + this.text.toString().replace(" ", "")
}