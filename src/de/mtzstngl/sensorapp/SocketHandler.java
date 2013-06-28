/*
 *Copyright (C) 2013 by Matthias Stangl
 *
 *Permission is hereby granted, free of charge, to any person obtaining a copy
 *of this software and associated documentation files (the "Software"), to deal
 *in the Software without restriction, including without limitation the rights
 *to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *copies of the Software, and to permit persons to whom the Software is
 *furnished to do so, subject to the following conditions:
 *
 *The above copyright notice and this permission notice shall be included in
 *all copies or substantial portions of the Software.
 *
 *THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *THE SOFTWARE.
 */
package de.mtzstngl.sensorapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import android.util.Log;

public class SocketHandler extends Thread {
  private boolean running;
  private String ip;
  private int port;
  private Data<Float> x, y, z;

  private Socket socket;
  private PrintWriter out;
  private BufferedReader in;
  private static String SEPARATOR = "<|>";

  public SocketHandler(String ip, int port, Data<Float> x, Data<Float> y, Data<Float> z){
    running = true;
    this.ip = ip;
    this.port = port;
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public void run(){
    try{
      socket = new Socket(ip, port);
      in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      out = new PrintWriter(socket.getOutputStream());

      while(running){
        write(x.getData() + SEPARATOR + y.getData() + SEPARATOR + z.getData());
        if(read() == null){
          Log.d("TCP ERROR", "broken pipe");
          stopSocket();
        }
      }

      socket.close();
    }catch(Exception e){
      Log.d("TCP ERROR", "Host not available OR error while write, read");
      e.printStackTrace();
    }
  }

  public void setHost(String host){
    ip = host;
  }
  
  public void stopSocket(){
    running = false;
  }

  private void write(String message){
    out.println(message);
    out.flush();
  }

  private String read() throws IOException{
    return in.readLine();
  }
}
