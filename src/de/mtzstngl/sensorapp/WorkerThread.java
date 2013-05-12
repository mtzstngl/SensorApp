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

import android.content.Context;
import android.hardware.*;
import android.os.*;

public class WorkerThread extends HandlerThread
							implements Handler.Callback, SensorEventListener{
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	private SocketHandler mSocketHandler;
	private boolean connected;
	private int x, y, z;
	
	public WorkerThread(Context ctx, int priority) {
		super("WorkerThread", priority);
		
		mSocketHandler = new SocketHandler();
		connected = false;
		
		mSensorManager = (SensorManager) ctx.getSystemService(Context.SENSOR_SERVICE);
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		
	}
	
	public boolean connect(String host){
		if(!mSocketHandler.connect(host, 23456)){
			return false;
		}
		connected = true;
		
		return true;
	}
	
	public boolean disconnect(){
		connected = false;
		if(!mSocketHandler.disconnect()){
			return false;
		}
		
		return true;
	}
	
	public void onResume() { //resume the sensor callback
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
	
    public void onPause() { //stop the sensor callback so we don't drain the battery
        mSensorManager.unregisterListener(this);
    }
    
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
			x = (int) event.values[0]; //nobody cares about the decimal point
			y = (int) event.values[1];
			z = (int) event.values[2];
			
			if(connected){ //we have to be connected to actually send data
				String sensorValues = Integer.toString(x) + "|"
									+ Integer.toString(y) + "|"
									+ Integer.toString(z);
				if(!mSocketHandler.send(sensorValues)){
					connected = false; //stop trying to send data if the connection is lost
				}
			}
		}
	}
	
}
