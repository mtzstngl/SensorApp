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

import java.net.*;
import java.io.*;
import android.util.Log;

public class SocketHandler {
	private Socket socket = null;
	private BufferedReader socketReader;
	private BufferedWriter socketWriter;
	private String newline;
	
	public SocketHandler() {
		newline = "\n"; // work around normally use System.lineSeparator();
	}
	
	public boolean connect(String host, int port){
//TODO:		//if( (socket == null) || (socket.isConnected() == false) )
		try {
			socket = new Socket(host, port);
		} catch (UnknownHostException e) {
			Log.d("TCP Error", "Unknown host");
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			Log.d("TCP Error", "IOException while Socket");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean disconnect(){
		try{
			socket.close();
		}catch(IOException e){
			e.printStackTrace();
			return false;
		}catch(NullPointerException e){
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean send(String message){
		try {
			_write(message);
			if(_read() == null){
				Log.d("TCP Error", "broken pipe");
				return false;
			}
		} catch (IOException e) {
			Log.d("TCP Error", "IOException while _write OR _read");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	
	private void _write(String message) throws IOException{
		socketWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		socketWriter.write(message + newline); //append newline. otherwise the sever would wait forever
		socketWriter.flush();
	}
	
	private String _read() throws IOException {
		socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		return socketReader.readLine();
	}
}