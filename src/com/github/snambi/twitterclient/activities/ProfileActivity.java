package com.github.snambi.twitterclient.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.github.snambi.twitterclient.R;
import com.github.snambi.twitterclient.TwitterApplication;
import com.github.snambi.twitterclient.clients.TwitterRestClient;
import com.github.snambi.twitterclient.fragemets.ProfileHeaderFragment;

public class ProfileActivity extends FragmentActivity {
	
	protected TwitterRestClient restClient;
	
	/**
	 * profile activity is made up of two fragments
	 * one frgament is statically loaded
	 * other fragment is dynamically loaded
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
			
		String screenName = getIntent().getStringExtra("screenname");
		
		restClient = TwitterApplication.getRestClient();
		
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.flProfileHeader, new ProfileHeaderFragment( restClient, screenName ) , "profile-fragment");
		ft.commit();
	}
}
