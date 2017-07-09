package at.bgoeschi.pogoraidar.tweetlist.presenter;

import java.util.List;

import at.bgoeschi.pogoraidar.PogoRaidarTweet;
import at.bgoeschi.pogoraidar.tweetlist.TweetListContract;
import at.bgoeschi.pogoraidar.tweetlist.model.TwitterInteractorImpl;

public class TwitterListPresenter implements TweetListContract.Presenter {

	private TweetListContract.View view;
	private TweetListContract.TwitterInteractor interactor;

	public TwitterListPresenter(TweetListContract.View view) {
		this.view = view;
		this.interactor = new TwitterInteractorImpl();
	}

	@Override
	public void onNewMessageEntered(String message) {
		if (checkTwitterSession()) {
			return;
		}

		interactor.sendTweet(message, new TweetListContract.TwitterInteractor.SendPogoRaidarTweetCallback() {
			@Override
			public void onSuccess() {
				view.showTweetSent();
				onUpdateCalled();
			}

			@Override
			public void onFailure(Exception e) {
				// TODO add error handling here
			}

			@Override
			public void onNoLocationError() {
				view.showLocationError();
			}
		});
	}

	@Override
	public void onUpdateCalled() {
		if (checkTwitterSession()) {
			return;
		}
		interactor.getPogoRaidarTweets(new TweetListContract.TwitterInteractor.LoadPogoRaidarTweetsCallback() {
			@Override
			public void onSuccess(List<PogoRaidarTweet> tweets) {
				view.publishPogoRaidarTweets(tweets);
			}

			@Override
			public void onFailure(Exception e) {
				// TODO add error handling here
			}

			@Override
			public void onNoLocationError() {
				view.showLocationError();
			}
		});
	}

	private boolean checkTwitterSession() {
		if (!interactor.hasActiveTwitterSession()) {
			view.showLoginScreen();
			return true;
		}
		return false;
	}
}
