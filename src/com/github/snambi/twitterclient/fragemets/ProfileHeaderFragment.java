package com.github.snambi.twitterclient.fragemets;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import com.github.snambi.twitterclient.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ProfileHeaderFragment extends Fragment{
	
	protected TwitterRestClient restClient;
	protected String screenName=null;
	
	protected TextView tvProfileUserName;
	protected TextView tvProfileTagLine;
	protected TextView tvProfileFollowing;
	protected TextView tvProfileFollowers;
	protected ImageView ivProfileUserImage;
	
	public ProfileHeaderFragment(){
	}
	public ProfileHeaderFragment( TwitterRestClient client, String screenName){
		restClient = client;
		this.screenName = screenName;
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
		tvProfileTagLine = (TextView) view.findViewById(R.id.tvProfileTagLine);
		ivProfileUserImage = (ImageView ) view.findViewById(R.id.ivProfileUserImage);
		tvProfileFollowers = (TextView)view.findViewById(R.id.tvProfileFollowers);
		tvProfileFollowing =(TextView) view.findViewById(R.id.tvProfileFollowing);
		
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		loadDetails();
	}
	
	private void loadDetails() {
		
			
			if( screenName == null ){
				
				restClient.getMyInfo( new JsonHttpResponseHandler(){
					
					@Override
					public void onFailure(Throwable arg0, JSONObject arg1) {
						super.onFailure(arg0, arg1);
					}
					
					@Override
					public void onSuccess(int arg0, JSONObject json) {
						User user = User.fromJson(json);
						//getActionBar().setTitle( "@" + user.getScreenName() );
						
						tvProfileUserName.setText(user.getName());
						tvProfileTagLine.setText(user.getTag());
						tvProfileFollowers.setText(user.getFollowers() + " followers");
						tvProfileFollowing.setText(user.getFollowing() + " follwiing");
						ImageLoader imageLoader = ImageLoader.getInstance();
						imageLoader.displayImage(user.getProfileImageUrl(), ivProfileUserImage );
					}
				});

			}else{
				
				ArrayList<String> screenNames = new ArrayList<String>();
				screenNames.add(screenName);
				
				restClient.getUsersDetails(screenNames, new JsonHttpResponseHandler(){
					
					@Override
					public void onFailure(Throwable error, JSONObject json) {
						super.onFailure(error, json);
					}
					
					@Override
					public void onSuccess(int arg0, JSONArray jsonarray) {
						// it should have only one object
						try {
							if( jsonarray.length() == 1 ){ 
								JSONObject json = jsonarray.getJSONObject(0);
								
								User user = User.fromJson(json);
								
								tvProfileUserName.setText(user.getName());
								tvProfileTagLine.setText(user.getTag());
								tvProfileFollowers.setText(user.getFollowers() + " followers");
								tvProfileFollowing.setText(user.getFollowing() + " follwiing");
								ImageLoader imageLoader = ImageLoader.getInstance();
								imageLoader.displayImage(user.getProfileImageUrl(), ivProfileUserImage );

							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});

			}
		
	}
}
