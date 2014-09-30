package com.github.snambi.twitterclient.activities;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.snambi.twitterclient.R;
import com.github.snambi.twitterclient.TwitterApplication;
import com.github.snambi.twitterclient.clients.TwitterRestClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ComposeActivity extends Activity {
	
	private TextView tvScreenName = null;
	private ImageView imgProfileImageUrl = null;
	private EditText etTweetText = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose);
		
		SharedPreferences prefs = getSharedPreferences("com.github.snambi.twitterclient", Context.MODE_PRIVATE);
		
		// read screen_name and profile_image_url
		String screenName = prefs.getString("screen_name", null);
		String profileImageUrl = prefs.getString("image_profile_url", null);
		
		tvScreenName = (TextView) findViewById(R.id.tvScreenName2);
		imgProfileImageUrl = (ImageView) findViewById(R.id.imgProfileImage2);
		etTweetText = (EditText) findViewById(R.id.etTweetBox);
		
		tvScreenName.setText("@" + screenName);
		
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(profileImageUrl, imgProfileImageUrl);
		
		// attach a listener that warns the user a
	}
	
	public void onCancelClick( View view){
		// go back to the timeline activity
		Intent intent = new Intent();
		setResult(RESULT_CANCELED, intent);
		finish();
	}
	
	public void onTweetClick( View view){
		
		TwitterRestClient restClient =TwitterApplication.getRestClient();
		
		String tweet = etTweetText.getText().toString();
		
		restClient.createTweet(tweet, new JsonHttpResponseHandler(){
			@Override
			public void onFailure(Throwable arg0, JSONObject arg1) {
				// TODO Auto-generated method stub
				super.onFailure(arg0, arg1);
			}
			
			@Override
			public void onSuccess(JSONObject jsonObject) {
				
			}
		});
	}
}
