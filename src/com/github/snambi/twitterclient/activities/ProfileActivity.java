package com.github.snambi.twitterclient.activities;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.snambi.twitterclient.R;
import com.github.snambi.twitterclient.TwitterApplication;
import com.github.snambi.twitterclient.clients.TwitterRestClient;
import com.github.snambi.twitterclient.fragemets.ProfileHeaderFragment;
import com.github.snambi.twitterclient.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ProfileActivity extends FragmentActivity {
	
	protected TwitterRestClient restClient;
	
	TextView tvProfileUserName;
	TextView tvProfileTagLine;
	ImageView ivProfileUserImage;
	TextView tvProfileFollowers;
	TextView tvProfileFollowing;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
			
		restClient = TwitterApplication.getRestClient();
		
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.flProfileHeader, new ProfileHeaderFragment( restClient ) , "profile-fragment");
		ft.commit();
		
		//loadDetails();
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
				
				tvProfileUserName.setText(user.getName());
				tvProfileTagLine.setText(user.getTag());
				tvProfileFollowers.setText(user.getFollowers() + " followers");
				tvProfileFollowing.setText(user.getFollowing() + " follwiing");
				ImageLoader imageLoader = ImageLoader.getInstance();
				imageLoader.displayImage(user.getProfileImageUrl(), ivProfileUserImage );
			}
		});
	}
}
