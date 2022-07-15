/*
Chris Shaw
Mobile Programming
3/18/20
Rollerball App
*/
package edu.lwtech.rollerball;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import edu.lwtech.rollerball.R;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private RollerSurfaceView mSufaceView;
    private final int SHAKE_THRESHOLD = 100;
    private float mLastAcceleration = SensorManager.GRAVITY_EARTH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mSufaceView = findViewById(R.id.rollerSurface);

        // For testing in the emulator
        mSufaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSufaceView.shake();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this, mAccelerometer);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        // Get accelerometer values
        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];
        float z = sensorEvent.values[2];

        // Move the ball
        mSufaceView.changeAcceleration(x, y);

        float currentAcceleration = x * x + y * y + z * z;

        // Calculate difference between 2 readings
        float delta = currentAcceleration - mLastAcceleration;

        // Save for next time
        mLastAcceleration = currentAcceleration;

        // Detect shake
        if (Math.abs(delta) > SHAKE_THRESHOLD) {
            mSufaceView.shake();
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }
}
