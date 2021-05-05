package com.example.miniproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static int time_intent = 2000;
    TextView big_text;
    TextView small_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash_screen);

        big_text = findViewById(R.id.big_text);
        small_text = findViewById(R.id.small_text);

        Animation splash_down = AnimationUtils.loadAnimation(this, R.anim.animation_splash_down);

        big_text.startAnimation(splash_down);
        small_text.startAnimation(splash_down);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
                finish();
            }
        }, time_intent);
    }
}
