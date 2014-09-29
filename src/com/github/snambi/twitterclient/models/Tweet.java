package com.github.snambi.twitterclient.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Tweet {

	private String body;
	private String createdAt;
	private long uid;
	private User user;
	
	public static Tweet fromJSON( JSONObject jsonObject ){
		Tweet tweet = new Tweet();
		
		try {
			tweet.body = jsonObject.getString("text");
			tweet.createdAt = jsonObject.getString("created_at");
			tweet.uid = jsonObject.getLong("id");
			tweet.user = User.fromJson( jsonObject.getJSONObject("user"));
			
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		
		return tweet;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public String toString(){
		return getBody() + " -- " + getUser().getScreenName();
	}

	public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray) {
		
		ArrayList<Tweet> tweets = new ArrayList<Tweet>();
		
		for( int i=0; i<jsonArray.length(); i++ ){
			Tweet t=null;
			try {
				
				JSONObject json = (JSONObject) jsonArray.get(i);
				t = Tweet.fromJSON(json);
				
			} catch (JSONException e) {
				e.printStackTrace();
				continue;
			}
			
			if( t != null ){
				tweets.add(t);			
			}

		}
		return tweets;
	}
}
