package com.example.galleryversionone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            Log.v("main activity", "Permission is granted");
        } else {
            Log.v("main activity", "Permission is revoked");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.main_fragment, new ImageFragment()).commit();
        }
    }

//    public void showImages(View view) {
//        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, ImageFragment.newInstance()).commit();
//    }
//
//    public void showVideos(View view) {
//        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, VideoFragment.newInstance()).commit();
//
//    }

    public void sendImages(View view){
        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, SendFilesFragment.newInstance()).addToBackStack(null).commit();
    }

    public void receiveImages(View view){

    }
}