package com.mycompany.prog02;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.CapabilityApi;
import com.google.android.gms.wearable.CapabilityInfo;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by qinbian on 7/10/15.
 */
public class StartCameraService extends Service {

    private static final String START_CAMERA_CAPABILITY_NAME = "start_camera";
    private GoogleApiClient mGoogleApiClient;
    private String startCameraNodeId = null;
    private static final long TIME_LIMIT = 20;

    private void setupStartCamera(){
        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected (Bundle bundle){
                Log.e("state:", "onConnected");
            }
            @Override
            public void onConnectionSuspended(int i){
                Log.e("state:", "Connection is suspended");
            }
        })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {
                        Log.e("state:", "connection failed");

                    }
                })
                .addApi(Wearable.API)
                .build();

        Thread t = new Thread(new Runnable(){
            @Override
            public void run(){
                ConnectionResult connectionResult = mGoogleApiClient.blockingConnect(TIME_LIMIT, TimeUnit.SECONDS);

                CapabilityApi.GetCapabilityResult result =
                        Wearable.CapabilityApi.getCapability(
                                mGoogleApiClient, START_CAMERA_CAPABILITY_NAME,
                                CapabilityApi.FILTER_REACHABLE).await();
                updateTranscriptionCapability(result.getCapability());

                if (startCameraNodeId != null) {
                    Wearable.MessageApi.sendMessage(mGoogleApiClient, startCameraNodeId,
                            START_CAMERA_CAPABILITY_NAME, null).await();
                } else {
                    Log.e("error:", "startCameraNodeId is null");

                }
            }
        });
        t.start();

    }

    private void updateTranscriptionCapability(CapabilityInfo capabilityInfo) {
        Set<Node> connectedNodes = capabilityInfo.getNodes();
        startCameraNodeId = pickBestNodeId(connectedNodes);
        Log.e("startCameraNodeId:", "in updateTranscriptionCapability is" + startCameraNodeId);
    }

    private String pickBestNodeId(Set<Node> nodes) {
        String bestNodeId = null;
        // Find a nearby node or pick one arbitrarily
        for (Node node : nodes) {
            if (node.isNearby()) {
                return node.getId();
            }
            bestNodeId = node.getId();
        }
        return bestNodeId;
    }

    /* Deliver the message */
    private void requestStartCamera() {
        if (startCameraNodeId != null) {
            Wearable.MessageApi.sendMessage(mGoogleApiClient, startCameraNodeId,
                    START_CAMERA_CAPABILITY_NAME, null).await();
            Toast.makeText(getApplicationContext(), "Already Sent Message",
                    Toast.LENGTH_SHORT).show();
        } else {
            Log.e("error:", "startCameraNodeId is null");

        }
    }


    @Override
    public void onCreate(){
        super.onCreate();
        setupStartCamera();
    }




    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
