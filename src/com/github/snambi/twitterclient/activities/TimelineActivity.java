package com.github.snambi.twitterclient.activities;

import org.json.JSONArray;

import android.app.Activity;
import android.os.Bundle;

import com.activeandroid.util.Log;
import com.github.snambi.twitterclient.R;
import com.github.snambi.twitterclient.TwitterApplication;
import com.github.snambi.twitterclient.clients.TwitterRestClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TimelineActivity extends Activity {

	TwitterRestClient twitterClient;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		
		twitterClient = TwitterApplication.getRestClient();
		
		populateTimeline();
	}

	private void populateTimeline() {
		twitterClient.getHomeTimeLine( new JsonHttpResponseHandler(){
			
			@Override
			public void onFailure(Throwable t, String s) {
				Log.d( "DEBUG", s, t);
			}
			
			@Override
			public void onSuccess(JSONArray jsonArray) {
				Log.d("DEBUG", jsonArray.toString() );
			}
		});		
	}
}
