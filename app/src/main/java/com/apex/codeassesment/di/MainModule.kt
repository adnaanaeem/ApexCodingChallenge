package com.apex.codeassesment.di

import android.content.Context
import android.content.SharedPreferences
import com.apex.codeassesment.data.local.LocalDataSource
import com.apex.codeassesment.data.local.LocalDataSourceImpl
import com.apex.codeassesment.data.local.prefrences.PreferencesManager
import com.apex.codeassesment.data.local.prefrences.PreferencesManagerImpl
import com.apex.codeassesment.data.network.ApiService
import com.apex.codeassesment.data.network.RetrofitBuilder
import com.apex.codeassesment.data.remote.RemoteDataSource
import com.apex.codeassesment.data.remote.RemoteDataSourceImpl
import com.apex.codeassesment.data.repository.UserRepository
import com.apex.codeassesment.data.repository.UserRepositoryImpl
import com.apex.codeassesment.utils.Constants.RANDOM_USER_PREFERENCES
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object MainModule {
    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder().build()
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(RANDOM_USER_PREFERENCES, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun providePreferencesManager(context: Context): PreferencesManager =
        PreferencesManagerImpl(context)

    @Provides
    @Singleton
    fun provideApiService(): ApiService = RetrofitBuilder.apiService

    @Provides
    @Singleton
    fun provideLocalDataSource(
        preferencesManager: PreferencesManager,
        moshi: Moshi
    ): LocalDataSource = LocalDataSourceImpl(preferencesManager, moshi)

    @Provides
    @Singleton
    fun provideRemoteDataSource(
        localDataSource: LocalDataSource,
        apiService: ApiService
    ): RemoteDataSource = RemoteDataSourceImpl(localDataSource, apiService)

    @Provides
    @Singleton
    fun provideUserRepository(
        localDataSource: LocalDataSource,
        remoteDataSource: RemoteDataSource
    ): UserRepository = UserRepositoryImpl(localDataSource, remoteDataSource)

}