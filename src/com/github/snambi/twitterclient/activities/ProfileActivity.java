package com.github.snambi.twitterclient.activities;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.github.snambi.twitterclient.R;
import com.github.snambi.twitterclient.TwitterApplication;
import com.github.snambi.twitterclient.clients.TwitterRestClient;
import com.github.snambi.twitterclient.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

public class ProfileActivity extends FragmentActivity {
	
	protected TwitterRestClient restClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
	
		restClient = TwitterApplication.getRestClient();
		loadDetails();
	}

	private void loadDetails() {
		
		restClient.getMyInfo( new JsonHttpResponseHandler(){
			
			@Override
			public void onFailure(Throwable arg0, JSONObject arg1) {
				super.onFailure(arg0, arg1);
			}
			
			@Override
			public void onSuccess(int arg0, JSONObject json) {
				User user = User.fromJson(json);
				getActionBar().setTitle( "@" + user.getScreenName() );
			}
		});
	}
}
