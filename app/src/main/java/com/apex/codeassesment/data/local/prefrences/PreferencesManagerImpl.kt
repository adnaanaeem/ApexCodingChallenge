package com.apex.codeassesment.data.local.prefrences

import android.content.Context
import com.apex.codeassesment.utils.Constants.RANDOM_USER_PREFERENCES
import com.apex.codeassesment.utils.Constants.SAVED_USER
import javax.inject.Inject
import javax.inject.Singleton

// TODO (2 point): Add tests
@Singleton
class PreferencesManagerImpl @Inject constructor(
    private val context: Context,
):PreferencesManager {
    override fun saveUser(user: String) {
        val sharedPreferences =
            context.getSharedPreferences(RANDOM_USER_PREFERENCES, Context.MODE_PRIVATE)
        sharedPreferences?.edit()?.putString(SAVED_USER, user)?.apply()
    }

    override fun loadUser(): String? {
        val sharedPreferences =
            context.getSharedPreferences(RANDOM_USER_PREFERENCES, Context.MODE_PRIVATE)
        return sharedPreferences?.getString(SAVED_USER, null)
    }
}
