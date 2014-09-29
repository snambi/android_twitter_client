package com.github.snambi.twitterclient.activities;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

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
