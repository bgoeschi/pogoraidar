package bgoeschi.at.pogoraidar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.twitter.sdk.android.core.TwitterCore;

import bgoeschi.at.pogoraidar.login.LoginFragment;
import bgoeschi.at.pogoraidar.tweetlist.TweetListFragment;

public class MainActivity extends AppCompatActivity implements LoginFragment.OnTwitterResultListener {

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
}
