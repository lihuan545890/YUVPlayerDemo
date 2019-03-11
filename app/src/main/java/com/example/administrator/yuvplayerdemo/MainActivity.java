package com.example.administrator.yuvplayerdemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends Activity {
    private GLSurface mglsuface = null;
    private MyGLRender mrender = null;
    int width = 640;
    int height = 360;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mglsuface = (GLSurface)findViewById(R.id.preview);
        mrender = new MyGLRender(mglsuface);
        mglsuface.setRenderer(mrender);

        mrender.update(width, height);
        new Thread(){
            @Override
            public void run() {
                super.run();

                File yuvFile = new File("/sdcard/test.yuv");
                FileInputStream fis = null;
                try {
                    fis   = new FileInputStream(yuvFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                int size = width * height * 3 / 2;

                byte[] input = new byte[size];
                int hasRead = 0;
                while (true){

                    try {
                        hasRead = fis.read(input);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (hasRead == -1){
                        break;
                    }

                    mrender.update(input);
                    Log.i("thread", "thread is executing hasRead: " + hasRead);
                    try {
                        sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
}
