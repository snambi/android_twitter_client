package com.github.snambi.twitterclient.activities;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.github.snambi.twitterclient.R;
import com.github.snambi.twitterclient.TwitterApplication;
import com.github.snambi.twitterclient.clients.TwitterRestClient;
import com.github.snambi.twitterclient.fragemets.HomeTimelineFragment;
import com.github.snambi.twitterclient.fragemets.MentionsTimelineFragment;
import com.github.snambi.twitterclient.fragemets.TwitterListFragment;
import com.github.snambi.twitterclient.listeners.FragmentTabListener;
import com.github.snambi.twitterclient.models.Tweet;

public class TimelineActivity extends FragmentActivity {

	TwitterRestClient twitterClient;
	TwitterListFragment listFragment = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		
		setupTabs();
		
		twitterClient = TwitterApplication.getRestClient();
						
		SharedPreferences prefs = getSharedPreferences("com.github.snambi.twitterclient", Context.MODE_PRIVATE);
		
		// read screen_name and profile_image_url
		String screenName = prefs.getString("screen_name", null);
		getActionBar().setTitle(screenName);
		
		prefs.registerOnSharedPreferenceChangeListener(new OnSharedPreferenceChangeListener(){

			@Override
			public void onSharedPreferenceChanged(
					SharedPreferences sharedPreferences, String key) {
				if( key.equals("screen_name") ){
					String title= sharedPreferences.getString(key, null);
					getActionBar().setTitle(title);
				}
			}
		});
	}
	
	private void setupTabs() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);

		Tab tab1 = actionBar
			.newTab()
			.setText("Home")
			.setIcon(R.drawable.ic_home)
			.setTag("HomeTimelineFragment")
			.setTabListener(
				new FragmentTabListener<HomeTimelineFragment>(R.id.flContainer, this, "first",
								HomeTimelineFragment.class));

		actionBar.addTab(tab1);
		actionBar.selectTab(tab1);

		Tab tab2 = actionBar
			.newTab()
			.setText("Mentions")
			.setIcon(R.drawable.ic_mentions)
			.setTag("MentionsTimelineFragment")
			.setTabListener(
			    new FragmentTabListener<MentionsTimelineFragment>(R.id.flContainer, this, "second",
			    		MentionsTimelineFragment.class));

		actionBar.addTab(tab2);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.compose_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		boolean result=false;
		
		switch( item.getItemId() ){
		case  R.id.compose_menu_item:
			
			Intent intent = new Intent(this, ComposeActivity.class);
			result=true;
			startActivityForResult(intent, 700);
			break;
		default:
			break;
		}
		return result;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		// tweet activity
		if( requestCode ==700 ){
			if( resultCode== RESULT_CANCELED ){
				// no op
			}
			if( resultCode == RESULT_OK ){
				Tweet tweet = (Tweet) data.getParcelableExtra("tweet");
				// insert the tweet to the top of the list
//				tweets.add(0, tweet);
//				aTweets.notifyDataSetChanged();
				listFragment.addTweetAtPosition(tweet, 0);
			}
		}
	}
}
