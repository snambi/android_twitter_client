package com.github.snambi.twitterclient.fragemets;

import java.util.List;

import org.json.JSONArray;

import com.activeandroid.util.Log;
import com.github.snambi.twitterclient.activities.EndlessScrollListener;
import com.github.snambi.twitterclient.clients.TwitterRestClient.TimelineCounter;
import com.github.snambi.twitterclient.db.TweetDbHelper;
import com.github.snambi.twitterclient.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HomeTimelineFragment extends TwitterListFragment {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		populateTimeline();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = super.onCreateView(inflater, container, savedInstanceState);

		// attach the endless scrollview listener to the listview
		lvTweets.setOnScrollListener( new EndlessScrollListener() {
			
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				populateTimeline();
			}
		});

		return view;
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
				
				//aTweets.addAll( tweets );
				addTweets(tweets);
			}
		});		
	}

}
