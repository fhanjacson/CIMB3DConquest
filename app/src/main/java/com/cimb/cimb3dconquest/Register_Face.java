package com.cimb.cimb3dconquest;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;


import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobileconnectors.s3.transferutility.*;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.models.nosql.CimbDO;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClient;
import com.amazonaws.services.rekognition.model.*;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.util.IOUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class Register_Face extends AppCompatActivity implements SurfaceHolder.Callback{
    Camera camera;
    SurfaceView sViewCamera;
    SurfaceHolder surfaceHolder;
    Camera.PictureCallback rawCallback;
    Camera.ShutterCallback shutterCallback;
    Camera.PictureCallback jpegCallback;
    Button btnProceed, btnCancel, btnCapture;
    Button start, stop, capture;
    String random = "OtherUser"+String.valueOf(new Random().nextInt((999 - 100) + 1) + 100);;
    BasicAWSCredentials awscred = new BasicAWSCredentials("AKIAIO667S4CLKPRWLKQ", "5kViAMSzCq8+4SzGvMK7qP2nMC+N3SffunhjQ+6F");
    AmazonS3Client s3Client = new AmazonS3Client(awscred);
    static CompareFace cf;
//    LoginActivity loginActivity = new LoginActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register__face);



        AWSMobileClient.getInstance().initialize(this).execute();
        Toast.makeText(this, Environment.getExternalStorageDirectory().getPath(), Toast.LENGTH_SHORT).show();

        start = (Button)findViewById(R.id.btnProceed);
        start.setOnClickListener(new Button.OnClickListener()
        {
            public void onClick(View arg0) {
                start_camera();
            }
        });
        stop = (Button)findViewById(R.id.btnCancel);
        stop.setOnClickListener(new Button.OnClickListener()
        {
            public void onClick(View arg0) {
                stop_camera();
            }
        });
        stop.setEnabled(false);
        capture = (Button) findViewById(R.id.btnCapture);
        capture.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                captureImage();
            }
        });
        capture.setEnabled(false);

        sViewCamera = (SurfaceView)findViewById(R.id.sViewCamera);
        surfaceHolder = sViewCamera.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        rawCallback = new Camera.PictureCallback() {
            public void onPictureTaken(byte[] data, Camera camera) {

                Log.d("Log", "onPictureTaken - raw");
            }
        };

        shutterCallback = new Camera.ShutterCallback() {
            public void onShutter() {
                Log.i("Log", "onShutter'd");
            }
        };
        jpegCallback = new Camera.PictureCallback() {
            public void onPictureTaken(byte[] data, Camera camera) {

                FileOutputStream outStream = null;
                try {
                    outStream = new FileOutputStream(String.format(Locale.ENGLISH , Environment.getExternalStorageDirectory().getPath()+ "/DCIM/"+LoginActivity.user.getUsername()+".jpg", System.currentTimeMillis()));
                    outStream.write(data);
                    outStream.close();
                    Log.d("Log", "onPictureTaken - wrote bytes: " + data.length);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d("Log", "onPictureTaken - jpeg");
            }
        };

    }

    private void start_camera()
    {
        start.setEnabled(false);
        try{
            camera = Camera.open(1);
        }catch(RuntimeException e){
            Log.e("My Error", "init_camera: " + e);
            return;
        }
        Camera.Parameters param;
        param = camera.getParameters();
        //modify parameter
        param.setPreviewFrameRate(30);
        param.setRotation(270);
        param.setPictureSize(1280,720);
//        param.setPreviewSize(176, 144);


        param.setPreviewSize(1280 , 720);


        camera.setParameters(param);
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.setDisplayOrientation(90);
            camera.startPreview();
            //camera.takePicture(shutter, raw, jpeg)
        } catch (Exception e) {
            Log.e("My Error", "init_camera: " + e);
        }
        finally{
            capture.setEnabled(true);

        }
    }

    private void captureImage() {
        // TODO Auto-generated method stub
        capture.setEnabled(false);
        camera.setDisplayOrientation(90);
        camera.takePicture(shutterCallback, rawCallback, jpegCallback);
        Toast.makeText(this,LoginActivity.user.getUsername()+".jpg", Toast.LENGTH_LONG).show();
        stop.setEnabled(true);
    }

    private void stop_camera()
    {
        stop.setEnabled(false);
        camera.stopPreview();
        camera.release();
        uploadWithTransferUtility(LoginActivity.user.getUsername());

        //IMPORTANT !!!
//        cf = new CompareFace(false, "372.jpg","303.jpg","s3-cimb");
//        etc asd = new etc();
//        if(cf.isResult()){
//            Log.d("Verification","CF Value is TRUE");
//        }
//        else{
//            Log.d("Verification","CF Value is FALSE");
//        }

//        asd.execute();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                verify();
//            }
//        }, 2000);


        Intent intent = new Intent( Register_Face.this, Register_Pattern.class);
        startActivity(intent);
    }

//    void verify(){
//        if(cf.isResult()){
//            Log.d("Verification","CF Value on verify is TRUE");
//        }
//        else{
//            Log.d("Verification","CF Value on verify is FALSE");
//        }
//        if(cf.isResult()){
//            Toast.makeText(this,"Correct", Toast.LENGTH_SHORT).show();
//        }
//        else{
//            Toast.makeText(this,"Wrong", Toast.LENGTH_SHORT).show();
//        }
//    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    public void uploadWithTransferUtility(String username) {


        TransferUtility transferUtility =
                TransferUtility.builder()
                        .context(getApplicationContext())
                        .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                        .s3Client(s3Client)
                        .build();

        TransferObserver uploadObserver =
                transferUtility.upload(
                        "s3-cimb", username+".jpg",
                        new File(Environment.getExternalStorageDirectory().getPath()+ "/DCIM/"+username+".jpg"));

        // Attach a listener to the observer to get state update and progress notifications
        uploadObserver.setTransferListener(new TransferListener() {

            @Override
            public void onStateChanged(int id, TransferState state) {
                if (TransferState.COMPLETED == state) {
                    Log.d("TAGGGGGG", "SUCEEESSS");
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                float percentDonef = ((float) bytesCurrent / (float) bytesTotal) * 100;
                int percentDone = (int)percentDonef;

                Log.d("YourActivity", "ID:" + id + " bytesCurrent: " + bytesCurrent
                        + " bytesTotal: " + bytesTotal + " " + percentDone + "%");
            }

            @Override
            public void onError(int id, Exception ex) {
                Log.d("TAGGGGGG", "FAILLLLL");
            }

        });

        // If you prefer to poll for the data, instead of attaching a
        // listener, check for the state and progress in the observer.
        if (TransferState.COMPLETED == uploadObserver.getState()) {
            // Handle a completed upload.
        }

        Log.d("YourActivity", "Bytes Transferrred: " + uploadObserver.getBytesTransferred());
        Log.d("YourActivity", "Bytes Total: " + uploadObserver.getBytesTotal());
    }








}