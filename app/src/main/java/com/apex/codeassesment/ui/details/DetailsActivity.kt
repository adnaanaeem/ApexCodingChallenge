package com.apex.codeassesment.ui.details

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.apex.codeassesment.R
import com.apex.codeassesment.data.model.Coordinates
import com.apex.codeassesment.data.model.User
import com.apex.codeassesment.databinding.ActivityDetailsBinding
import com.apex.codeassesment.ui.location.LocationActivity
import com.apex.codeassesment.utils.Constants.SAVED_USER_KEY
import com.apex.codeassesment.utils.Constants.USER_LATITUDE_KEY
import com.apex.codeassesment.utils.Constants.USER_LONGITUDE_KEY
import com.apex.codeassesment.utils.loadImageWithGlide

// TODO (3 points): Convert to Kotlin
// TODO (3 points): Remove bugs or crashes if any
// TODO (1 point): Add content description to images
// TODO (2 points): Add tests
@Suppress("DEPRECATION")
class DetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(SAVED_USER_KEY, User::class.java)
        } else {
            intent.getParcelableExtra(SAVED_USER_KEY)
        }

        if (user != null) {
            with(binding) {
                // TODO (1 point): Use Glide to load images
                detailsImage.loadImageWithGlide(user.picture?.large)
                detailsImage.contentDescription = user.name?.first

                val name = user.name
                detailsName.text = getString(R.string.details_name, name?.first, name?.last)
                detailsEmail.text = getString(R.string.details_email, user.gender)
                detailsAge.text = user.dob?.age.toString()
                val coordinates = user.location?.coordinates
                detailsLocation.text = getString(
                    R.string.details_location,
                    coordinates?.latitude,
                    coordinates?.longitude
                )
                detailsLocationButton.setOnClickListener { navigateLocation(coordinates) }
            }
        }

    }

    private fun navigateLocation(coordinates: Coordinates?) {
        val intent = Intent(this, LocationActivity::class.java)
            .putExtra(USER_LATITUDE_KEY, coordinates?.latitude)
            .putExtra(USER_LONGITUDE_KEY, coordinates?.longitude)
        startActivity(intent)
    }
}
