package com.github.snambi.twitterclient.fragemets;

import java.util.List;

import org.json.JSONArray;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.activeandroid.util.Log;
import com.github.snambi.twitterclient.clients.TwitterRestClient.TweetsCounter;
import com.github.snambi.twitterclient.db.TweetDbHelper;
import com.github.snambi.twitterclient.listeners.EndlessScrollListener;
import com.github.snambi.twitterclient.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class UserTimelineFragment extends TwitterListFragment {
	public static final String SCREEN_NAME="screen-name";

	protected TweetsCounter counter = new TweetsCounter();
	protected String screenName=null;
	
	public static UserTimelineFragment newInstance(String screenName ){
		UserTimelineFragment fragment = new UserTimelineFragment();
		if( screenName != null && !screenName.trim().equals("")){
			Bundle data = new Bundle();
			data.putString( SCREEN_NAME, screenName);
			fragment.setArguments(data);
		}
		return fragment;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
	
	@Override
	public void onCreate(Bundle args) {
		super.onCreate(args);
		
		if(  getArguments() != null &&
				getArguments().getString(SCREEN_NAME) != null && 
				!getArguments().getString(SCREEN_NAME).trim().equals("") ){
			screenName = getArguments().getString(SCREEN_NAME).trim();
		}
		
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
		client.getUserTimeline( screenName, counter, new JsonHttpResponseHandler(){
			
			@Override
			public void onFailure(Throwable t, String s) {
				Log.d( "debug", s, t);
			}
			
			@Override
			public void onSuccess(JSONArray jsonArray) {
				Log.d("debug", jsonArray.toString() );
				
				List<Tweet> tweets = Tweet.fromJSONArray(jsonArray);
								
				// these values are used for next iteration
				counter.setSinceIdMaxIdFrom(tweets);
				TweetDbHelper.saveWhenNotPresent(tweets);
				
				//aTweets.addAll( tweets );
				addTweets(tweets);
			}
		});		
	}
	
}
