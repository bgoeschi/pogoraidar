package bgoeschi.at.pogoraidar;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.twitter.sdk.android.core.TwitterCore;

import bgoeschi.at.pogoraidar.login.LoginFragment;
import bgoeschi.at.pogoraidar.tweetlist.TweetListFragment;

public class MainActivity extends AppCompatActivity implements LoginFragment.OnTwitterResultListener {

	private static final int PERMISSIONS_LOCATION_REQUEST = 22;
	private final String TAG_CURRENT_FRAGMENT = "currentFragment";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		FragmentManager manager = getSupportFragmentManager();

		if (TwitterCore.getInstance().getSessionManager().getActiveSession() == null) {
			manager.beginTransaction().replace(R.id.content, new LoginFragment(), TAG_CURRENT_FRAGMENT).commit();
		} else {
			manager.beginTransaction().replace(R.id.content, new TweetListFragment(), TAG_CURRENT_FRAGMENT).commit();
		}
		getLocation();

	}

	@Override
	public void onTwitterLoginSuccessful() {
		getSupportFragmentManager().beginTransaction().replace(R.id.content, new TweetListFragment(), TAG_CURRENT_FRAGMENT).commit();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		getSupportFragmentManager().findFragmentByTag(TAG_CURRENT_FRAGMENT).onActivityResult(requestCode, resultCode, data);
	}

	private void getLocation() {
		// Get the location manager
		LocationManager locationManager = (LocationManager)
				getSystemService(LOCATION_SERVICE);

		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			// Here, thisActivity is the current activity
			if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
						PERMISSIONS_LOCATION_REQUEST);
			}
			return;
		}

		Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

		if (location != null) {
			User.get().setCurrentLocation(location);
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == PERMISSIONS_LOCATION_REQUEST && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
			getLocation();
		}
	}
}
