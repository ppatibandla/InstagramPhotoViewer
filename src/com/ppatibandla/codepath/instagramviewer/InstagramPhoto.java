package com.ppatibandla.codepath.instagramviewer;

import java.util.ArrayList;

import android.R.integer;

public class InstagramPhoto {
	// url/ height/ username/ caption
	public String username;
	public String caption;
	public String imageUrl;
	public int imageHeight;
	public int imageWidth;
	public int likesCount;
	public String iconUrl;
	public int commentCount;
	public ArrayList<InstagramComment> comments;

}
