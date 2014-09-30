package com.github.snambi.twitterclient.models;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable{

	private static final long serialVersionUID = 4124610137957134742L;
	private String name;
	private long id;
	private String screenName;
	private String profileImageUrl;
	
	public static User fromJson(JSONObject jsonObject) {
		
		User user = null;
		
		try {
			
			user = new User();
			
			user.name = jsonObject.getString("name");
			user.id = jsonObject.getLong("id");
			user.screenName = jsonObject.getString("screen_name");
			user.profileImageUrl = jsonObject.getString("profile_image_url");
			
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return user;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		
	}

	public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {

		@Override
		public User createFromParcel(Parcel source) {
			return new User(source);
		}

		@Override
		public User[] newArray(int size) {
			return new User[size];
		}
	};
	
	private User( Parcel in ){
		setId(in.readLong());
		setName(in.readString());
		setProfileImageUrl(in.readString());
		setScreenName(in.readString());
	}
}
