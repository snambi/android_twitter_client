package com.github.snambi.twitterclient.activities;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.activeandroid.util.Log;
import com.codepath.oauth.OAuthLoginActivity;
import com.github.snambi.twitterclient.R;
import com.github.snambi.twitterclient.TwitterApplication;
import com.github.snambi.twitterclient.clients.TwitterRestClient;
import com.github.snambi.twitterclient.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

public class LoginActivity extends OAuthLoginActivity<TwitterRestClient> {
	
	TwitterRestClient twitterClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		getActionBar().setTitle("Login");		
	}

	// Inflate the menu; this adds items to the action bar if it is present.
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	// OAuth authenticated successfully, launch primary authenticated activity
	// i.e Display application "homepage"
	@Override
	public void onLoginSuccess() {
		//Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show();
		
		Intent i = new Intent(this, TimelineActivity.class);
		startActivity(i);
		
		twitterClient = TwitterApplication.getRestClient();
		saveUserInfoInSharedPrefs();
	}

	private void saveUserInfoInSharedPrefs(){
		
		twitterClient.getMyInfo( new JsonHttpResponseHandler(){
			
			@Override
			public void onFailure(Throwable arg0, JSONObject arg1) {
				// TODO Auto-generated method stub
				super.onFailure(arg0, arg1);
			}
			
			@Override
			public void onSuccess(JSONObject jsonObject) {
				Log.d("debug", "User information " + jsonObject.toString());
				
				User user = User.fromJson(jsonObject);
				
				// save these info in private shared info
				SharedPreferences prefs = getSharedPreferences("com.github.snambi.twitterclient", Context.MODE_PRIVATE);
				
				prefs.edit().putString("user_name", user.getName()).apply();
				prefs.edit().putString("screen_name", user.getScreenName()).apply();
				prefs.edit().putString("image_profile_url", user.getProfileImageUrl()).apply();
				prefs.edit().putLong("id", user.getUid()).apply();
			}
		});
	}

	// OAuth authentication flow failed, handle the error
	// i.e Display an error dialog or toast
	@Override
	public void onLoginFailure(Exception e) {
		e.printStackTrace();
	}

	// Click handler method for the button used to start OAuth flow
	// Uses the client to initiate OAuth authorization
	// This should be tied to a button used to login
	public void loginToRest(View view) {
		getClient().connect();
	}

}
