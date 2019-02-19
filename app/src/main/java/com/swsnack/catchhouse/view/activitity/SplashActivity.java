package com.swsnack.catchhouse.view.activitity;

import android.content.Intent;
import android.os.Bundle;

import com.swsnack.catchhouse.R;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent mainIntent = new Intent(SplashActivity.this, HomeActivity.class);
        SplashActivity.this.startActivity(mainIntent);
        SplashActivity.this.finish();

    }
}
