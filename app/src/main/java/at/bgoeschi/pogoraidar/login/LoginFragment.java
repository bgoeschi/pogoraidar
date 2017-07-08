package at.bgoeschi.pogoraidar.login;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;

import at.bgoeschi.pogoraidar.R;
import at.bgoeschi.pogoraidar.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {

	private FragmentLoginBinding binding;
	private OnTwitterResultListener onTwitterResultListener;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);
		binding.fragmentLoginTwitterLogin.setCallback(new com.twitter.sdk.android.core.Callback<TwitterSession>() {
			@Override
			public void success(Result<TwitterSession> result) {
				if (onTwitterResultListener != null) {
					onTwitterResultListener.onTwitterLoginSuccessful();
				}
			}

			@Override
			public void failure(TwitterException exception) {
				// Nothing to do here, maybe show error in future
			}
		});
		return binding.getRoot();
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof OnTwitterResultListener) {
			this.onTwitterResultListener = (OnTwitterResultListener) context;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		binding.fragmentLoginTwitterLogin.onActivityResult(requestCode, resultCode, data);
	}

	public interface OnTwitterResultListener {
		void onTwitterLoginSuccessful();
	}
}
