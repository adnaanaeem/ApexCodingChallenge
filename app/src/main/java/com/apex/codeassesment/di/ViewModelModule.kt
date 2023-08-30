package com.apex.codeassesment.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.apex.codeassesment.ui.main.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindYourViewModel(viewModel: MainViewModel): ViewModel
}
