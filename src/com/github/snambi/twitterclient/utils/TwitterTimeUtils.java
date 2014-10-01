package com.github.snambi.twitterclient.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import android.content.res.Resources;
import android.text.format.DateUtils;
import android.text.format.Time;

public class TwitterTimeUtils {

    public static final long SECOND_IN_MILLIS = 1000;
    public static final long MINUTE_IN_MILLIS = SECOND_IN_MILLIS * 60;
    public static final long HOUR_IN_MILLIS = MINUTE_IN_MILLIS * 60;
    public static final long DAY_IN_MILLIS = HOUR_IN_MILLIS * 24;
    public static final long WEEK_IN_MILLIS = DAY_IN_MILLIS * 7;
    /**
     * This constant is actually the length of 364 days, not of a year!
     */
    public static final long YEAR_IN_MILLIS = WEEK_IN_MILLIS * 52;

    
	public static String getRelativeTimeAgo(String rawJsonDate) {
		String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
		SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
		sf.setLenient(true);
	 
		String relativeDate = "";
		try {
			long dateMillis = sf.parse(rawJsonDate).getTime();

//			relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
//																System.currentTimeMillis(), 
//																DateUtils.SECOND_IN_MILLIS).toString();
			
			relativeDate = getTwitterRelativeTimeSpanString(dateMillis,
																System.currentTimeMillis(), 
																DateUtils.SECOND_IN_MILLIS).toString();

		} catch (ParseException e) {
			e.printStackTrace();
		}
	 
		return relativeDate;
	}
	
	/**
	 * Only handles past time in twitter format.
	 * 2s
	 * 2m
	 * 2h
	 * 3d
	 * 
	 * @param time
	 * @param now
	 * @param minResolution
	 * @return
	 */
    public static CharSequence getTwitterRelativeTimeSpanString(long time, 
    															long now, 
    															long minResolution) {
    	StringBuilder result = new StringBuilder();
    	
    	Resources r = Resources.getSystem();
        boolean past = (now >= time);
        long duration = Math.abs(now - time);

        int resId;
        long count;
        if (duration < MINUTE_IN_MILLIS && minResolution < MINUTE_IN_MILLIS) {
            count = duration / SECOND_IN_MILLIS;
            if (past) {
            	result.append(count);
            	result.append("s");
            } else {
            }
        } else if (duration < HOUR_IN_MILLIS && minResolution < HOUR_IN_MILLIS) {
            count = duration / MINUTE_IN_MILLIS;
            if (past) {
            	result.append(count);
            	result.append("m");
            } else {
            }
        } else if (duration < DAY_IN_MILLIS && minResolution < DAY_IN_MILLIS) {
            count = duration / HOUR_IN_MILLIS;
            if (past) {
            	result.append(count);
            	result.append("h");
            } else {
            }
        } else if (duration < WEEK_IN_MILLIS && minResolution < WEEK_IN_MILLIS) {
        		result.append( DateUtils.getRelativeTimeSpanString(time, now, minResolution) );
        		//return getRelativeDayString(r, time, now);
        } else {
            // We know that we won't be showing the time, so it is safe to pass
            // in a null context.
            result.append( DateUtils.formatDateRange(null, time, time, 0) );
        }

//        String format = r.getQuantityString(resId, (int) count);
//        return String.format(format, count);
        
        return result.toString();
    }
    

}
