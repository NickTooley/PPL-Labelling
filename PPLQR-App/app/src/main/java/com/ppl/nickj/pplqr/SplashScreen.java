package com.ppl.nickj.pplqr;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.ppl.nickj.pplqr.db.AppDatabase;
import com.ppl.nickj.pplqr.db.Product;
import com.ppl.nickj.pplqr.db.dbinit;

import java.util.List;

public class SplashScreen extends AppCompatActivity {
    public boolean animFinished;
    public boolean dbLoadFinished;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            //w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

//        while(ContextCompat.checkSelfPermission(SplashScreen.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SplashScreen.this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.INTERNET},
                    1);


    }

    public void onRequestPermissionsResult(
            int requestCode,
            String[] permissions,
            int[] grantResults
    ){
        Thread timer = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(500);
                    Intent intent = new Intent(getApplicationContext(), QRScanner.class);
                    startActivity(intent);
                    finish();
                    super.run();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        timer.start();
    }




}
