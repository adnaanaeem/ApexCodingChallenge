package com.apex.codeassesment.ui.main

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.apex.codeassesment.RandomUserApplication
import com.apex.codeassesment.data.model.User
import com.apex.codeassesment.databinding.ActivityMainBinding
import com.apex.codeassesment.ui.adapter.UserAdapter
import com.apex.codeassesment.ui.details.DetailsActivity
import com.apex.codeassesment.ui.main.compose.ComposeMainActivity
import com.apex.codeassesment.utils.Constants
import com.apex.codeassesment.utils.loadImageWithGlide
import com.apex.codeassesment.utils.navigateToActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

// TODO (5 points): Move calls to repository to Presenter or ViewModel.
// TODO (5 points): Use combination of sealed/Dataclasses for exposing the data required by the view from viewModel .
// TODO (3 points): Add tests for viewModel or presenter.
// TODO (1 point): Add content description to images
// TODO (3 points): Add tests
// TODO (Optional Bonus 10 points): Make a copy of this activity with different name and convert the current layout it is using in
//  Jetpack Compose.
class MainActivity : AppCompatActivity(), UserAdapter.OnItemClickListener {

    // TODO (2 points): Convert to view binding
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: MainViewModel

    private lateinit var userAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        (application as RandomUserApplication).inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]

        observeState()

        with(binding) {
            userAdapter = UserAdapter(mutableListOf(), this@MainActivity)
            binding.mainUserList.adapter = userAdapter

            mainSeeDetailsButton.setOnClickListener {
                viewModel.onEvent(
                    MainViewEvent.UserDetailsEvent(
                        viewModel.uiState.value.randomUser
                    )
                )
            }
            mainRefreshButton.setOnClickListener { viewModel.onEvent(MainViewEvent.RefreshEvent(true)) }
            mainUserListButton.setOnClickListener { viewModel.onEvent(MainViewEvent.UserListEvent) }
            composeScreenButton.setOnClickListener { viewModel.onEvent(MainViewEvent.ComposeScreenEvent) }
        }
    }

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collect { state ->
                        updateUserDetails(state.randomUser)
                    }
                }

                launch {
                    viewModel.uiEvent.collectLatest { event ->
                        Log.e("Adnan", "collectLatest $event")
                        when (event) {
                            is UiEvent.UpdateList -> {
                                Log.e("Adnan", "userAdapter ${event.userList}")
                                userAdapter.updateUserList(event.userList)
                            }

                            // TODO (2 points): Convert to extenstion function.
                            is UiEvent.NavigateToUser -> navigateToActivity<DetailsActivity>(
                                Constants.SAVED_USER_KEY to event.user,
                            )
                            UiEvent.NavigateToCompose -> navigateToActivity<ComposeMainActivity>()
                        }
                    }
                }

//                launch {
//                    viewModel.userFlowList.collect { result ->
//                        when (result) {
//                            is ApiResult.Error -> Unit
//                            is ApiResult.Loading -> Unit // show progress if needed
//                            is ApiResult.Success -> userAdapter.updateUserList(result.data)
//                        }
//                    }
//                }
//                launch {
//                    viewModel.userFlow.collect { result ->
//                        when (result) {
//                            is ApiResult.Error -> Unit
//                            is ApiResult.Loading -> Unit // show progress if needed
//                            is ApiResult.Success -> updateUserDetails(result.data)
//                        }
//                    }
//                }
            }
        }
    }

    private fun updateUserDetails(user: User) {
        if (!user.name?.first.isNullOrEmpty()) {
            // TODO (1 point): Use Glide to load images after getting the data from endpoints mentioned in RemoteDataSource
            binding.mainImage.loadImageWithGlide(
                url = user.picture?.medium
            )
            binding.mainName.text = user.name?.first
            binding.mainEmail.text = user.email
        }
    }

    override fun onItemClick(user: User) {
        viewModel.onEvent(MainViewEvent.UserDetailsEvent(user))
    }
}
