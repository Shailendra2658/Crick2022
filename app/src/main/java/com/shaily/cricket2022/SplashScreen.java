package com.shaily.cricket2022;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by Shailedra on 26/12/2018.
 */
public class SplashScreen extends Activity {
    private static final String TAG = "SplashScreen";
    public static String BOWL_TYPE = "";
    public static String LEVEL = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash);

     /*   Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(2000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    int used = checkLaunchCount();
                    if(used>10000){
                      //  finish();
                    }
                    Intent intent = new Intent(SplashScreen.this,MainActivity.class);
                    //Change the activity.
                    //intent.putExtra(EXTRA_ADDRESS, "");
                    startActivity(intent);
                }
            }
        };
        timerThread.start();*/

        ((ImageView) findViewById(R.id.imageView2)).startAnimation(AnimationUtils.loadAnimation(this, R.anim.side_slide));
        new Thread() {
            public void run() {
                Intent intent;
                try {
                    sleep(3000);
                    intent = new Intent(SplashScreen.this, DashBoardActivity.class);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    intent = new Intent(SplashScreen.this, DashBoardActivity.class);
                } catch (Throwable th) {
                    SplashScreen.this.startActivity(new Intent(SplashScreen.this, DashBoardActivity.class));
                    throw th;
                }
                SplashScreen.this.startActivity(intent);
            }
        }.start();
    }

    private int checkLaunchCount() {
        String data = readFromFile(getApplicationContext());
        Log.d(TAG,"readData "+data);
        int count = Integer.parseInt(data);
        count++;
        Log.d(TAG,"writeToFile "+count);
        writeToFile(""+count, getApplicationContext());
        return count;
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }

    private String readFromFile(Context context) {

        String ret = "1";

        try {
            InputStream inputStream = context.openFileInput("config.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }


    private void writeToFile(String data,Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("config.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

}
