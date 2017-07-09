package at.bgoeschi.pogoraidar.tweetlist;

import java.util.List;

import at.bgoeschi.pogoraidar.PogoRaidarTweet;

public interface TweetListContract {

	interface View {
		void publishPogoRaidarTweets(List<PogoRaidarTweet> tweets);
		void showLocationError();
		void showTweetSent();
		void showLoginScreen();
	}

	interface Presenter {
		void onNewMessageEntered(String message);
		void onUpdateCalled();
	}

	interface TwitterInteractor {
		void sendTweet(String message, SendPogoRaidarTweetCallback callback);
		void getPogoRaidarTweets(LoadPogoRaidarTweetsCallback callback);
		boolean hasActiveTwitterSession();

		interface LoadPogoRaidarTweetsCallback {
			void onSuccess(List<PogoRaidarTweet> tweets);
			void onFailure(Exception e);
			void onNoLocationError();
		}

		interface SendPogoRaidarTweetCallback {
			void onSuccess();
			void onFailure(Exception e);
			void onNoLocationError();
		}
	}

}
