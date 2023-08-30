package com.apex.codeassesment.data.remote

import com.apex.codeassesment.data.local.LocalDataSource
import com.apex.codeassesment.data.model.User
import com.apex.codeassesment.data.network.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

// TODO (2 points): Add tests
@Singleton
class RemoteDataSourceImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val apiService: ApiService
) : RemoteDataSource {

    // TODO (5 points): Load data from endpoint https://randomuser.me/api
    //override fun LoadUser() = User.createRandom()
    override suspend fun loadUser(): Flow<ApiResult<User>> = flow {
        try {
            val response = apiService.getRandomUser()
            val user = response.results.firstOrNull() ?: User()
            // save as local Data Source
            localDataSource.saveUser(user)
            emit(ApiResult.Success(user))
        } catch (e: HttpException) {
            emit(ApiResult.Error(e))
        } catch (e: IOException) {
            emit(ApiResult.Error(e))
        }
    }

    // TODO (3 points): Load data from endpoint https://randomuser.me/api?results=10
    // TODO (Optional Bonus: 3 points): Handle success and failure from endpoints
    //override fun LoadUsers() = (0..10).map { User.createRandom() }
    override suspend fun loadUsers() = flow {
        try {
            val response = apiService.getUsersList()
            emit(ApiResult.Success(response.results))
        } catch (e: HttpException) {
            emit(ApiResult.Error(e))
        } catch (e: IOException) {
            emit(ApiResult.Error(e))
        }
    }
}
