package bgoeschi.at.pogoraidar;

import android.location.Location;

import com.twitter.sdk.android.core.TwitterCore;

/**
 * Created by Thomas on 05.07.2017.
 */

public class User {

	private static User instance;

	private Location currentLocation;
	private String twitterUserName;

	private User() {

	}

	public static User get() {
		if (instance == null) {
			instance = new User();
		}
		return instance;
	}

	public Location getCurrentLocation() {
		return currentLocation;
	}

	public void setCurrentLocation(Location currentLocation) {
		this.currentLocation = currentLocation;
	}

	public String getTwitterUserName() {
		return TwitterCore.getInstance().getSessionManager().getActiveSession().getUserName();
	}
}
