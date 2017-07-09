package at.bgoeschi.pogoraidar.tweetlist.model;

import android.location.Location;
import android.util.Log;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Search;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.params.Geocode;

import java.util.ArrayList;
import java.util.List;

import at.bgoeschi.pogoraidar.PogoRaidarTweet;
import at.bgoeschi.pogoraidar.User;
import at.bgoeschi.pogoraidar.tweetlist.TweetListContract;
import retrofit2.Call;
import retrofit2.Response;

public class TwitterInteractorImpl implements TweetListContract.TwitterInteractor {

	@Override
	public void sendTweet(String message, final SendPogoRaidarTweetCallback callback) {
		if (User.get().getCurrentLocation() == null) {
			if (callback != null) {
				callback.onNoLocationError();
			}
			return;
		}
		Location location = User.get().getCurrentLocation();
		TwitterCore.getInstance().getApiClient().getStatusesService().update("@pogoraidar " + message, null, false, location.getLatitude(),
				location.getLongitude(), null, true, false, null).enqueue(new retrofit2.Callback<Tweet>() {
			@Override
			public void onResponse(Call<Tweet> call, Response<Tweet> response) {
				Log.d("Success", response.toString());
				if (callback != null) {
					callback.onSuccess();
				}
			}

			@Override
			public void onFailure(Call<Tweet> call, Throwable t) {
				Log.e("Failure", t.getMessage());
				if (callback != null) {
					callback.onFailure(new Exception(t));
				}
			}
		});
	}

	@Override
	public void getPogoRaidarTweets(final LoadPogoRaidarTweetsCallback callback) {
		Location location = User.get().getCurrentLocation();
		if (location == null) {
			if (callback != null) {
				callback.onNoLocationError();
			}
			return;
		}

		TwitterApiClient client = TwitterCore.getInstance().getApiClient();
		client.getSearchService().tweets(null, new Geocode(location.getLatitude(), location.getLongitude(), 1000, Geocode.Distance
				.KILOMETERS), null, null, null, null, null, null, null, false).enqueue(new Callback<Search>() {
			@Override
			public void success(Result<Search> result) {
				List<PogoRaidarTweet> pogoRaidarTweets = new ArrayList<>();
				for (Tweet tweet : result.data.tweets) {
					String user = tweet.user != null ? tweet.user.name : "";
					String place = tweet.place != null ? tweet.place.name : "";
					pogoRaidarTweets.add(new PogoRaidarTweet(user, place, tweet.text));
				}
				if (callback != null) {
					callback.onSuccess(pogoRaidarTweets);
				}
			}

			@Override
			public void failure(TwitterException exception) {
				Log.e("TwitterException", exception.getMessage());
				callback.onFailure(exception);
			}
		});
	}

	@Override
	public boolean hasActiveTwitterSession() {
		return TwitterCore.getInstance().getSessionManager().getActiveSession() != null;
	}
}
