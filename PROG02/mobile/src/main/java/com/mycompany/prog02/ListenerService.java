package com.mycompany.prog02;

import android.content.Intent;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by qinbian on 7/9/15.
 */
public class ListenerService extends WearableListenerService {

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        startCamera();
    }


/*    String nodeId;
    public static final String START_CAMERA_MESSAGE_PATH = "/start_camera";*/

/*    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        nodeId = messageEvent.getSourceNodeId();
        if (messageEvent.getPath().equals(START_CAMERA_MESSAGE_PATH)){
            startCamera();
        }
    } */

    private void startCamera() {
        Intent mIntent = new Intent(this, StartCameraActivity.class);
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
    }
}
