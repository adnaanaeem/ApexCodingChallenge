package com.apex.codeassesment

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.apex.codeassesment.data.local.LocalDataSource
import com.apex.codeassesment.data.model.User
import com.apex.codeassesment.data.model.UserResponse
import com.apex.codeassesment.data.network.ApiService
import com.apex.codeassesment.data.remote.ApiResult
import com.apex.codeassesment.data.remote.RemoteDataSource
import com.apex.codeassesment.data.remote.RemoteDataSourceImpl
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.HttpException
import java.io.IOException

@ExperimentalCoroutinesApi
class RemoteDataSourceTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var localDataSource: LocalDataSource

    @Mock
    lateinit var apiService: ApiService

    @InjectMocks
    lateinit var remoteDataSource: RemoteDataSourceImpl

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `loadUser success`() = runBlockingTest {

        val mockUser = User()
        val mockApiResponse = UserResponse(results = listOf(mockUser))
        whenever(apiService.getRandomUser()).thenReturn(mockApiResponse)

        // Act
        val flow = remoteDataSource.loadUser()

        // Assert
        flow.collect { result ->
            assert(result is ApiResult.Success)
            assert((result as ApiResult.Success).data == mockUser)
        }

        // Verify
        verify(apiService).getRandomUser()
        verify(localDataSource).saveUser(mockUser)
        verifyNoMoreInteractions(apiService, localDataSource)
    }

    @Test
    fun `loadUser failure - HttpException`() = runBlockingTest {

        val httpException = HttpException(mock())
        whenever(apiService.getRandomUser()).thenThrow(httpException)

        // Act
        val flow = remoteDataSource.loadUser()

        // Assert
        flow.collect { result ->
            assert(result is ApiResult.Error)
            assert((result as ApiResult.Error).exception == httpException)
        }

        // Verify
        verify(apiService).getRandomUser()
        verifyNoMoreInteractions(apiService, localDataSource)
    }

    @Test
    fun `loadUser failure - IOException`() = runBlockingTest {

        val ioException = IOException()

        val apiService = mock<ApiService> {
            onBlocking { getRandomUser() } doAnswer { throw ioException }
        }

        val remoteDataSource = RemoteDataSourceImpl(localDataSource, apiService)

        // Act
        val flow = remoteDataSource.loadUser()

        // Assert
        val result = flow.toList()
        assert(result.size == 1)
        assert(result[0] is ApiResult.Error)
        assert((result[0] as ApiResult.Error).exception == ioException)

        // Verify
        verify(apiService).getRandomUser()
        verifyNoMoreInteractions(apiService, localDataSource)
    }


}
