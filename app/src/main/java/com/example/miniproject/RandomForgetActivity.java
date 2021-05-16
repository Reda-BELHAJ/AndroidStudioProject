package com.example.miniproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.example.miniproject.model.User;
import com.google.android.material.textfield.TextInputLayout;

import java.security.SecureRandom;

import io.realm.Realm;

public class RandomForgetActivity extends AppCompatActivity {

    ImageView backarrow;
    Button next, resend;
    TextInputLayout code;
    String emailText;

    Realm realm;
    JavaMailAPI send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_forget);

        realm = Realm.getDefaultInstance();

        backarrow = findViewById(R.id.backarrow);
        next = findViewById(R.id.next);
        code = findViewById(R.id.code);
        resend = findViewById(R.id.resend);

        backarrow.setOnClickListener(v -> {
            Intent intent = new Intent(RandomForgetActivity.this, ForgetActivity.class);
            RandomForgetActivity.this.startActivity(intent);
            overridePendingTransition(R.anim.slide_left, R.anim.slide_out_right);
            finish();
        });

        emailText = getIntent().getExtras().getString("email");

        SecureRandom random = new SecureRandom();
        int num = random.nextInt(100000);
        String formatted = String.format("%05d", num);

        sendEmail(emailText, getName(), formatted);

        next.setOnClickListener(v -> {
            if(checkNotEmpty()) {
                Intent intent = new Intent(RandomForgetActivity.this, Forget2Activity.class);
                intent.putExtra("email", emailText);
                RandomForgetActivity.this.startActivity(intent);
                overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
                finish();
            }
        });

        resend.setOnClickListener(v -> {
            sendEmail(emailText, getName(), formatted);
        });
    }

    public String getName(){
        User toEdit = realm.where(User.class).equalTo("userEmail", emailText).findFirst();
        return toEdit.getFullName();
    }

    public boolean checkNotEmpty(){
        if(code.getEditText().getText().toString().isEmpty()){
            code.setError("The Code Is Empty!");
            return false;
        }
        return true;
    }

    public void sendEmail(String emailText ,String fullnameText,String formatted){
        String message = "Hey " + fullnameText + ",\nWe are happy you signed up for Mini-Projet, your request has been verified." +
                "\nHere's your Code: "+ formatted +
                "\nWelcome to Mini-Projet!"+
                "\nMini-Projet Team";
        send = new JavaMailAPI(this , emailText, "Email Validation", message);
        send.execute();
    }
}