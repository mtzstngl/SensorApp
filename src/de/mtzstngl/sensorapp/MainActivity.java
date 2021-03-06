/*
* Copyright (c) <2013> <Matthias Stangl>
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in
* all copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
* THE SOFTWARE.
*/
package de.mtzstngl.sensorapp;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener,
    SensorEventListener {
  private SensorManager mSensorManager;
  private Sensor mAccelerometer;
  private boolean connected = false;
  private TextView viewX, viewY, viewZ;

  private SocketHandler sHandler;
  private Data<Float> dataX, dataY, dataZ;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    viewX = (TextView) findViewById(R.id.textX);
    viewY = (TextView) findViewById(R.id.textY);
    viewZ = (TextView) findViewById(R.id.textZ);

    mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

    dataX = new Data<Float>(0F);
    dataY = new Data<Float>(0F);
    dataZ = new Data<Float>(0F);

    this.getWindow().addFlags(LayoutParams.FLAG_KEEP_SCREEN_ON); //keep the screen on and the values sending
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
  public void onSensorChanged(SensorEvent event) {
    if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
      viewX.setText("X: " + Float.toString(event.values[0]));
      viewY.setText("Y: " + Float.toString(event.values[1]));
      viewZ.setText("Z: " + Float.toString(event.values[2]));

      if(connected){
        dataX.setData(event.values[0]);
        dataY.setData(event.values[1]);
        dataZ.setData(event.values[2]);
      }
    }
  }

  @Override
  public void onClick(View v) {
    Button bConnect = (Button) findViewById(v.getId());
    EditText input = (EditText) findViewById(R.id.ipInput);
    RadioButton radioPlayerOne = (RadioButton) findViewById(R.id.radioPlayerOne);
    RadioButton radioPlayerTwo = (RadioButton) findViewById(R.id.radioPlayerTwo);
    
    if(connected){
      bConnect.setText(R.string.stringButtonConnect);
      input.setEnabled(true);
      radioPlayerOne.setEnabled(true);
      radioPlayerTwo.setEnabled(true);
      connected = false;
      try{
        sHandler.stopSocket();
        sHandler.join();
      }catch(InterruptedException e){
        e.printStackTrace();
      }
    }else{
      bConnect.setText(R.string.stringButtonDisconnect);
      input.setEnabled(false);
      radioPlayerOne.setEnabled(false);
      radioPlayerTwo.setEnabled(false);
      connected = true;
      if(radioPlayerOne.isChecked()){
        sHandler = new SocketHandler(input.getText().toString(), 23456, dataX, dataY, dataZ);
      }else{
        sHandler = new SocketHandler(input.getText().toString(), 23457, dataX, dataY, dataZ);
      }
      sHandler.start();
    }
  }
}
