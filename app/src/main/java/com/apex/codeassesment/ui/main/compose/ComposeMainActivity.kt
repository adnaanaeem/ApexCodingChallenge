package com.apex.codeassesment.ui.main.compose

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.apex.codeassesment.RandomUserApplication
import com.apex.codeassesment.ui.details.DetailsActivity
import com.apex.codeassesment.ui.main.MainViewModel
import com.apex.codeassesment.ui.main.UiEvent
import com.apex.codeassesment.utils.Constants
import com.apex.codeassesment.utils.navigateToActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class ComposeMainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as RandomUserApplication).inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]

        observeState()

        setContent {
            MainScreen(
                viewModel.uiState.collectAsState().value,
                viewModel.userFlowList,
                onEvent = { eventValue -> viewModel.onEvent(event = eventValue) }
            )
        }
    }

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiEvent.collectLatest { event ->
                        when (event) {
                            // TODO (2 points): Convert to extenstion function.
                            is UiEvent.NavigateToUser -> navigateToActivity<DetailsActivity>(
                                Constants.SAVED_USER_KEY to event.user,
                            )
                            UiEvent.NavigateToCompose -> navigateToActivity<ComposeMainActivity>()
                        }
                    }
                }
            }
        }
    }
}