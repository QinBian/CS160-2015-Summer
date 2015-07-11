package com.mycompany.prog02;

import android.app.Activity;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by qinbian on 7/8/15.
 */
public class StartCameraActivity extends Activity {

    static final int REQUEST_TAKE_PHOTO = 1;
    String mCurrentPhotoPath;
    Uri imageUri;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        dispatchTakePictureIntent();
    }

//    private View.OnClickListener cameraListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            takePhoto(v);
//        }
//    };
//
//    private void takePhoto(View v) {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        File photo = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "picture.jpeg");
//        imageUri = Uri.fromFile(photo);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == Activity.RESULT_OK){
            Uri selectedImage = imageUri;
            Intent i = new Intent(getApplicationContext(), WriteTweetActivity.class);
            i.putExtra("photo_uri", selectedImage.toString());
            startActivity(i);
        }
    }


    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        Toast.makeText(getApplicationContext(), mCurrentPhotoPath,
                Toast.LENGTH_LONG).show();
        return image;
    }



    public void dispatchTakePictureIntent(){
        //Button btn = (Button) findViewById(R.id.take_photo);
        //btn.setOnClickListener(new View.OnClickListener() {
            //@Override
           //public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch(IOException ex) {

                    }
                    if (photoFile != null) {
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(photoFile));
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                        imageUri = Uri.fromFile(photoFile);
                    }
                }
            //}
        //});
    }


}
