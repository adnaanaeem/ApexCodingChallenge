package com.apex.codeassesment.data.remote

import com.apex.codeassesment.data.model.User
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {
    suspend fun loadUser(): Flow<ApiResult<User>>
    suspend fun loadUsers(): Flow<ApiResult<List<User>>>
}