package com.ppatibandla.codepath.instagramviewer;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.R.string;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;


public class PhotosActivity extends Activity {

	public static final String CLIENT_ID = "dec6f77fe21e4532bd2fce9988a94053";
	private ArrayList<InstagramPhoto> photos;
	private InstagramPhotosAdapter aPhotos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        photos = new ArrayList<InstagramPhoto>();
        aPhotos = new InstagramPhotosAdapter(this, photos);
        ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);
        //set adapter to list view
        lvPhotos.setAdapter(aPhotos);
        fetchPopularPhotos();
    }

    private void fetchPopularPhotos() {
    	//https://api.instagram.com/v1/media/popular?client_id=
    	// Setup popular url endpoint
    	String popularUrl = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;
    	
    	// Trigger network request
    	AsyncHttpClient client = new AsyncHttpClient();
    	client.get(popularUrl, new JsonHttpResponseHandler() {
    		// define success and failure callbacks
    		// Handle successful response.
    		@Override
    		public void onSuccess(int statusCode, Header[] headers,
    				JSONObject response) {
    			// url/ height/ username/ caption
    			JSONArray photosJSON = null;
    			try {
    				photos.clear();
    				photosJSON = response.getJSONArray("data");
    				for (int i =0; i < photosJSON.length(); i++) {
    					JSONObject photoJSON = photosJSON.getJSONObject(i);
    					InstagramPhoto photo = new InstagramPhoto();
    					if (photoJSON.optJSONObject("user") != null) {
    						photo.username = photoJSON.getJSONObject("user").getString("username");
    						photo.iconUrl = photoJSON.getJSONObject("user").getString("profile_picture");
    					}
    					if (photoJSON.optJSONObject("caption") != null) {
    						photo.caption = photoJSON.getJSONObject("caption").getString("text");
    					}
    					if ((photoJSON.optJSONObject("images") != null)
    							&& (photoJSON.optJSONObject("images").optJSONObject("standard_resolution") != null)) {
    						photo.imageUrl = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
    						photo.imageHeight = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
    						photo.imageWidth = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("width");
    					}
    					if (photoJSON.optJSONObject("likes") != null) {
    						photo.likesCount = photoJSON.getJSONObject("likes").getInt("count");
    					}
    					
						photo.comments = new ArrayList<InstagramComment>();
    					if (photoJSON.optJSONObject("comments") != null) {
    						photo.commentCount = photoJSON.getJSONObject("comments").getInt("count");
    						JSONArray commentsArray = photoJSON.getJSONObject("comments").getJSONArray("data");
    						for (int c_i = commentsArray.length() - 1; (c_i >= 0) && (c_i >= commentsArray.length() - 2); c_i--) {
    							JSONObject latestComment = commentsArray.getJSONObject(c_i);
    							InstagramComment c = new InstagramComment();
    							c.text = latestComment.getString("text");
    							c.user = latestComment.getJSONObject("from").getString("username");
    							c.time = latestComment.getInt("created_time");
    							photo.comments.add(c);
    						}
//    						photo.comment.user = latestComment.getJSONObject("from").getString("username");
//   						photo.comment.time = latestComment.getInt("created_time");
    					}
    					photos.add(photo);
    				}
    				aPhotos.notifyDataSetChanged();
    			} catch(JSONException e){
    				e.printStackTrace();
    			}
    			// TODO Auto-generated method stub
    			super.onSuccess(statusCode, headers, response);
    		}
    		
    		@Override
    		public void onFailure(int statusCode, Header[] headers,
    				String response, Throwable throwable) {
    			//Log.i("onFailure : ", response.toString());
    			// TODO Auto-generated method stub
    			super.onFailure(statusCode, headers, response, throwable);
    		}
    	});
    	
    	// Handle response
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.photos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
