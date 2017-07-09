package at.bgoeschi.pogoraidar.tweetlist.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import at.bgoeschi.pogoraidar.PogoRaidarTweet;
import at.bgoeschi.pogoraidar.R;
import at.bgoeschi.pogoraidar.databinding.FragmentTweetListBinding;
import at.bgoeschi.pogoraidar.tweetlist.TweetListContract;
import at.bgoeschi.pogoraidar.tweetlist.presenter.TwitterListPresenter;

public class TweetListFragment extends Fragment implements TweetListContract.View, View.OnClickListener {

    private FragmentTweetListBinding binding;
    private TweetListContract.Presenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tweet_list, container, false);
        presenter = new TwitterListPresenter(this);
        presenter.onUpdateCalled();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.sendMessage.setOnClickListener(this);
    }

    @Override
    public void publishPogoRaidarTweets(List<PogoRaidarTweet> tweets) {
        for (PogoRaidarTweet tweet : tweets) {
            binding.fragmentTweetListText.append(tweet + "\n\n");
        }
    }

    @Override
    public void showLocationError() {
        Toast.makeText(getContext(), R.string.no_known_position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showTweetSent() {
        binding.messageInput.getText().clear();
        Toast.makeText(getContext(), getString(R.string.message_sent_successfully), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoginScreen() {
        // restart activity here to act accordingly
        getActivity().recreate();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == binding.sendMessage.getId()) {
            presenter.onNewMessageEntered(binding.messageInput.getText().toString());
        }
    }
}
