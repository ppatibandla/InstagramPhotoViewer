package com.ppatibandla.codepath.instagramviewer;

import java.util.IllegalFormatCodePointException;
import java.util.List;

import com.squareup.picasso.Picasso;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.BufferType;

public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto> {

	public InstagramPhotosAdapter(Context context, List<InstagramPhoto> photos) {
		super(context, android.R.layout.simple_list_item_1, photos);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		InstagramPhoto photo = getItem(position);
		if(convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
		}
		TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
		ImageView imgPhoto = (ImageView) convertView.findViewById(R.id.imgPhoto);
		TextView tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
		ImageView imgUserIcon = (ImageView) convertView.findViewById(R.id.imgUserIcon);
		Spannable caption = new SpannableString(photo.username + " " + photo.caption);
		caption.setSpan(new ForegroundColorSpan(Color.BLUE), 0, photo.username.length(), 0);
		tvCaption.setText(caption, BufferType.SPANNABLE);
		Float aspect_ratio =  ((float)photo.imageHeight / photo.imageWidth);
		Log.i("OnSuccess", "convertView width : " + String.valueOf(parent.getWidth()));
		Log.i("OnSunccess", "Aspect ratio : " + String.valueOf(aspect_ratio));
		imgPhoto.getLayoutParams().height = (int) (parent.getWidth() * aspect_ratio);
		
		imgPhoto.setImageResource(0);
		Picasso.with(getContext()).load(photo.imageUrl).into(imgPhoto);
		
		TextView tvCommentCount = (TextView) convertView.findViewById(R.id.tvCommentCount);
		tvCommentCount.setText(String.valueOf(photo.commentCount) + " " + "comments");
		TextView tvComment = (TextView) convertView.findViewById(R.id.tvComment1);
		TextView tvComment2 = (TextView) convertView.findViewById(R.id.tvComment2);
		if (! photo.comments.isEmpty()) {
			String user = photo.comments.get(0).user;
			String text = photo.comments.get(0).text;
			Spannable comment1 = new SpannableString(user + " " + text);
			comment1.setSpan(new ForegroundColorSpan(Color.BLUE), 0, user.length(), 0);
			tvComment.setText(comment1, BufferType.SPANNABLE);
			
			if (photo.comments.size() > 1) {
				user = photo.comments.get(1).user;
				text = photo.comments.get(1).text;
				Spannable comment2 = new SpannableString(user + " " + text);
				comment2.setSpan(new ForegroundColorSpan(Color.BLUE), 0, user.length(), 0);
				tvComment2.setText(comment2, BufferType.SPANNABLE);
			} else {
				tvComment2.setVisibility(View.GONE);
			}
		} else {
			tvComment.setVisibility(View.GONE);
			tvComment2.setVisibility(View.GONE);
		}
		
		tvUserName.setText(photo.username);
		tvUserName.setTextColor(Color.BLUE);
		Picasso.with(getContext()).load(photo.iconUrl).into(imgUserIcon);
//		Picasso.with(getContext()).load(photo.iconUrl).
		return convertView;
	}





}
