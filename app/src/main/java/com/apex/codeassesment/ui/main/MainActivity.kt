package com.apex.codeassesment.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.apex.codeassesment.R
import com.apex.codeassesment.data.UserRepository
import com.apex.codeassesment.data.model.User
import com.apex.codeassesment.di.MainComponent
import com.apex.codeassesment.ui.details.DetailsActivity
import javax.inject.Inject

// TODO (5 points): Move calls to repository to Presenter or ViewModel.
// TODO (5 points): Use combination of sealed/Dataclasses for exposing the data required by the view from viewModel .
// TODO (3 points): Add tests for viewmodel or presenter.
// TODO (1 point): Add content description to images
// TODO (3 points): Add tests
// TODO (Optional Bonus 10 points): Make a copy of this activity with different name and convert the current layout it is using in
//  Jetpack Compose.
class MainActivity : AppCompatActivity() {

  // TODO (2 points): Convert to view binding
  private var userImageView: ImageView? = null
  private var userNameTextView: TextView? = null
  private var userEmailTextView: TextView? = null
  private var seeDetailsButton: Button? = null
  private var refreshUserButton: Button? = null
  private var showUserListButton: Button? = null
  private var userListView: ListView? = null

  @Inject lateinit var userRepository: UserRepository

  private var randomUser: User = User()
    set(value) {
      // TODO (1 point): Use Glide to load images after getting the data from endpoints mentioned in RemoteDataSource
      // userImageView.setImageBitmap(refreshedUser.image)
      userNameTextView!!.text = value.name!!.first
      userEmailTextView!!.text = value.email
      field = value
    }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    sharedContext = this

    (applicationContext as MainComponent.Injector).mainComponent.inject(this)

    val arrayAdapter = ArrayAdapter<User>(this, android.R.layout.simple_list_item_1)

    userImageView = findViewById(R.id.main_image)
    userNameTextView = findViewById(R.id.main_name)
    userEmailTextView = findViewById(R.id.main_email)
    seeDetailsButton = findViewById(R.id.main_see_details_button)
    refreshUserButton = findViewById(R.id.main_refresh_button)
    showUserListButton = findViewById(R.id.main_user_list_button)
    userListView = findViewById(R.id.main_user_list)
    userListView!!.adapter = arrayAdapter
    userListView?.setOnItemClickListener { parent, _, position, _ -> navigateDetails(parent.getItemAtPosition(position) as User) }

    randomUser = userRepository.getSavedUser()

    seeDetailsButton!!.setOnClickListener { navigateDetails(randomUser) }

    refreshUserButton!!.setOnClickListener { randomUser = userRepository.getUser(true) }

    showUserListButton!!.setOnClickListener {
      val users = userRepository.getUsers()
      arrayAdapter.clear()
      arrayAdapter.addAll(users)
    }
  }

  // TODO (2 points): Convert to extenstion function.
  private fun navigateDetails(user: User) {
    val putExtra = Intent(this, DetailsActivity::class.java).putExtra("saved-user-key", user)
    startActivity(putExtra)
  }

  companion object {
    var sharedContext: Context? = null
  }
}
