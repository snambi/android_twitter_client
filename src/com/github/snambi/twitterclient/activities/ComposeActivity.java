package com.github.snambi.twitterclient.activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.snambi.twitterclient.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ComposeActivity extends Activity {
	
	private TextView tvScreenName = null;
	private ImageView imgProfileImageUrl = null;

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
		
		tvScreenName.setText("@" + screenName);
		
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(profileImageUrl, imgProfileImageUrl);
	}
}
