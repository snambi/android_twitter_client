package com.github.snambi.twitterclient.fragemets;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.snambi.twitterclient.R;
import com.github.snambi.twitterclient.TwitterApplication;
import com.github.snambi.twitterclient.clients.TwitterRestClient;

public class ProfileHeaderFragment extends Fragment{
	
	protected TwitterRestClient restClient;
	
	protected TextView tvProfileUserName;
	protected TextView tvProfileTagLine;
	protected TextView tvProfileFollowing;
	protected TextView tvProfileFollowers;
	protected ImageView ivProfileUserImage;
	
	public ProfileHeaderFragment(){
	}
	public ProfileHeaderFragment( TwitterRestClient client){
		restClient = client;
	}
	
	public TwitterRestClient getRestClient() {
		return restClient;
	}
	public void setRestClient(TwitterRestClient restClient) {
		this.restClient = restClient;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		restClient = TwitterApplication.getRestClient();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate( R.layout.fragment_profile_header, container, false);
		
		tvProfileUserName = (TextView) view.findViewById(R.id.tvProfileUserName);
		tvProfileUserName = (TextView) view.findViewById(R.id.tvProfileTagLine);
		ivProfileUserImage = (ImageView ) view.findViewById(R.id.ivProfileUserImage);
		tvProfileFollowers = (TextView)view.findViewById(R.id.tvProfileFollowers);
		tvProfileFollowing =(TextView) view.findViewById(R.id.tvProfileFollowing);
		
		return view;
	}
}
