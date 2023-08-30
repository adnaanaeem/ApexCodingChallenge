package com.apex.codeassesment.data.repository

import com.apex.codeassesment.data.model.User
import com.apex.codeassesment.data.remote.ApiResult
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getSavedUser(): User
    suspend fun getUser(forceUpdate: Boolean): Flow<ApiResult<User>>
    suspend fun getUsers(): Flow<ApiResult<List<User>>>
}