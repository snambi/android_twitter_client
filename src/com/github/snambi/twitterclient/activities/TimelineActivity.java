package com.github.snambi.twitterclient.activities;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.activeandroid.util.Log;
import com.github.snambi.twitterclient.R;
import com.github.snambi.twitterclient.TwitterApplication;
import com.github.snambi.twitterclient.adapters.TwitterArrayAdapter;
import com.github.snambi.twitterclient.clients.TwitterRestClient;
import com.github.snambi.twitterclient.clients.TwitterRestClient.TimelineCounter;
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
		
		// attach the endless scrollview listener to the listview
		lvTweets.setOnScrollListener( new EndlessScrollListener() {
			
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				populateTimeline();
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.compose_menu, menu);
		
//		MenuItem item = menu.findItem(R.id.compose_menu_item);
//		item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){
//
//			@Override
//			public boolean onMenuItemClick(MenuItem item) {
//				Toast.makeText(getApplicationContext(), "clicked", Toast.LENGTH_SHORT).show();
//				return true;
//			}
//			
//		});
		
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
				
				aTweets.addAll( tweets );
			}
		});		
	}
}
