package com.apex.codeassesment

import android.app.Application
import com.apex.codeassesment.di.DaggerMainComponent
import com.apex.codeassesment.di.MainComponent
import com.apex.codeassesment.ui.main.compose.ComposeMainActivity
import com.apex.codeassesment.ui.main.MainActivity

class RandomUserApplication : Application(), MainComponent.Injector {
  override val mainComponent = DaggerMainComponent.factory().create(this)
  fun inject(activity: MainActivity) {
    mainComponent.inject(activity)
  }
  fun inject(activity: ComposeMainActivity) {
    mainComponent.inject(activity)
  }
}