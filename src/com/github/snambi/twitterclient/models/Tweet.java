package com.github.snambi.twitterclient.models;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "tweets")
public class Tweet extends Model implements Parcelable {

	@Column(name="body")
	private String body;
	
	@Column(name="created_at")
	private String createdAt;
	
	@Column(name="uid", index=true, unique=true)
	private long uid;
	
	@Column(name="user", index=true)
	private User user;
	
	public Tweet(){
		super();
	}
	
	public Tweet(String body, String created, long id, User usr){
		this.body = body;
		this.createdAt = created;
		this.uid = id;
		this.user = user;
	}
	
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

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(getBody());
		dest.writeString(getCreatedAt() );
		dest.writeLong( getUid());
		
		user.writeToParcel(dest, flags);
	}
	
	public static final Parcelable.Creator<Tweet> CREATOR = new Parcelable.Creator<Tweet>() {

		@Override
		public Tweet createFromParcel(Parcel source) {
			return new Tweet(source);
		}

		@Override
		public Tweet[] newArray(int size) {
			return new Tweet[size];
		}
	};
	
	private Tweet( Parcel in ){
		super();
		setBody(in.readString());
		setCreatedAt(in.readString());
		setUid(in.readLong());
		setUser(  User.CREATOR.createFromParcel(in) );
	}
}
