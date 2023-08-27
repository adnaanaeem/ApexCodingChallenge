package com.apex.codeassesment.ui.details;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.apex.codeassesment.R;
import com.apex.codeassesment.data.model.Coordinates;
import com.apex.codeassesment.data.model.Name;
import com.apex.codeassesment.data.model.User;
import com.apex.codeassesment.databinding.ActivityDetailsBinding;
import com.apex.codeassesment.ui.location.LocationActivity;

// TODO (3 points): Convert to Kotlin
// TODO (3 points): Remove bugs or crashes if any
// TODO (1 point): Add content description to images
// TODO (2 points): Add tests
public class DetailsActivity extends AppCompatActivity {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    final ActivityDetailsBinding binding = ActivityDetailsBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    final User user = getIntent().getParcelableExtra("saved-user-key");

    // TODO (1 point): Use Glide to load images
//    binding.detailsImage = user.getPicture().getLarge();
    final Name name = user.getName();
    binding.detailsName.setText(getString(R.string.details_name, name.getFirst(), name.getLast()));
    binding.detailsEmail.setText(getString(R.string.details_email, user.getGender()));
    binding.detailsAge.setText(user.getDob().getAge());
    final Coordinates coordinates = user.getLocation().getCoordinates();
    binding.detailsLocation.setText(getString(R.string.details_location, coordinates.getLatitude(), coordinates.getLongitude()));
    binding.detailsLocationButton.setOnClickListener((x) -> navigateLocation(coordinates));
  }

  private void navigateLocation(@NonNull final Coordinates coordinates) {
    final Intent intent = new Intent(this, LocationActivity.class)
      .putExtra("user-latitude-key", coordinates.getLatitude())
      .putExtra("user-longitude-key", coordinates.getLongitude());
    startActivity(intent);
  }
}
