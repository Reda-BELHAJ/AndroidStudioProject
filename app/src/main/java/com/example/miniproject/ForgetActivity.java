package com.example.miniproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.miniproject.model.User;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

import io.realm.Realm;
import io.realm.RealmResults;

public class ForgetActivity extends AppCompatActivity {

    ImageView backarrow;
    Button next;
    TextInputLayout email;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        realm = Realm.getDefaultInstance();

        backarrow = findViewById(R.id.backarrow);
        next = findViewById(R.id.next);
        email = findViewById(R.id.email);

        backarrow.setOnClickListener(v -> {
            Intent intent = new Intent(ForgetActivity.this, MainActivity.class);
            ForgetActivity.this.startActivity(intent);
            overridePendingTransition(R.anim.slide_left, R.anim.slide_out_right);
            finish();
        });

        next.setOnClickListener(v -> {
            String emailText = email.getEditText().getText().toString();

            if(checkEmail(emailText) && checkEmailDB(emailText)){
                Intent intent = new Intent(ForgetActivity.this, RandomForgetActivity.class);
                intent.putExtra("email", emailText);
                ForgetActivity.this.startActivity(intent);
                overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
                finish();
            }
        });
    }

    public boolean checkEmailDB(String emailText){
        boolean ay = false;
        RealmResults<User> realmObjects = realm.where(User.class).findAll();
        for (User myRealmObject : realmObjects) {
            if (emailText.equals(myRealmObject.getUserEmail())) {
                return true;
            }
            else{
                ay = true;
            }
        }

        if(ay){
            email.setError("The Email Is Not Valid!");
            email.requestFocus();
        }
        return false;
    }

    public boolean checkEmail(String emailText){
        String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);

        if (! pattern.matcher(emailText).matches()){
            email.setError("The Email Is Not Valid!");
            email.requestFocus();
        }

        return pattern.matcher(emailText).matches();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ForgetActivity.this, MainActivity.class);
        ForgetActivity.this.startActivity(intent);
        overridePendingTransition(R.anim.slide_left, R.anim.slide_out_right);
        finish();
    }
}