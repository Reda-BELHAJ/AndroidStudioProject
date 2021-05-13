package com.example.miniproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.miniproject.model.User;
import com.google.android.material.textfield.TextInputLayout;

import java.util.UUID;
import java.util.regex.Pattern;

import io.realm.Realm;
import io.realm.RealmResults;

public class SignUpActivity extends AppCompatActivity {

    Realm realm;

    TextView hello_message;
    TextView small_message;

    Button login_req;
    Button signup;

    TextInputLayout email;
    TextInputLayout fullname;
    TextInputLayout username;
    TextInputLayout password;
    TextInputLayout confirm_password;

    JavaMailAPI send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        realm = Realm.getDefaultInstance();

        Animation blink = AnimationUtils.loadAnimation(this, R.anim.animation_blink);

        hello_message = findViewById(R.id.hello_message);
        small_message = findViewById(R.id.small_message);

        hello_message.startAnimation(blink);
        small_message.startAnimation(blink);

        email = findViewById(R.id.email);
        fullname = findViewById(R.id.fullname);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        confirm_password = findViewById(R.id.confirm_password);

        signup = findViewById(R.id.sign_up);
        login_req = findViewById(R.id.login_request);

        login_req.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
            SignUpActivity.this.startActivity(intent);
            overridePendingTransition(R.anim.slide_left, R.anim.slide_out_right);
            finish();
        });



        signup.setOnClickListener(v -> {

            String emailText = email.getEditText().getText().toString();
            String fullnameText = fullname.getEditText().getText().toString();
            String usernameText = username.getEditText().getText().toString();
            String passwordText = password.getEditText().getText().toString();
            String con_passwordText = confirm_password.getEditText().getText().toString();



            if (checkNotEmpty(emailText, fullnameText, usernameText, passwordText, con_passwordText)
                    && checkDuplicate(emailText, usernameText)
                    && checkEmail(emailText)
                    && checkPasswords(passwordText, con_passwordText)) {

                addUser();
//                sendEmail(emailText, fullnameText, usernameText, passwordText);

                Intent intent = new Intent(SignUpActivity.this, SignUpActivity2.class);
                SignUpActivity.this.startActivity(intent);
                overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
                finish();
            }

        });


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

    public boolean checkPasswords(String passwordText, String con_passwordText){
        if(! passwordText.equals(con_passwordText)){
            confirm_password.setError("It doesn't match the Password");
            confirm_password.requestFocus();
        }

        return passwordText.equals(con_passwordText);
    }

    public Boolean checkNotEmpty(String emailText, String fullnameText, String usernameText, String passwordText, String con_passwordText) {
        boolean result = true;

        if (emailText.isEmpty()) {
            email.setError("The Email Is Empty!");
            email.requestFocus();
            result = false;
        }

        if (fullnameText.isEmpty()) {
            fullname.setError("The FullName Is Empty!");
            fullname.requestFocus();
            result = false;
        }

        if (usernameText.isEmpty()) {
            username.setError("The Username Is Empty!");
            username.requestFocus();
            result = false;
        }

        if (passwordText.isEmpty()) {
            password.setError("The Password Is Empty!");
            password.requestFocus();
            result = false;
        }

        if (con_passwordText.isEmpty()) {
            confirm_password.setError("Confirm your Password");
            confirm_password.requestFocus();
            result = false;
        }

        return result;
    }

    public Boolean checkDuplicate(String emailText, String usernameText) {
        RealmResults<User> realmObjects = realm.where(User.class).findAll();
        for (User myRealmObject : realmObjects) {
            if (emailText.equals(myRealmObject.getUserEmail())) {
                email.setError("The Email Is Already in Use!");
                email.requestFocus();
                return false;
            }
            if (usernameText.equals(myRealmObject.getUserName())) {
                username.setError("The Username Is Already in Use!");
                username.requestFocus();
                return false;
            }
        }
        return true;
    }

    public void addUser(){
        realm.executeTransactionAsync(bgRealm -> {
            Number maxId = bgRealm.where(User.class).max("userId");

            int newId = (maxId == null) ? 1 : maxId.intValue() + 1;
            User user = bgRealm.createObject(User.class, newId);
            user.setUserEmail(email.getEditText().getText().toString());
            user.setFullName(fullname.getEditText().getText().toString());
            user.setUserName(username.getEditText().getText().toString());
            user.setPassword(password.getEditText().getText().toString());
            user.setRole("Professeur");
            user.setEtat(false);

        }, () -> {
            System.out.println("Success");

        }, error -> {
            error.printStackTrace();
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        SignUpActivity.this.startActivity(intent);
        overridePendingTransition(R.anim.slide_left, R.anim.slide_out_right);
        finish();
    }

    @Override
    protected void onDestroy() {
        if (send != null && send.getmProgressDialog() != null && (send.getmProgressDialog().isShowing())) {
            send.getmProgressDialog().cancel();
        }
        super.onDestroy();
    }
}