package at.bgoeschi.pogoraidar;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.twitter.sdk.android.core.TwitterCore;

import at.bgoeschi.pogoraidar.login.LoginFragment;
import at.bgoeschi.pogoraidar.tweetlist.TweetListFragment;

public class MainActivity extends AppCompatActivity implements LoginFragment.OnTwitterResultListener {

	private static final int PERMISSIONS_LOCATION_REQUEST = 22;
	private static final int LOCATION_SETTINGS_REQUEST = 21;
	private final String TAG_CURRENT_FRAGMENT = "currentFragment";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		getLocation();
	}

	@Override
	public void onTwitterLoginSuccessful() {
		getSupportFragmentManager().beginTransaction().replace(R.id.content, new TweetListFragment(), TAG_CURRENT_FRAGMENT).commit();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == LOCATION_SETTINGS_REQUEST) {
			getLocation();
		} else {
			Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_CURRENT_FRAGMENT);
			if (fragment != null) {
				fragment.onActivityResult(requestCode, resultCode, data);
			}
		}
	}

	private void getLocation() {
		// Get the location manager
		final LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

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
			updateUi();
		} else {
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, new LocationListener() {
				@Override
				public void onLocationChanged(Location location) {
					Log.d("LocationManager", "location received");
					User.get().setCurrentLocation(location);
					updateUi();
					locationManager.removeUpdates(this);
				}

				@Override
				public void onStatusChanged(String s, int i, Bundle bundle) {
					Log.d("LocationManager", "status changed");
					locationManager.removeUpdates(this);
				}

				@Override
				public void onProviderEnabled(String s) {
					Log.d("LocationManager", "provider enabled");
					locationManager.removeUpdates(this);
				}

				@Override
				public void onProviderDisabled(String s) {
					Log.d("LocationManager", "provider disabled");
					locationManager.removeUpdates(this);
					if ("network".equals(s)) {
						final Snackbar snackbar = Snackbar.make(findViewById(R.id.content), R.string.network_provider_disabled, Snackbar
								.LENGTH_INDEFINITE);
						snackbar.setAction(R.string.settings, new View.OnClickListener() {
							@Override
							public void onClick(View view) {
								startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), LOCATION_SETTINGS_REQUEST);
								snackbar.dismiss();
							}
						});
						snackbar.show();
					}
				}
			}, null);
		}
	}

	private void updateUi() {
		FragmentManager manager = getSupportFragmentManager();
		if (TwitterCore.getInstance().getSessionManager().getActiveSession() == null) {
			manager.beginTransaction().replace(R.id.content, new LoginFragment(), TAG_CURRENT_FRAGMENT).commit();
		} else {
			manager.beginTransaction().replace(R.id.content, new TweetListFragment(), TAG_CURRENT_FRAGMENT).commit();
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
