package com.upik.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.upik.R;
import com.upik.VollySupport.AppController;

public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 2000;
    private TextView txt_Up,txt_ik;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen_layout);

        Log.e("SplashActivity","SplashActivity");


        txt_Up=findViewById(R.id.txt_Up);
        txt_ik=findViewById(R.id.txt_ik);
        txt_Up.setTypeface(AppController.logo_BPreplayBold);
        txt_ik.setTypeface(AppController.logo_BPreplayBold);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent mainIntent = new Intent(SplashActivity.this,nearby_job_activity.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
