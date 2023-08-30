package com.apex.codeassesment.data.local.prefrences

interface PreferencesManager {
    fun saveUser(user: String)
    fun loadUser():String?
}