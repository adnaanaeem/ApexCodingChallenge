package com.apex.codeassesment.data.repository

import com.apex.codeassesment.data.local.LocalDataSource
import com.apex.codeassesment.data.model.User
import com.apex.codeassesment.data.remote.ApiResult
import com.apex.codeassesment.data.remote.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject
import javax.inject.Singleton

// TODO (2 points) : Add tests
// TODO (3 points) : Hide this class through an interface, inject the interface in the clients instead and remove warnings
@Singleton
class UserRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) : UserRepository {

    private val savedUser = AtomicReference(User())

    override fun getSavedUser() = localDataSource.loadUser()

    override suspend fun getUser(forceUpdate: Boolean): Flow<ApiResult<User>> {
        if (forceUpdate) {
            val userResult = remoteDataSource.loadUser()
            // Just to save value for savedUser Object
            userResult.collect { apiResult ->
                when (apiResult) {
                    is ApiResult.Success -> {
                        val user = apiResult.data
                        localDataSource.saveUser(user)
                        savedUser.set(user)
                    }

                    // do nothing if not fetched because we already returning flow value to Ui
                    is ApiResult.Loading,
                    is ApiResult.Error -> Unit
                }
            }
            return userResult
        }
        return flowOf(ApiResult.Success(savedUser.get()))
    }

    override suspend fun getUsers() = remoteDataSource.loadUsers()
}
