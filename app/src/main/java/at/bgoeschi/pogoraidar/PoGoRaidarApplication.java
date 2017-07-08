package at.bgoeschi.pogoraidar;

import android.app.Application;

import com.twitter.sdk.android.core.Twitter;

/**
 * Created by Thomas on 05.07.2017.
 */

public class PoGoRaidarApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		Twitter.initialize(this);
	}
}
