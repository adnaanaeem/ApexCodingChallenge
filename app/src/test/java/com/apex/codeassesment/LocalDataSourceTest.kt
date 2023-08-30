package com.apex.codeassesment

import com.apex.codeassesment.data.local.LocalDataSource
import com.apex.codeassesment.data.local.LocalDataSourceImpl
import com.apex.codeassesment.data.local.prefrences.PreferencesManager
import com.apex.codeassesment.data.model.Name
import com.apex.codeassesment.data.model.User
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class LocalDataSourceTest {

    @Mock
    lateinit var preferencesManager: PreferencesManager

    @Mock
    lateinit var moshi: Moshi

    private lateinit var localDataSource: LocalDataSource

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        preferencesManager = mock()
        moshi = mock()
        localDataSource = LocalDataSourceImpl(preferencesManager, moshi)

        // Mock the behavior of moshi.adapter(User::class.java)
        val moshiAdapterMock: JsonAdapter<User> = mock()
        whenever(moshi.adapter(User::class.java)).thenReturn(moshiAdapterMock)
    }

    @Test
    fun `loadUser returns user from preferences`() {
        val serializedUser = """{
            "gender": "male",
            "name": {
                "first": "John",
                "last": "Doe"
            },
            "location": {...}, 
            "email": "john@example.com",
            "login": {...}, 
            "dob": {...}, 
            "registered": {...}, 
            "phone": "23-456-5890",
            "cell": "000-025-1235",
            "id": {...}, 
            "picture": {...},
            "nat": "US"
        }"""

        val user = User(
            gender = "male",
            name = Name("Adnan", "Naeem"),
            email = "adnan@gmail.com",
            phone = "123-456-7890",
            cell = "000-123-4567",
            nat = "US"
        )

        val jsonAdapter: JsonAdapter<User> = mock()
        whenever(preferencesManager.loadUser()).thenReturn(serializedUser)
        whenever(moshi.adapter(User::class.java)).thenReturn(jsonAdapter)
        whenever(jsonAdapter.fromJson(serializedUser)).thenReturn(user)

        // Act
        val loadedUser = localDataSource.loadUser()

        // Assert
        assertEquals(user, loadedUser)
    }


    @Test
    fun `loadUser returns random user if deserialization fails`() {
        // Arrange
        val serializedUser = """{"name":"Adnan","age":30}"""
        val jsonAdapter: JsonAdapter<User> = mock()
        whenever(preferencesManager.loadUser()).thenReturn(serializedUser)
        whenever(moshi.adapter(User::class.java)).thenReturn(jsonAdapter)
        whenever(jsonAdapter.fromJson(serializedUser)).thenThrow(RuntimeException())

        // Act
        val loadedUser = localDataSource.loadUser()

        // Assert
        assertNotNull(loadedUser)
    }

    @Test
    fun `loadUser returns random user if preferences are empty`() {
        // Arrange
        whenever(preferencesManager.loadUser()).thenReturn(null)

        val moshiAdapterMock: JsonAdapter<User> = mock()
        whenever(moshi.adapter(User::class.java)).thenReturn(moshiAdapterMock)

        val defaultUser = User.createRandom()
        whenever(moshiAdapterMock.fromJson(any<String>())).thenReturn(defaultUser)

        // Act
        val loadedUser = localDataSource.loadUser()

        // Assert
        assertNotNull(loadedUser)
    }

    @Test
    fun `saveUser stores user in preferences`() {
        // Arrange
        val user = User("Male", Name("Adnan", "Naeem"))
        val jsonAdapter: JsonAdapter<User> = mock()
        val serializedUser = """{"name":"Male","age":25}"""
        whenever(moshi.adapter(User::class.java)).thenReturn(jsonAdapter)
        whenever(jsonAdapter.toJson(user)).thenReturn(serializedUser)

        // Act
        localDataSource.saveUser(user)

        // Assert
        verify(preferencesManager).saveUser(serializedUser)
    }
}
