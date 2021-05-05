package com.example.miniproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.example.miniproject.model.User;
import com.google.android.material.textfield.TextInputLayout;

import io.realm.Realm;

public class Forget2Activity extends AppCompatActivity {

    ImageView backarrow;
    Button next;
    TextInputLayout password, conf_password;
    Realm realm;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget2);

        realm = Realm.getDefaultInstance();

        backarrow = findViewById(R.id.backarrow);
        next = findViewById(R.id.next);
        password = findViewById(R.id.password);
        conf_password = findViewById(R.id.confirm_password);

        email = getIntent().getExtras().getString("email");

        System.out.println(email +"++++++++++++++++++++++++");

        backarrow.setOnClickListener(v -> {
            Intent intent = new Intent(Forget2Activity.this, ForgetActivity.class);
            Forget2Activity.this.startActivity(intent);
            overridePendingTransition(R.anim.slide_left, R.anim.slide_out_right);
            finish();
        });

        next.setOnClickListener(v -> {
            String passwordText = password.getEditText().getText().toString();
            String conf_passwordText = conf_password.getEditText().getText().toString();

            if(checkPasswords(passwordText, conf_passwordText)){
                updatePassword(passwordText);

                Intent intent = new Intent(Forget2Activity.this, Forget3Activity.class);
                Forget2Activity.this.startActivity(intent);
                overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
                finish();
            }
        });
    }

    public void updatePassword(String passwordText) {
        User toEdit = realm.where(User.class).equalTo("userEmail", email).findFirst();
        realm.beginTransaction();
        toEdit.setPassword(passwordText);
        realm.commitTransaction();
    }

    public boolean checkPasswords(String passwordText, String con_passwordText){

        if(! passwordText.equals(con_passwordText)){
            conf_password.setError("It doesn't match the Password");
            conf_password.requestFocus();
        }

        return checkPass(passwordText, con_passwordText) && passwordText.equals(con_passwordText);
    }

    public boolean checkPass(String passwordText, String con_passwordText){
        boolean result = true;

        if (passwordText.isEmpty()) {
            password.setError("The Password Is Empty!");
            password.requestFocus();
            result = false;
        }

        if (con_passwordText.isEmpty()) {
            conf_password.setError("Confirm your Password");
            conf_password.requestFocus();
            result = false;
        }
        return result;
    }


}