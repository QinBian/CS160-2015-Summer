package com.mycompany.prog02;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by qinbian on 7/8/15.
 */
public class PageDisplayActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_display);

        Button cameraButton = (Button) findViewById(R.id.take_photo);
        cameraButton.setOnClickListener(startCameraListener);

    }

    private View.OnClickListener startCameraListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent myIntent = new Intent(PageDisplayActivity.this, StartCameraActivity.class);
            PageDisplayActivity.this.startActivity(myIntent);
        }
    };
}
