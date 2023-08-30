package com.apex.codeassesment

import com.apex.codeassesment.data.local.LocalDataSource
import com.apex.codeassesment.data.model.Name
import com.apex.codeassesment.data.model.User
import com.apex.codeassesment.data.remote.ApiResult
import com.apex.codeassesment.data.remote.RemoteDataSource
import com.apex.codeassesment.data.repository.UserRepositoryImpl
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class UserRepositoryITest {

    @Mock
    private lateinit var localDataSource: LocalDataSource

    @Mock
    private lateinit var remoteDataSource: RemoteDataSource

    private lateinit var userRepository: UserRepositoryImpl

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        userRepository = UserRepositoryImpl(localDataSource, remoteDataSource)
    }

    @Test
    fun `getSavedUser should call localDataSource`() {
        // Arrange
        val expectedUser = User("Male", Name("Adnan", "Naeem"))
        whenever(localDataSource.loadUser()).thenReturn(expectedUser)

        // Act
        val result = userRepository.getSavedUser()

        // Assert
        assertEquals(expectedUser, result)
        verify(localDataSource).loadUser()
    }

    @Test
    fun `getUser with forceUpdate true should call remoteDataSource`() = runBlockingTest {
        // Arrange
        val remoteUser = User("Male", Name("Adnan", "Naeem"))
        whenever(remoteDataSource.loadUser()).thenReturn(flowOf(ApiResult.Success(remoteUser)))

        // Act
        val resultFlow = userRepository.getUser(forceUpdate = true)

        // Assert
        val result = resultFlow.single()
        assertTrue(result is ApiResult.Success)
        assertEquals(remoteUser, (result as ApiResult.Success).data)
        verify(remoteDataSource).loadUser()
    }

    @Test
    fun `getUser with forceUpdate false should return savedUser`() = runBlockingTest {
        // Arrange
        val savedUser = User("Female", Name("Seema", "Tahir"))
        val savedUserFlow = flowOf(ApiResult.Success(savedUser))
        whenever(remoteDataSource.loadUser()).thenReturn(savedUserFlow)

        // Act
        val resultFlow = userRepository.getUser(forceUpdate = false).toList()

        // Assert
        assertEquals(1, resultFlow.size)
        assertTrue(resultFlow.first() is ApiResult.Success)
        assertEquals(savedUser, (resultFlow.first() as ApiResult.Success).data)
        verifyZeroInteractions(remoteDataSource)
    }

    @Test
    fun `getUsers should call remoteDataSource`() = runBlockingTest {
        // Arrange
        val userList = flowOf( ApiResult.Success(listOf(User("Male", Name("Adnan", "Naeem")), User("Female", Name("Saima", "Noreen")))))
        whenever(remoteDataSource.loadUsers()).thenReturn(userList)

        // Act
        val result = userRepository.getUsers()

        // Assert
        assertEquals(userList, result)
        verify(remoteDataSource).loadUsers()
    }
}
