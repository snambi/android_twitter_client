package com.github.snambi.twitterclient.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.snambi.twitterclient.R;
import com.github.snambi.twitterclient.models.Tweet;
import com.github.snambi.twitterclient.utils.TwitterTimeUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TwitterArrayAdapter extends ArrayAdapter<Tweet>{

	public TwitterArrayAdapter(Context context, List<Tweet> objects) {
		super(context, 0, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		Tweet tweet = getItem(position);
		
		View v=null;
		if( convertView == null ){
			LayoutInflater inflater = LayoutInflater.from(getContext());
			v= inflater.inflate( R.layout.tweet_item, parent, false);
		}else{
			v=convertView;
		}
		
		ViewHolder holder=null;
		if( v.getTag() == null ){
			holder = new ViewHolder();
			holder.imgProfileImage = (ImageView) v.findViewById(R.id.imgItemProfileImage);
			holder.tvScreenName = (TextView) v.findViewById(R.id.tvScreenName);
			holder.tvBody = (TextView) v.findViewById(R.id.tvTweetBody);
			holder.tvCreatedTime = (TextView) v.findViewById(R.id.tvTweetCreatedTime);
		}else{
			holder = (ViewHolder) v.getTag();
		}
		
		
		holder.imgProfileImage.setImageResource(android.R.color.transparent);
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(tweet.getUser().getProfileImageUrl(), holder.imgProfileImage);
		
		holder.tvBody.setText( tweet.getBody());
		holder.tvScreenName.setText( tweet.getUser().getScreenName());
		String relativeTime = TwitterTimeUtils.getRelativeTimeAgo( tweet.getCreatedAt());
		holder.tvCreatedTime.setText(relativeTime);
		
		return v;
	}
	
	public class ViewHolder{
		ImageView imgProfileImage;
		TextView tvScreenName;
		TextView tvBody;
		TextView tvCreatedTime;
	}
}
