package com.github.snambi.twitterclient.models;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "users")
public class User extends Model implements Parcelable{

	private static final long serialVersionUID = 4124610137957134742L;
	
	@Column(name="name")
	private String name;
	
	@Column(name="uid", index=true)
	private long uid;
	
	@Column(name="screen_name", index=true, unique=true)
	private String screenName;
	
	@Column(name="profile_image_url")
	private String profileImageUrl;
	
	public User(){
		super();
	}
	
	public static User fromJson(JSONObject jsonObject) {
		
		User user = null;
		
		try {
			
			user = new User();
			
			user.name = jsonObject.getString("name");
			user.uid = jsonObject.getLong("id");
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

	public long getUid() {
		return uid;
	}

	public void setUid(long id) {
		this.uid = id;
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
		dest.writeLong(getUid());
		dest.writeString( getName() );
		dest.writeString(getProfileImageUrl());
		dest.writeString(getScreenName());
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
		super();
		setUid(in.readLong());
		setName(in.readString());
		setProfileImageUrl(in.readString());
		setScreenName(in.readString());
	}
}
