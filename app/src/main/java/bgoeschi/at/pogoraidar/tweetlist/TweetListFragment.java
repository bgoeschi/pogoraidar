package bgoeschi.at.pogoraidar.tweetlist;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Search;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.params.Geocode;

import bgoeschi.at.pogoraidar.R;
import bgoeschi.at.pogoraidar.User;
import bgoeschi.at.pogoraidar.databinding.FragmentTweetListBinding;
import retrofit2.Call;
import retrofit2.Response;

public class TweetListFragment extends Fragment {

    private FragmentTweetListBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tweet_list, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasSession()) {
                    postRaidarTweet();
                }
            }
        });
        if (hasSession()) {
            loadTweets();
        }
    }

    private void postRaidarTweet() {
        double latitude = User.get().getCurrentLocation().getLatitude();
        double longitude = User.get().getCurrentLocation().getLongitude();
        TwitterCore.getInstance().getApiClient()
                .getStatusesService().update(binding.messageInput.getText().toString()+ " #pogoraidar",
                null, false, latitude, longitude, null, true, false, null).enqueue(new retrofit2.Callback<Tweet>() {
            @Override
            public void onResponse(Call<Tweet> call, Response<Tweet> response) {
                binding.messageInput.getText().clear();
                Toast.makeText(getContext(), getString(R.string.message_sent_successfully), Toast.LENGTH_SHORT).show();
                loadTweets();
                Log.d("Success", response.toString());
            }

            @Override
            public void onFailure(Call<Tweet> call, Throwable t) {
                Log.e("Failure", t.getMessage());
            }
        });
    }

    private void loadTweets() {
        double latitude = User.get().getCurrentLocation().getLatitude();
        double longitude = User.get().getCurrentLocation().getLongitude();
		TwitterApiClient client = TwitterCore.getInstance().getApiClient();
		client.getSearchService().tweets(null, new Geocode(latitude, longitude, 10, Geocode.Distance.KILOMETERS), null, null, null,
				null, null, null, null, false).enqueue(new Callback<Search>() {
			@Override
			public void success(Result<Search> result) {
				for (Tweet tweet : result.data.tweets) {
					binding.fragmentTweetListText.append("--" + (tweet.place != null ? tweet.place.fullName : "") + "--" + tweet.text +
							"\n\n");
				}
			}

            @Override
            public void failure(TwitterException exception) {
                Log.e("TwitterException", exception.getMessage());
            }
        });
    }

    private boolean hasSession() {
        return TwitterCore.getInstance().getSessionManager().getActiveSession() != null;
    }
}
