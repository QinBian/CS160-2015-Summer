package com.mycompany.prog02;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.tweetui.LoadCallback;
import com.twitter.sdk.android.tweetui.TweetUtils;
import com.twitter.sdk.android.tweetui.TweetView;

import com.twitter.sdk.android.tweetui.TweetUtils;

/**
 * Created by qinbian on 7/10/15.
 */
public class TwitterSearchActivity extends Activity {
    String searchURL = "https://api.twitter.com/1.1/search/tweets.json?q=%23cs160excited";
    private TextView tweetDisplay;
    private LinearLayout myLayout;



    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_search);
        myLayout= (LinearLayout) findViewById(R.id.my_tweet_layout);
    }

    private class GetTweets extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... twitterURL) {
            StringBuilder tweetFeedBuilder = new StringBuilder();
            for (String searchURL : twitterURL) {
                HttpClient tweetClient = new DefaultHttpClient();
                try {
                    HttpGet tweetGet = new HttpGet(searchURL);
                    HttpResponse tweetResponse = tweetClient.execute(tweetGet);

                    StatusLine searchStatus = tweetResponse.getStatusLine();
                    if (searchStatus.getStatusCode() == 200) {
                        HttpEntity tweetEntity = tweetResponse.getEntity();
                        InputStream tweetContent = tweetEntity.getContent();

                        InputStreamReader tweetInput = new InputStreamReader(tweetContent);
                        BufferedReader tweetReader = new BufferedReader(tweetInput);

                        String lineIn;
                        while ((lineIn = tweetReader.readLine()) != null) {
                            tweetFeedBuilder.append(lineIn);
                        }
                    }
                    else
                        Log.e("tag1:", "Whoops - something went wrong!");
                }
                catch(Exception e) {
                    Log.e("exception:", "received in GetTweets");
                }
            }
            return tweetFeedBuilder.toString();
        }

        protected void onPostExecute(String result){
            String tweetResultId;
            try {
                JSONObject resultObject = new JSONObject(result);
                JSONArray tweetArray = resultObject.getJSONArray("results");

                for (int t=0; t<1; t++) {
                    JSONObject tweetObject = tweetArray.getJSONObject(t);
                    tweetResultId = tweetObject.getString("id_str");
                    long tweetId = Long.parseLong(tweetResultId, 10);
                    loadTweet(tweetId);

                }
            }
            catch (Exception e) {
                Log.e("tag2:", "Whoops - something went wrong!");
            }
        }

        protected void loadTweet(long tweetId){
            TweetUtils.loadTweet(tweetId, new LoadCallback<Tweet>() {
                @Override
                public void success(Tweet tweet) {
                    myLayout.addView(new TweetView(TwitterSearchActivity.this, tweet));
                }

                @Override
                public void failure(TwitterException exception) {
                    Log.e("tag3: ", "failure");
                }
            });

        }
    }
}
