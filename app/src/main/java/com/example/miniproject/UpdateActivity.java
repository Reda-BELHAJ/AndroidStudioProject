package com.example.miniproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.example.miniproject.model.User;
import com.google.android.material.textfield.TextInputLayout;

import io.realm.Realm;
import io.realm.RealmResults;

public class UpdateActivity extends AppCompatActivity {

    Button update;
    ImageView backarrow_icon;

    TextInputLayout email;
    TextInputLayout fullname;
    TextInputLayout username;
    TextInputLayout password;
    TextInputLayout confirm_password;

    String user_name, role;

    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        realm = Realm.getDefaultInstance();

        backarrow_icon = findViewById(R.id.backarrow);
        email = findViewById(R.id.email);
        fullname = findViewById(R.id.fullname);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        confirm_password = findViewById(R.id.confirm_password);

        update = findViewById(R.id.update);

        user_name = getIntent().getExtras().getString("USER_NAME");
        role = getIntent().getExtras().getString("ROLE");

        email.getEditText().setText(getUser().getUserEmail());
        fullname.getEditText().setText(getUser().getFullName());
        username.getEditText().setText(user_name);

        backarrow_icon.setOnClickListener(v -> {
            Intent intent = new Intent(UpdateActivity.this, HomeActivity.class);
            intent.putExtra("USER_NAME", user_name);
            intent.putExtra("ROLE", role);
            UpdateActivity.this.startActivity(intent);
            overridePendingTransition(R.anim.slide_left, R.anim.slide_out_right);
            finish();
        });

        update.setOnClickListener(v -> {
            if (checkNotEmpty(email.getEditText().toString(), fullname.getEditText().toString(), password.getEditText().toString(), confirm_password.getEditText().toString() )
                || checkPasswords(password.getEditText().toString(), confirm_password.getEditText().toString())){
                updateUser(getUser(), getUser().getUserId(), getUser().getUserName(), password.getEditText().toString(), email.getEditText().toString(), fullname.getEditText().toString(), getUser().getRole() );
            }
        });
    }

    public Boolean checkNotEmpty(String emailText, String fullnameText, String passwordText, String con_passwordText) {
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

    public boolean checkPasswords(String passwordText, String con_passwordText){
        if(! passwordText.equals(con_passwordText)){
            confirm_password.setError("It doesn't match the Password");
            confirm_password.requestFocus();
        }

        return passwordText.equals(con_passwordText);
    }

    public User getUser() {
        User user = realm.where(User.class).equalTo("userName", user_name).findFirst();
        return user;
    }

    private void updateUser(User user, int id ,String username, String password, String mail, String fullname, String role) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                user.setUserId(id);
                user.setUserName(username);
                user.setEtat(true);
                user.setPassword(password);
                user.setUserEmail(mail);
                user.setFullName(fullname);
                user.setRole(role);

                realm.copyToRealmOrUpdate(user);
            }
        });
    }
}