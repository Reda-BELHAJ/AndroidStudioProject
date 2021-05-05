package com.example.miniproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import io.realm.Realm;

public class Forget3Activity extends AppCompatActivity {

    Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget3);

        next = findViewById(R.id.next);

        next.setOnClickListener(v -> {
            Intent intent = new Intent(Forget3Activity.this, MainActivity.class);
            Forget3Activity.this.startActivity(intent);
            overridePendingTransition(R.anim.slide_left, R.anim.slide_out_right);
            finish();

        });

    }
}