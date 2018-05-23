package com.example.pati.letspaint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Pati on 04.05.2018.
 */

public class PaintingSurface extends SurfaceView implements SurfaceHolder.Callback, Runnable{
private boolean isThreadWorking= false;
private Object blockade= new Object();
private SurfaceHolder surfaceHolder;
private Thread paintingThread;
private Paint paint = new Paint();
private float moveX=0;
private float moveY=0;
private float startX=0;
private float startY=0;
private float previousX=0;
private float previousY=0;
private Bitmap bitmap= null;
public static Canvas canvas= null;

    public PaintingSurface(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
    }
    public void resumePainting(){
        paintingThread= new Thread(this);
        isThreadWorking=true;
        paintingThread.start();
    }
    public void pausePainting(){
        isThreadWorking=false;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        performClick();
        synchronized (blockade){
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    pausePainting();
                    resumePainting();
                    startX=event.getX();
                    startY=event.getY();
                    paint.setColor(MainActivity.color);
                    paint.setStrokeWidth(MainActivity.strokeWidth);
                    canvas.drawCircle(startX, startY, 10, paint);
                    previousX=startX;
                    previousY=startY;
                    return true;

                case MotionEvent.ACTION_UP:
                    canvas.drawCircle(previousX, previousY, 10, paint);
                    return true;

                case MotionEvent.ACTION_MOVE:
                    moveX=event.getX();
                    moveY= event.getY();
                    canvas.drawLine(previousX, previousY, moveX, moveY, paint);
                    previousX= moveX;
                    previousY=moveY;
                    return true;
                default:
                    break;
            }
        }
        return false;

    }


    @Override
    public boolean performClick(){
        return super.performClick();
    }

    @Override
    public void run(){
      while(isThreadWorking){
          Canvas canvas = null;

          try {
              synchronized (surfaceHolder) {
                  if (!surfaceHolder.getSurface().isValid()) {
                      continue;
                  }
                  canvas = surfaceHolder.lockCanvas(null);

                  synchronized (blockade) {
                      if (isThreadWorking) {
                          if (MainActivity.clear == true) {

                              canvas.drawARGB(255, 255, 255, 255);
                              MainActivity.clear = false;
                          }
                          canvas.drawBitmap(bitmap, 0,0,null);
                      }
                  }
              }
          }
          finally
          {
                if(canvas!= null)
                {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
          }
          try
              {
                  Thread.sleep(1000/25);
              }
              catch(InterruptedException e){}

      }

    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        bitmap=Bitmap.createBitmap(getWidth(),getHeight(),Bitmap.Config.ARGB_8888);
        canvas=new Canvas(bitmap);
        canvas.drawARGB(255,255,255,255);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }


}
