package com.mycompany.prog02;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import io.fabric.sdk.android.Fabric;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.io.File;

/**
 * Created by qinbian on 7/8/15.
 */
public class WriteTweetActivity extends Activity {
    Uri photoUri;
    private static final int TWEET_COMPOSER_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new TweetComposer());
        composeTweet();
    }

    public void composeTweet(){
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Uri photoUri = Uri.parse(extras.getString("photo_uri"));
            Intent intent = new TweetComposer.Builder(this)
                    .text("#cs160excited")
                    .image(photoUri)
                    .createIntent();
            startActivityForResult(intent, TWEET_COMPOSER_REQUEST_CODE);

            /*TweetComposer.Builder builder = new TweetComposer.Builder(this)
                    .text("#cs160excited")
                    .image(photoUri);*/

            /*builder.show();*/
        } else {
            Toast.makeText(getApplicationContext(), "Not pass extras!",
                    Toast.LENGTH_LONG).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        openTwitter();
//        Intent i = new Intent(getApplicationContext(), TwitterSearchActivity.class);
//        WriteTweetActivity.this.startActivity(i);
    }


    public void openTwitter(){
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + "QinBian")));
        }catch (Exception e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/#!/" + "QinBian")));
        }
    }
}
