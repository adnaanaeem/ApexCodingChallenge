package com.apex.codeassesment

import android.content.Context
import android.content.SharedPreferences
import com.apex.codeassesment.data.local.prefrences.PreferencesManager
import com.apex.codeassesment.data.local.prefrences.PreferencesManagerImpl
import com.apex.codeassesment.utils.Constants
import com.apex.codeassesment.utils.Constants.SAVED_USER
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PreferencesManagerTest {

    @Mock
    private lateinit var context: Context

    @Mock
    private lateinit var sharedPreferences: SharedPreferences

    @Mock
    private lateinit var editor: SharedPreferences.Editor

    private lateinit var preferencesManager: PreferencesManager

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        whenever(
            context.getSharedPreferences(
                Constants.RANDOM_USER_PREFERENCES,
                Context.MODE_PRIVATE
            )
        ).thenReturn(sharedPreferences)
        whenever(sharedPreferences.edit()).thenReturn(editor)

        preferencesManager = PreferencesManagerImpl(context)
    }

    @Test
    fun `saveUser should store user in SharedPreferences`() {
        // Arrange
        val user = "Serialized User"
        // Act
        preferencesManager.saveUser(user)
        // Assert
        verify(editor).putString(SAVED_USER, user)
    }


    @Test
    fun `loadUser should retrieve user from SharedPreferences`() {
        // Arrange
        val user = "Serialized User"
        whenever(sharedPreferences.getString(SAVED_USER, null)).thenReturn(user)
        // Act
        val loadedUser = preferencesManager.loadUser()
        // Assert
        assert(loadedUser == user)
    }

    @Test
    fun `loadUser should return null if user is not saved in SharedPreferences`() {
        // Arrange
        whenever(sharedPreferences.getString(SAVED_USER, null)).thenReturn(null)
        // Act
        val loadedUser = preferencesManager.loadUser()
        // Assert
        assert(loadedUser == null)
    }
}
