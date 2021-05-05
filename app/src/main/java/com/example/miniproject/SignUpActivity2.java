package com.example.miniproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class SignUpActivity2 extends AppCompatActivity {

    Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2);

        next = findViewById(R.id.next);

        next.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity2.this, MainActivity.class);
            SignUpActivity2.this.startActivity(intent);
            overridePendingTransition(R.anim.slide_left, R.anim.slide_out_right);
            finish();

        });
    }
}