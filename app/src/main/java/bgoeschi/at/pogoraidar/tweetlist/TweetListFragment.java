package bgoeschi.at.pogoraidar.tweetlist;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Search;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.params.Geocode;

import bgoeschi.at.pogoraidar.R;
import bgoeschi.at.pogoraidar.databinding.FragmentTweetListBinding;

public class TweetListFragment extends Fragment {

	FragmentTweetListBinding binding;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tweet_list, container, false);
		return binding.getRoot();
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		if (TwitterCore.getInstance().getSessionManager().getActiveSession() == null) {
			return;
		}

		TwitterApiClient client = TwitterCore.getInstance().getApiClient();
		client.getSearchService().tweets("#zib2", new Geocode(48.2111219, 16.372768, 10, Geocode.Distance.KILOMETERS), null, null,
				null, null, null, null, null, false).enqueue(new Callback<Search>() {
			@Override
			public void success(Result<Search> result) {
				for (Tweet tweet : result.data.tweets) {
					binding.fragmentTweetListText.append("--" + tweet.place+ "--" + tweet.text + "\n\n");
				}
			}

			@Override
			public void failure(TwitterException exception) {
				Log.e("TwitterException", exception.getMessage());
			}
		});
	}
}
