package com.github.snambi.twitterclient.clients;

import java.util.List;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.github.snambi.twitterclient.models.Tweet;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterRestClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "308Y6AhszvPZK1Tg9l7RDXf9Z";       // Change this
	public static final String REST_CONSUMER_SECRET = "xdRYse6QDDbWkE5bFA7xB5XsLwl6DWusTUSytx7TMge4LE1YNS"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://twitterclientnambi"; // Change this (here and in manifest)

	public TwitterRestClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}
	
	
	public void getHomeTimeLine( AsyncHttpResponseHandler responseHandler ){
		
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		RequestParams params = new RequestParams();
		
		// get the sinceId and maxId
		TimelineCounter counter = TimelineCounter.getInstance();
		
		boolean m = false;
		boolean s = false;
		
//		if( counter.getSinceId() > 0){
//			params.put("since_id", counter.getSinceIdStr() );
//			s=true;
//		}
		if( counter.getMaxId() > 0){
			params.put("max_id", counter.getMaxIdStr() );
			m=true;
		}
		
		if( s==false && m==false){
			client.get(apiUrl, null, responseHandler);
		}else{
			client.get(apiUrl, params, responseHandler);
		}
		
	}
	
	
	public void saveUserInfo( AsyncHttpResponseHandler responseHandler){
		String api_url = getApiUrl("account/verify_credentials.json");
		
		client.get(api_url, responseHandler);
	}


	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */
	
	
	/**
	 * <code>TimelineCounter</code> keeps track of which tweets are downloaded and which need to be fetched.
	 * this is a singleton.
	 */
	public static  class TimelineCounter{
		
		// max value of the received tweets
		private long sinceId=0l;
		// min values of the received tweets 
		private long maxId=0l;
		
		private static TimelineCounter timelineCounter;
		
		private TimelineCounter(){
		}
		
		public static TimelineCounter getInstance(){
			if( timelineCounter == null ){
				timelineCounter = new TimelineCounter();
			}
			return timelineCounter;
		}

		public long getSinceId() {
			return sinceId;
		}
		public String getSinceIdStr(){
			return ""+ sinceId;
		}

		public void setSinceId(long sinceId) {
			this.sinceId = sinceId;
		}

		public String getMaxIdStr(){
			return ""+ maxId;
		}
		
		public long getMaxId() {
			return maxId;
		}

		public void setMaxId(long maxId) {
			this.maxId = maxId;
		}
		
		public void setSinceIdMaxIdFrom( List<Tweet> tweets){
			if( tweets == null || tweets.size() == 0){
				return ;
			}
			
			// find the highest id and lowest id from the list
			long high=0;
			long low=0;
			
			for( Tweet t : tweets ){
				
				// special case, if "low" is 0 , then start from the first value
				if( low == 0){
					low = t.getUid();
				}
				if( t.getUid() <= low){
					low = t.getUid();
				}
				if( t.getUid() > high){
					high = t.getUid();
				}
			}
			
			if( high > getSinceId() ){
				setSinceId(high);
			}else if( getSinceId() == 0){
				// handle special case
				setSinceId(high);
				
			}
			if( low < getMaxId() ){
				setMaxId(low);
			}else if( getMaxId() == 0){
				setMaxId(low);
			}
		}
		
	}
}