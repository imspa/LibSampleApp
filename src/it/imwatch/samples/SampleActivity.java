package it.imwatch.samples;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;
import it.imwatch.SensorService;
import it.imwatch.SensorServiceInstance;
import it.imwatch.SimpleShakeDetector;

public class SampleActivity extends Activity {

    SimpleShakeDetector mShakeDetector;
    SensorService mSensors;

    final static int UPDATE_VIEWS_INTERVAL = 1000;

    TextView accX, accY, accZ, rotX, rotY, rotZ;

    Handler handler = new Handler();
    Runnable updateViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        mShakeDetector = new SimpleShakeDetector(this, new SimpleShakeDetector.OnShakeListener() {

            @Override
            public void onShakeDetected(int steps) {
                Toast.makeText(SampleActivity.this, "Shake Detected", Toast.LENGTH_SHORT).show();
            }

        }, SimpleShakeDetector.DEFAULT_UPDATE_INTERVAL);

        mSensors = new SensorServiceInstance(100);

        //		Change parameters to customize shake detection.
        //
        //		mShakeDetector.setMinGestureSize(14);
        //		mShakeDetector.setNoMovement(5);
        //		mShakeDetector.setThreshold(10f);
        //		mShakeDetector.setUpdateInterval(10);


        // stuff to update the view
        accX = (TextView) findViewById(R.id.accelX);
        accY = (TextView) findViewById(R.id.accelY);
        accZ = (TextView) findViewById(R.id.accelZ);
        rotX = (TextView) findViewById(R.id.rotX);
        rotY = (TextView) findViewById(R.id.rotY);
        rotZ = (TextView) findViewById(R.id.rotZ);

        updateViews = new Runnable() {

            @Override
            public void run() {
                accX.setText(String.format("%1.4f", mSensors.getAccelVector().X));
                accY.setText(String.format("%1.4f", mSensors.getAccelVector().Y));
                accZ.setText(String.format("%1.4f", mSensors.getAccelVector().Z));
                rotX.setText(String.format("%5.1f", mSensors.getRotationVector().X));
                rotY.setText(String.format("%5.1f", mSensors.getRotationVector().Y));
                rotZ.setText(String.format("%5.1f", mSensors.getRotationVector().Z));
                handler.postDelayed(updateViews, UPDATE_VIEWS_INTERVAL);
            }
        };

        handler.postDelayed(updateViews, UPDATE_VIEWS_INTERVAL);
    }

    @Override
    public void onPause() {
        super.onPause();

        mShakeDetector.onPause();
        mSensors.onPause();

        handler.removeCallbacks(updateViews);
    }

    @Override
    public void onResume() {
        super.onResume();

        mShakeDetector.onResume();
        mSensors.onResume();

        handler.postDelayed(updateViews, UPDATE_VIEWS_INTERVAL);
    }

}
