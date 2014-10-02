package com.github.snambi.twitterclient.db;

import java.util.List;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.From;
import com.activeandroid.query.Select;
import com.github.snambi.twitterclient.models.Tweet;

public class TweetDbHelper {

	public static boolean existsInDb( List<Tweet> tweets ){
		boolean result = true;
		
		// if all the tweets are already present in the table, then return "true"
		Select select = new Select();
		for( Tweet tweet: tweets ){
			Tweet t = select.from(Tweet.class).where("uid == ? ", tweet.getUid() ).executeSingle();
			if( t != null || t.getUid() == 0 ){
				// its fake
				result=false;
				break;
			}
		}
		
		return result;
	}
	
	public static void saveWhenNotPresent( List<Tweet> tweets ){
		
		try
		{
			ActiveAndroid.beginTransaction();
			
			Select select = new Select();
			From from = select.from(Tweet.class);
		
			for( Tweet tweet : tweets ){
				Tweet  t = from.where("uid == ? ", tweet.getUid() ).executeSingle();
				if( t==null ){
					tweet.save();
				}
			}
			ActiveAndroid.setTransactionSuccessful();
		}finally{
			ActiveAndroid.endTransaction();
		}
	}
}
