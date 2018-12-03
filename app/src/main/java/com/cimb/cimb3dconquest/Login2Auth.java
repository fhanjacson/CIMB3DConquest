package com.cimb.cimb3dconquest;

import android.content.Intent;
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

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClient;
import com.amazonaws.services.rekognition.model.BoundingBox;
import com.amazonaws.services.rekognition.model.CompareFacesMatch;
import com.amazonaws.services.rekognition.model.CompareFacesRequest;
import com.amazonaws.services.rekognition.model.CompareFacesResult;
import com.amazonaws.services.rekognition.model.ComparedFace;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.S3Object;
import com.amazonaws.util.IOUtils;
import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Login2Auth extends Register_Pattern  implements SurfaceHolder.Callback{
    Camera camera;
    SurfaceView sViewCamera;
    SurfaceHolder surfaceHolder;
    Camera.PictureCallback rawCallback;
    Camera.ShutterCallback shutterCallback;
    Camera.PictureCallback jpegCallback;
    Button start, compare, capture;
    BasicAWSCredentials awscred = new BasicAWSCredentials("yourIAMAccessKey", "ourIAMSecretKey");
    Boolean patterncorrect = false;

//    LoginActivity loginActivity = new LoginActivity();
    public CompareFace cf = new CompareFace(false, LoginActivity.user.getUsername()+".jpg",LoginActivity.user.getUsername()+"-compare"+".jpg" ,"s3-cimb");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_2auth);



        start = (Button)findViewById(R.id.btnStart);
        start.setOnClickListener(new Button.OnClickListener()
        {
            public void onClick(View arg0) {
                start_camera();
            }
        });

        capture = (Button)findViewById(R.id.btnCapture);
        capture.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View arg0){
                captureImage();
            }
        });


        compare = (Button)findViewById(R.id.btnCompare);
        compare.setOnClickListener(new Button.OnClickListener()
        {
            public void onClick(View arg0) {
                compare();
            }
        });

        capture.setEnabled(false);
        compare.setEnabled(false);

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
                    outStream = new FileOutputStream(String.format(Locale.ENGLISH , Environment.getExternalStorageDirectory().getPath()+ "/DCIM/"+LoginActivity.user.getUsername()+"-compare.jpg", System.currentTimeMillis()));
                    outStream.write(data);
                    outStream.close();
                    Log.d("Log", "onPictureTaken - wrote bytes: " + data.length);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d("Log", "onPictureTaken - jpeg");
            }
        };


        final String save_pattern = Paper.book().read(save_pattern_key);
        mPatternLockView = (PatternLockView) findViewById(R.id.lockPattern);
        mPatternLockView.addPatternLockListener(new PatternLockViewListener() {
            @Override
            public void onStarted() {

            }

            @Override
            public void onProgress(List<PatternLockView.Dot> progressPattern) {

            }

            @Override
            public void onComplete(List<PatternLockView.Dot> pattern) {
                confirmation_pattern = PatternLockUtils.patternToString(mPatternLockView, pattern);
                if (confirmation_pattern.equals(save_pattern)) {
                    Log.d("onComplete", save_pattern);
                    Toast.makeText(Login2Auth.this, "Password Correct", Toast.LENGTH_SHORT).show();
                    patterncorrect = true;
                } else {
                    Log.d("onComplete", confirmation_pattern);
                    Toast.makeText(Login2Auth.this, "Password Incorrect!!!", Toast.LENGTH_SHORT).show();
                    patterncorrect = false;
                }
            }

            @Override
            public void onCleared() {

            }

        });

    }

    private void start_camera(){
        start.setEnabled(false);
        try{
            camera = Camera.open(1);
        }catch(RuntimeException e){
            Log.e("My Error", "init_camera: " + e);
            return;
        }
        Camera.Parameters param;
        param = camera.getParameters();
        param.setPreviewFrameRate(30);
        param.setRotation(270);
        param.setPictureSize(1280,720);
        param.setPreviewSize(1280 , 720);

        camera.setParameters(param);
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.setDisplayOrientation(90);
            camera.startPreview();
        } catch (Exception e) {
            Log.e("My Error", "init_camera: " + e);
        } finally {
            capture.setEnabled(true);
        }
    }

    private void captureImage() {
        capture.setEnabled(false);
        camera.setDisplayOrientation(90);
        camera.takePicture(shutterCallback, rawCallback, jpegCallback);
        Toast.makeText(this,LoginActivity.user.getUsername()+"-compare.jpg", Toast.LENGTH_LONG).show();
        compare.setEnabled(true);
    }


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

    private void compare(){
        compare.setEnabled(false);
        camera.stopPreview();
        camera.release();
        Toast.makeText(this, LoginActivity.user.getUsername()+"-compare.jpg", Toast.LENGTH_LONG).show();

        Runnable runnable = new Runnable() {
            public void run() {

                String photo1 = cf.getPic1();
                String photo2 = Environment.getExternalStorageDirectory().getPath()+ "/DCIM/"+ LoginActivity.user.getUsername() +"-compare.jpg";
                String bucket = cf.getBucket();


                AmazonRekognition rekognitionClient = new AmazonRekognitionClient(awscred);
                rekognitionClient.setRegion(com.amazonaws.regions.Region.getRegion(Regions.AP_NORTHEAST_1));

                ByteBuffer targetImageBytes=null;
                try (InputStream inputStream = new FileInputStream(new File(photo2))) {
                    targetImageBytes = ByteBuffer.wrap(IOUtils.toByteArray(inputStream));
                }
                catch(Exception e)
                {
                    Log.d("COMPARE FACE","Failed to load target images: " + photo2);
                    System.exit(1);
                }


                Image target=new Image()
                        .withBytes(targetImageBytes);


                CompareFacesRequest compareFacesRequest = new CompareFacesRequest().withSourceImage(new Image()
                        .withS3Object(new S3Object()
                                .withName(photo1).withBucket(bucket))).withTargetImage(target).withSimilarityThreshold(80F);
                // Call operation
                CompareFacesResult compareFacesResult=rekognitionClient.compareFaces(compareFacesRequest);


                // Display results
                List<CompareFacesMatch> faceDetails = compareFacesResult.getFaceMatches();
                boolean result = false;
                for (CompareFacesMatch match: faceDetails){
                    ComparedFace face= match.getFace();
                    BoundingBox position = face.getBoundingBox();
                    Log.d("COMPARE FACE","Face at " + position.getLeft().toString()
                            + " " + position.getTop()
                            + " matches with " + face.getConfidence().toString()
                            + "% confidence.");

                    result = face.getConfidence() > 95;
                }

                List<ComparedFace> uncompared = compareFacesResult.getUnmatchedFaces();

                Log.d("COMPARE FACE","There was " + uncompared.size()
                        + " face(s) that did not match");
                Log.d("COMPARE FACE","Source image rotation: " + compareFacesResult.getSourceImageOrientationCorrection());
                Log.d("COMPARE FACE","target image rotation: " + compareFacesResult.getTargetImageOrientationCorrection());
                cf.setResult(result);

                if(cf.isResult()){
                    Log.d("Verification","CF Value is TRUE");
                }
                else{
                    Log.d("Verification","CF Value is FALSE");
                }
            }
        };
        Thread mythread = new Thread(runnable);
        mythread.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                verify();
            }
        }, 3000);

        start.setEnabled(true);

    }


    void verify(){
        if(cf.isResult() && patterncorrect){
            Toast.makeText(this,"Identity Verified", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent( Login2Auth.this, MainMenuActivity.class);
            startActivity(intent);

        }else {
            Toast.makeText(this,"Identity Cannot be Verified", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
