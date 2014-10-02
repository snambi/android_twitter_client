package com.github.snambi.twitterclient.activities;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.util.Log;
import com.github.snambi.twitterclient.R;
import com.github.snambi.twitterclient.TwitterApplication;
import com.github.snambi.twitterclient.adapters.TwitterArrayAdapter;
import com.github.snambi.twitterclient.clients.TwitterRestClient;
import com.github.snambi.twitterclient.clients.TwitterRestClient.TimelineCounter;
import com.github.snambi.twitterclient.db.TweetDbHelper;
import com.github.snambi.twitterclient.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TimelineActivity extends Activity {

	TwitterRestClient twitterClient;
	ListView lvTweets;
	List<Tweet> tweets = new ArrayList<Tweet>();
	//ArrayAdapter<Tweet> aTweets = null;
	TwitterArrayAdapter aTweets=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		
		
		lvTweets = (ListView) findViewById(R.id.lvTweets);
		//aTweets = new ArrayAdapter<Tweet>(this, android.R.layout.simple_list_item_1, tweets);
		aTweets = new TwitterArrayAdapter(this, tweets);
		lvTweets.setAdapter(aTweets);
		
		twitterClient = TwitterApplication.getRestClient();
		
		populateTimeline();
		
//		// also get the user's 'screen_name' and 'profile_image_url'
//		saveUserInfoInSharedPrefs();
				
		// attach the endless scrollview listener to the listview
		lvTweets.setOnScrollListener( new EndlessScrollListener() {
			
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				populateTimeline();
			}
		});

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

	private void populateTimeline() {
		twitterClient.getHomeTimeLine( new JsonHttpResponseHandler(){
			
			@Override
			public void onFailure(Throwable t, String s) {
				Log.d( "debug", s, t);
			}
			
			@Override
			public void onSuccess(JSONArray jsonArray) {
				Log.d("debug", jsonArray.toString() );
				
				List<Tweet> tweets = Tweet.fromJSONArray(jsonArray);
				
				// figure out the sinceId and maxId based on the received the tweets.
				TimelineCounter counter = TimelineCounter.getInstance();
				
				
				// these values are used for next iteration
				counter.setSinceIdMaxIdFrom(tweets);
				TweetDbHelper.saveWhenNotPresent(tweets);
				aTweets.addAll( tweets );
			}
		});		
	}
	
	private void saveTweetsInDB( List<Tweet> tweets){
		if( tweets != null && tweets.size()>0 ){
			try{
				ActiveAndroid.beginTransaction();
				for( Tweet t : tweets ){
					t.save();
				}
				ActiveAndroid.setTransactionSuccessful();
			}finally{
				ActiveAndroid.endTransaction();
			}
		}
	}
	
//	private void saveUserInfoInSharedPrefs(){
//		
//		twitterClient.saveUserInfo( new JsonHttpResponseHandler(){
//			
//			@Override
//			public void onFailure(Throwable arg0, JSONObject arg1) {
//				// TODO Auto-generated method stub
//				super.onFailure(arg0, arg1);
//			}
//			
//			@Override
//			public void onSuccess(JSONObject jsonObject) {
//				Log.d("debug", "User information " + jsonObject.toString());
//				
//				User user = User.fromJson(jsonObject);
//				
//				// save these info in private shared info
//				SharedPreferences prefs = getSharedPreferences("com.github.snambi.twitterclient", Context.MODE_PRIVATE);
//				
//				prefs.edit().putString("user_name", user.getName()).apply();
//				prefs.edit().putString("screen_name", user.getScreenName()).apply();
//				prefs.edit().putString("image_profile_url", user.getProfileImageUrl()).apply();
//				prefs.edit().putLong("id", user.getUid()).apply();
//			}
//		});
//	}
	
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
				tweets.add(0, tweet);
				aTweets.notifyDataSetChanged();
			}
		}
	}
}
