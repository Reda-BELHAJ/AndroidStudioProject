package com.example.miniproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.example.miniproject.model.User;
import com.google.android.material.textfield.TextInputLayout;

import android.widget.TextView;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    Realm realm;

    TextView hello_message;
    TextView small_message;

    Button forget_pass;
    Button request_acc;
    Button login;

    TextInputLayout username;
    TextInputLayout password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        realm = Realm.getDefaultInstance();

        Animation blink = AnimationUtils.loadAnimation(this, R.anim.animation_blink);

        hello_message = findViewById(R.id.hello_message);
        small_message = findViewById(R.id.small_message);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        hello_message.startAnimation(blink);
        small_message.startAnimation(blink);

        forget_pass = findViewById(R.id.forget_pass);
        request_acc = findViewById(R.id.request_acc);
        login = findViewById(R.id.login);

        forget_pass.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ForgetActivity.class);
            MainActivity.this.startActivity(intent);
            overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
            finish();
        });

        request_acc.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
            MainActivity.this.startActivity(intent);
            overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
            finish();
        });

        login.setOnClickListener(v -> {
            String usernameText = username.getEditText().getText().toString();
            String passwordText = password.getEditText().getText().toString();

            if (checkNotEmpty(usernameText, passwordText)) {
                if (checkDB(usernameText, passwordText) && checkEtat(usernameText)) {
                    if(checkRole( usernameText).equals("Professeur")) {
                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        intent.putExtra("USER_NAME", usernameText);
                        MainActivity.this.startActivity(intent);
                        overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
                        finish();
                    }
                } else {
                    username.setError("The Password or the Username is Incorrect");
                    password.setError("The Password or the Username is Incorrect");
                }
            }

        });

    }

    public Boolean checkNotEmpty(String usernameText, String passwordText){
        boolean result = true;
        if (usernameText.isEmpty()) {
            username.setError("The Username Is Empty!");
            username.requestFocus();
            return result;
        }
        if (passwordText.isEmpty()) {
            password.setError("The Password Is Empty!");
            password.requestFocus();
            return result;
        }

        return result;
    }

    public Boolean checkDB(String usernameText, String passwordText){
        RealmResults<User> realmObjects = realm.where(User.class).findAll();
        for (User myRealmObject : realmObjects) {
            if (usernameText.equals(myRealmObject.getUserName()) && passwordText.equals(myRealmObject.getPassword())) {
                return true;
            }
        }
        return false;
    }

    public String checkRole(String usernameText){
        User realmObjects = realm.where(User.class).equalTo("userName", usernameText).findFirst();
        return realmObjects.getRole();
    }

    public boolean checkEtat(String usernameText){
        User realmObjects = realm.where(User.class).equalTo("userName", usernameText).findFirst();
        if(! realmObjects.getEtat()) {
            username.setError("The Username Is Not Yet Validated!");
            username.requestFocus();
        }
        return realmObjects.getEtat();
    }
}