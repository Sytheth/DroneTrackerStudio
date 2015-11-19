package com.example.sytheth.dronetrackerstudio;

import android.Manifest;
import android.animation.AnimatorInflater;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.media.Image;
import android.media.ImageReader;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.Surface;
import android.view.TextureView;
import android.graphics.SurfaceTexture;
import android.util.Size;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import android.os.HandlerThread;
import android.os.Handler;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import javax.crypto.NoSuchPaddingException;
import javax.xml.transform.Result;


public class MainActivity extends Activity implements LocationListener {
    /**
     * @param location GPS location of the user
     */
    private AutoFitTextureView mTextureView = null;
    private CameraDevice mCameraDevice = null;
    private CaptureRequest.Builder mPreviewBuilder = null;
    private CameraCaptureSession mPreviewSession = null;
    private Size mPreviewSize = null;
    public Location location;

    /**
     * @param file File where the camera image will be saved.
     */
    final File file = new File(Environment.getExternalStorageDirectory()+"/DCIM/Camera", "IMG_Drone.jpg");


    // Create the Surface Texture Listener to grab the image from the camera and put it on the screen
    private TextureView.SurfaceTextureListener mSurfaceTextureListener = new TextureView.SurfaceTextureListener() {

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width,
                                                int height) {
            configureTransform(width, height);
            Toast.makeText(MainActivity.this,"CONFIGURED!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width,
                                              int height) {
            // Get the camera manager to set up the image
            CameraManager manager = (CameraManager) getSystemService(CAMERA_SERVICE);
            try {
                String cameraId = manager.getCameraIdList()[0];
                CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
                StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                mPreviewSize = map.getOutputSizes(ImageFormat.JPEG)[0];
                mTextureView.setAspectRatio(3,4);
                // Make sure the permissions for camera have been enabled
                if (checkCallingOrSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                    manager.openCamera(cameraId, mStateCallback, null);
                else{
                    Toast.makeText(MainActivity.this, "Camera Permissions Denied!", Toast.LENGTH_SHORT).show();
                }
            }
            catch(CameraAccessException e) {
                e.printStackTrace();
            }
        }
    };


    // Set up the callback for the camera device
    private CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {

            mCameraDevice = camera;
            SurfaceTexture texture = mTextureView.getSurfaceTexture();

            if (texture == null) {
                return;
            }

            texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
            Surface surface = new Surface(texture);

            // Get the current image
            try {
                mPreviewBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            } catch (CameraAccessException e){
                e.printStackTrace();
            }

            // Set target for preview window
            mPreviewBuilder.addTarget(surface);

            // Create capture session
            try {
                mCameraDevice.createCaptureSession(Arrays.asList(surface), mPreviewStateCallback, null);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(CameraDevice camera, int error) {
        }

        @Override
        public void onDisconnected(CameraDevice camera) {
        }
    };

    // Run camera updates on a separate thread
    private CameraCaptureSession.StateCallback mPreviewStateCallback = new CameraCaptureSession.StateCallback() {
        @Override
        public void onConfigured(CameraCaptureSession session) {
            mPreviewSession = session;
            mPreviewBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
            HandlerThread backgroundThread = new HandlerThread("CameraPreview");
            backgroundThread.start();
            Handler backgroundHandler = new Handler(backgroundThread.getLooper());

            try {
                mPreviewSession.setRepeatingRequest(mPreviewBuilder.build(), null, backgroundHandler);
            }
            catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onConfigureFailed(CameraCaptureSession session) {
            Toast.makeText(MainActivity.this, "Error02!",Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * Adds all information to the email besides the image attachment.
     * @param view User interface.
     */
    public void sendEmail(View view) throws NoSuchAlgorithmException, NoSuchPaddingException, IOException, InvalidKeyException
    {
        Hasher infoHasher = new Hasher(this.getApplicationContext().getFilesDir() + "/hast.txt");
        if(!infoHasher.exist())
        {
            //TODO Get info via GUI
            infoHasher.getInfo("gdcerau@gmail.com","NinjaTurtleSwag");
        }

        Email email = new Email(infoHasher.getStream());
        String[] toArr = {"croninstephen347@gmail.com"};
        email.setTo(toArr);
        email.setFrom("DroneyTracker@Droney.com");

        // Collect informaiton from GUI
        EditText editText = (EditText)findViewById(R.id.editText1);
        String description = editText.getText().toString();
        Calendar c = Calendar.getInstance();
        String dateTime = c.getTime().toString();

        // If location was found, add it to the subject line
        if (!location.getProvider().contentEquals("Test")){
            email.setSubject(description + "|" + dateTime + "|" +"Lat: "+location.getLatitude() + "|" + "Long: "+location.getLongitude());
        }
        else{
            email.setSubject(description + "|" + dateTime + "|" +" Location Unavailable");
        }
        email.setBody("");

        // Send the email
        AsyncTaskRunner runner = new AsyncTaskRunner();
        Object[] obarray = {email,file};
        runner.execute(obarray);

        // Reset camera *** to be added later
        Toast.makeText(MainActivity.this, "Email Sent!",Toast.LENGTH_SHORT).show();
    }

    /**
     * Saves the current frame from the camera as a .jpg.
     * @param view User interface.
     */
    public void takePhoto(View view){
        ImageView cameraBtn = (ImageView)findViewById(R.id.captureButton);
        cameraBtn.setBackgroundResource(R.drawable.camerabtnanim);
        AnimationDrawable cameraAnim = (AnimationDrawable)cameraBtn.getBackground();
        cameraAnim.start();


        // Get the GPS Location
        LocationManager locationMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationMan.getProvider("Test") == null) {
            locationMan.addTestProvider("Test", false, false, false, false, false, false, false, 0, 1);
        }
        locationMan.setTestProviderEnabled("Test", true);
        location = new Location("Test");
        location.setLatitude(0);
        location.setLongitude(0);
        location.setAltitude(0);
        location.setTime(System.currentTimeMillis());
        location.setAccuracy(0);
        location.setElapsedRealtimeNanos(1);

        locationMan.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        // Check to see if camera is open
        if(null == mCameraDevice) {
            return;
        }
        // Capture the picture
        try {
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(mCameraDevice.getId());
            Size[] jpegSizes = null;

            // Set up camera type
            if (characteristics != null) {
                jpegSizes = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).getOutputSizes(ImageFormat.JPEG);
        }

            // This does NOT set the image size, but it is a guess if all else fails
            int width = 480;
            int height = 480;

            // Choose the best quality image available from the camera
            if (jpegSizes != null && 0 < jpegSizes.length) {
                width = jpegSizes[0].getWidth();
                height = jpegSizes[0].getHeight();
            }

            // Read the image from the camera
            ImageReader reader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1);
            List<Surface> outputSurfaces = new ArrayList<Surface>(2);
            outputSurfaces.add(reader.getSurface());
            outputSurfaces.add(new Surface(mTextureView.getSurfaceTexture()));
            final CaptureRequest.Builder captureBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(reader.getSurface());
            captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_SCENE_MODE_STEADYPHOTO);
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, 90);

            // Set up a image available listener
            ImageReader.OnImageAvailableListener readerListener = new ImageReader.OnImageAvailableListener() {

                @Override
                public void onImageAvailable(ImageReader reader) {
                    Image image = null;
                    // Capture the image
                    try {
                        image = reader.acquireLatestImage();
                        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                        byte[] bytes = new byte[buffer.capacity()];
                        buffer.get(bytes);
                        // Save the image using the save method
                        save(bytes);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (image != null) {
                            image.close();
                            // Wait 20 seconds to time out if GPS location could not be found
                            int count=0;


                            while (count < 200 & location.getProvider().equals("Test")){
                                count++;

                                SystemClock.sleep(100);
                            }
                            // GEO-tag the image if location was found
                            if (!location.getProvider().equals("Test")){

                                loc2Exif(file.getPath(), location);
                            }
                            else{
                                LocationManager locationMan2 = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
                                locationMan2.setTestProviderLocation("Test", location);
                                onLocationChanged(location);
                                Toast.makeText(MainActivity.this, "Failed to find location!", Toast.LENGTH_SHORT).show();
                            }
                            // Allow the picture to be sent
                        }
                    }
                }

                // Save the image
                private void save(byte[] bytes) throws IOException {
                    OutputStream output = null;
                    try {
                        output = new FileOutputStream(file);
                        // Crop the image
                        Bitmap bmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, null);
                        Bitmap cropped = Bitmap.createBitmap(bmap,0,0,bmap.getWidth(),bmap.getWidth());
                        Bitmap cropped2 = Bitmap.createScaledBitmap(cropped, 480, 480, true);
                        cropped2.compress(Bitmap.CompressFormat.JPEG, 85, output);

                    } finally {
                        if (null != output) {
                            output.close();
                        }
                    }
                }
            };

            // Run the camera capture on a separate thread
            HandlerThread thread = new HandlerThread("CameraPicture");
            thread.start();
            final Handler backgroudHandler = new Handler(thread.getLooper());
            reader.setOnImageAvailableListener(readerListener, backgroudHandler);

            // Create a capture listener for when the image has been saved
            final CameraCaptureSession.CaptureCallback captureListener = new CameraCaptureSession.CaptureCallback() {

                @Override
                public void onCaptureCompleted(CameraCaptureSession session,
                                               CaptureRequest request, TotalCaptureResult result) {
                    super.onCaptureCompleted(session, request, result);
                    Toast.makeText(MainActivity.this, "Saved:"+file, Toast.LENGTH_SHORT).show();
                }

            };

            // start the capture session
            mCameraDevice.createCaptureSession(outputSurfaces, new CameraCaptureSession.StateCallback() {

                @Override
                public void onConfigured(CameraCaptureSession session) {

                    try {
                        session.capture(captureBuilder.build(), captureListener, backgroudHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession session) {

                }
            }, backgroudHandler);

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }

    /**
     * Sets the location of the user.
     * @param loc The new location, as a Location object.
     */
    @Override
    public void onLocationChanged(Location loc){
        MainActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                ImageView cameraBtn = (ImageView) findViewById(R.id.captureButton);
                cameraBtn.setBackgroundResource(R.drawable.retake);
            }
        });

        location = loc;
    }
    /**
     * @param provider The name of the location provider associated with this update.
     * @param status 0 if the provider is out of service, and this is not expected to change in the near future; 1 if the provider is temporarily unavailable but is expected to be available shortly; and 2 if the provider is currently available.
     * @param extras An optional Bundle which will contain provider specific status variables.
     */
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
    /**
     * @param provider The name of the location provider associated with this update.
     */
    @Override
    public void onProviderEnabled(String provider) {
    }
    /**
     * @param provider The name of the location provider associated with this update.
     */
    @Override
    public void onProviderDisabled(String provider) {
    }



