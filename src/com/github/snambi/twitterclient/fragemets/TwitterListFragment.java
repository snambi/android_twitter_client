package com.github.snambi.twitterclient.fragemets;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.github.snambi.twitterclient.R;
import com.github.snambi.twitterclient.TwitterApplication;
import com.github.snambi.twitterclient.adapters.TwitterArrayAdapter;
import com.github.snambi.twitterclient.clients.TwitterRestClient;
import com.github.snambi.twitterclient.models.Tweet;

public class TwitterListFragment extends Fragment {
	
	protected TwitterRestClient client;
	
	protected ListView lvTweets;
	protected List<Tweet> tweets = new ArrayList<Tweet>();
	protected TwitterArrayAdapter aTweets=null;

	public TwitterListFragment( ){
	}
	
	public TwitterListFragment( TwitterRestClient client){
		this.client = client;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		client = TwitterApplication.getRestClient();
		//TwitterRestClient c = (TwitterRestClient) savedInstanceState.getSerializable("client");
		//String test = savedInstanceState.getString("test");
		
		//TwitterRestClient c  = (TwitterRestClient) getArguments().getSerializable("client");
		//String test = getArguments().getString("test");

		aTweets = new TwitterArrayAdapter(getActivity(), tweets);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
	
		View view = inflater.inflate(R.layout.fragment_twitter_list, container, false);
		lvTweets = (ListView) view.findViewById(R.id.lvTweets);
		lvTweets.setAdapter(aTweets);
		
		return view;
	}
		
	public TwitterRestClient getClient() {
		return client;
	}
	public void setClient(TwitterRestClient client) {
		this.client = client;
	}

	// provides a way to add tweets from the activity
	public void addTweets( List<Tweet> tweets){
		aTweets.addAll(tweets);
	}
	
	public void addTweetAtPosition( Tweet tweet , int position){
		if( tweet != null && tweets !=null ){
			tweets.add(position, tweet);
			aTweets.notifyDataSetChanged();
		}
	}
}
