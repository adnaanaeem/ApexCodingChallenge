package com.apex.codeassesment.data.network

import com.apex.codeassesment.data.model.UserResponse
import retrofit2.http.*

interface ApiService {
    @GET("/api")
    suspend fun getRandomUser(): UserResponse
    @GET("/api?results=10")
    suspend fun getUsersList(): UserResponse
}