    /**
     * Encodes the GPS location into the image.
     * @param flNm Filename of the .jpg to be encoded
     * @param loc Current location of the user.
     */
    // GEO-Tag the image
    //http://stackoverflow.com/questions/10531544/write-geotag-jpegs-exif-data-in-android
    public void loc2Exif(String flNm, Location loc) {

        try {
            ExifInterface ef = new ExifInterface(flNm);
            ef.setAttribute(ExifInterface.TAG_GPS_LATITUDE, dec2DMS(loc.getLatitude()));
            ef.setAttribute(ExifInterface.TAG_GPS_LONGITUDE,dec2DMS(loc.getLongitude()));
            if (loc.getLatitude() > 0)
                ef.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, "N");
            else
                ef.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, "S");
            if (loc.getLongitude()>0)
                ef.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, "E");
            else
                ef.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, "W");
            ef.saveAttributes();
            Toast.makeText(MainActivity.this, "Location Saved!", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {}
    }

    /**
     * Reformats GPS data into string form.
     * @param coord Coordinates of the user.
     * @return
     */
    // Reformat the GPS Data
    String dec2DMS(double coord) {
        coord = coord > 0 ? coord : -coord;  // -105.9876543 -> 105.9876543
        String sOut = Integer.toString((int)coord) + "/1,";   // 105/1,
        coord = (coord % 1) * 60;         // .987654321 * 60 = 59.259258
        sOut = sOut + Integer.toString((int)coord) + "/1,";   // 105/1,59/1,
        coord = (coord % 1) * 60000;             // .259258 * 60000 = 15555
        sOut = sOut + Integer.toString((int)coord) + "/1000";   // 105/1,59/1,15555/1000
        return sOut;
    }

    /**
     * Called when the activity is starting.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        mTextureView = (AutoFitTextureView) findViewById(R.id.textureView1);
        mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);
    }

    /**
     * Method for handling when the app minimizied while running.
     */
    @Override
    public void onPause(){
        // Close the camera when paused
        super.onPause();
        if (mCameraDevice != null)
        {
            mCameraDevice.close();
            mCameraDevice = null;
        }
    }


    /**
     * Configures the necessary {@link android.graphics.Matrix} transformation to `mTextureView`.
     * This method should be called after the camera preview size is determined in
     * setUpCameraOutputs and also the size of `mTextureView` is fixed.
     *
     * @param viewWidth  The width of `mTextureView`
     * @param viewHeight The height of `mTextureView`
     */
    private void configureTransform(int viewWidth, int viewHeight) {
        if (null == mTextureView || null == mPreviewSize) {
            return;
        }
        int rotation = MainActivity.this.getWindowManager().getDefaultDisplay().getRotation();
        Matrix matrix = new Matrix();
        RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
        RectF bufferRect = new RectF(0, 0, mPreviewSize.getHeight(), mPreviewSize.getWidth());
        float centerX = viewRect.centerX();
        float centerY = viewRect.centerY();
        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
            float scale = Math.max(
                    (float) viewHeight / mPreviewSize.getHeight(),
                    (float) viewWidth / mPreviewSize.getWidth());
            matrix.postScale(scale, scale, centerX, centerY);
            matrix.postRotate(90 * (rotation - 2), centerX, centerY);
        } else if (Surface.ROTATION_180 == rotation) {
            matrix.postRotate(180, centerX, centerY);
        }
        mTextureView.setTransform(matrix);
    }
}

