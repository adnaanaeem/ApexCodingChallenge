package com.apex.codeassesment.data.local

import com.apex.codeassesment.data.model.User
interface LocalDataSource {
    fun loadUser():User
    fun saveUser(user: User)
}