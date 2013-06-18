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

public class Data{
  private float x, y, z;

  public Data(float x, float y, float z){
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public synchronized void setAll(float x, float y, float z){
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public synchronized void setX(float x){
    this.x = x;
  }

  public synchronized void setY(float y){
    this.y = y;
  }

  public synchronized void setZ(float z){
    this.z = z;
  }

  public synchronized float[] getAll(){
    float[] ret = new float[] {x, y, z};
    return ret;
  }

  public synchronized float getX(){
    return x;
  }

  public synchronized float getY(){
    return y;
  }

  public synchronized float getZ(){
    return z;
  }
}
