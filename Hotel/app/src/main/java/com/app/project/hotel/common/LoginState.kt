package com.app.project.hotel.common

import com.chibatching.kotpref.KotprefModel

object LoginState: KotprefModel() {
    var loginState by booleanPref(false)
}