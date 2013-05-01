package de.mtzstngl.sensorapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.app.Activity;
import android.content.Context;
import android.hardware.*;

public class MainActivity extends Activity implements SensorEventListener, View.OnClickListener{
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	private TextView viewX, viewY, viewZ;
	int x, y, z;
	boolean connected = false;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        viewX = (TextView) findViewById(R.id.textX);
        viewY = (TextView) findViewById(R.id.textY);
        viewZ = (TextView) findViewById(R.id.textZ);
      
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }
    
	@Override
	public void onSensorChanged(SensorEvent event)
	{
		if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
			x = (int) event.values[0];
			y = (int) event.values[1];
			z = (int) event.values[2];
			
			viewX.setText("X: " + Integer.toString(x));
			viewY.setText("Y: " + Integer.toString(y));
			viewZ.setText("Z: " + Integer.toString(z));
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
	
	@Override
    protected void onPause() {
    	super.onPause();
        mSensorManager.unregisterListener(this);
    }
    
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

	@Override
	public void onClick(View v) {
		if(connected){
			connected = false;
			Button bConnect = (Button) findViewById(v.getId());
			bConnect.setText(R.string.stringButtonConnect);
			this.onResume();
		}else{
			connected = true;
			Button bConnect = (Button) findViewById(v.getId());
			bConnect.setText(R.string.stringButtonDisconnect);
			this.onPause();
		}
	}
}