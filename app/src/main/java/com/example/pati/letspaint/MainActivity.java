package com.example.pati.letspaint;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "tag";
    public static int color= Color.argb(0xFF,0xFF, 0x40,0x89);//FF4089
    public static float strokeWidth= 5;
    public static boolean clear = false;
    private EditText strokeWidthEdit;
    private View drawnView;
    private Canvas canva2=null;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        strokeWidthEdit=  findViewById(R.id.strokeWidhtEdit);
        strokeWidthEdit.addTextChangedListener(watcher);

    }


    public  TextWatcher watcher= new TextWatcher (){


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if(strokeWidthEdit.getText().toString().trim().length()!=0){strokeWidth=Integer.parseInt(strokeWidthEdit.getText().toString());}
            else
            {
                Toast.makeText(getApplicationContext(),"Set width", Toast.LENGTH_SHORT);
            }
        }

        @Override
        public void afterTextChanged(Editable s) throws InvalidParameterException {

            if(strokeWidthEdit.getText().toString().trim().length()!=0){strokeWidth=Integer.parseInt(strokeWidthEdit.getText().toString());}
            else
            {
                Toast.makeText(getApplicationContext(),"Set width", Toast.LENGTH_SHORT);
            }


        }

    };

    public void onClickGreen(View view) {//77f949
        color=Color.argb(0xFF,0x77, 0xf9,0x49);
    }

    public void onClickPink(View view) {
        color=Color.argb(0xFF,0xFF, 0x40,0x89);
    }

    public void onClickYellow(View view) {//aada48
        color=Color.argb(0xFF,0xff, 0xff,0x00);
    }

    public void onClickBlue(View view) {//5544ff
        color=Color.argb(0xFF,0x55, 0x44,0xff);
    }

    public void onClickClear(View view) {
        clear=true;
        PaintingSurface.canvas.drawARGB(255, 255, 255, 255);
    }

    private  File getOutputMediaFile(){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/Files");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName="MI_"+ timeStamp +".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }

    private void storeImage(Bitmap image) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Log.d(TAG,
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
    }
    public  Bitmap getBitmap()
    {

        drawnView.setDrawingCacheEnabled(true);
        drawnView.buildDrawingCache();
        Bitmap bmp = Bitmap.createBitmap(drawnView.getDrawingCache());
        drawnView.setDrawingCacheEnabled(false);

        return bmp;
    }


    public void onClickSave(View view) {

        drawnView=  findViewById(R.id.painting_surface);
        Bitmap myBitmap = drawnView.getDrawingCache();
        storeImage(myBitmap);


        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_window, null);
        //Toast.makeText(getApplicationContext()," ", Toast.LENGTH_SHORT).show();
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();
    }

}
