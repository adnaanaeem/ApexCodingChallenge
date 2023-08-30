package com.apex.codeassesment.data.local

import com.apex.codeassesment.data.local.prefrences.PreferencesManager
import com.apex.codeassesment.data.local.prefrences.PreferencesManagerImpl
import com.apex.codeassesment.data.model.User
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import javax.inject.Inject
import javax.inject.Singleton

// TODO (3 points): Convert to Kotlin
// TODO (2 point): Add tests
// TODO (1 point): Use the correct naming conventions.
@Singleton
class LocalDataSourceImpl @Inject constructor(
    private val preferencesManager: PreferencesManager,
    // TODO (3 points): Inject all dependencies instead of instantiating them.
    private val moshi: Moshi
): LocalDataSource {
    override fun loadUser(): User {
        val serializedUser = preferencesManager.loadUser()
        val jsonAdapter: JsonAdapter<User> = moshi.adapter(User::class.java)
        return try {
            // TODO (1 point): Address Android Studio warning
            val user = serializedUser?.let { jsonAdapter.fromJson(it) }
            user ?: User.createRandom()
        } catch (e: Exception) {
            e.printStackTrace()
            User.createRandom()
        }
    }

    override fun saveUser(user: User) {
        val jsonAdapter: JsonAdapter<User> = moshi.adapter(User::class.java)
        val serializedUser = jsonAdapter.toJson(user)
        preferencesManager.saveUser(serializedUser)
    }
}
