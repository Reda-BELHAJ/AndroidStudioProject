package com.example.miniproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.miniproject.model.User;

import io.realm.Realm;
import io.realm.RealmResults;

public class SplashActivity extends AppCompatActivity {

    private static int time_intent = 2000;
    TextView big_text;
    TextView small_text;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        realm = Realm.getDefaultInstance();

        big_text = findViewById(R.id.big_text);
        small_text = findViewById(R.id.small_text);

        Animation splash_down = AnimationUtils.loadAnimation(this, R.anim.animation_splash_down);

        big_text.startAnimation(splash_down);
        small_text.startAnimation(splash_down);

        addDefault("reda.belhaj@uir.ac.ma", "Reda BELHAJ", "Belhaj", "123", "President");
        addDefault("yassine.bouziane@uir.ac.ma", "Yassine BOUZIANE", "Yassine", "123", "ResponsableRH");
        addDefault("mohamed-reda.falhi@uir.ac.ma", "Reda FALHI", "Falhi", "123", "Directeur");

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
    public void addDefault(String mail, String fullname, String username, String pass, String role){
        RealmResults<User> realmObjects = realm.where(User.class).findAll();
        if( checkDuplicate(mail, username)){
            realm.executeTransactionAsync(bgRealm -> {
                Number maxId = bgRealm.where(User.class).max("userId");

                int newId = (maxId == null) ? 1 : maxId.intValue() + 1;
                User user = bgRealm.createObject(User.class, newId);
                user.setUserEmail(mail);
                user.setFullName(fullname);
                user.setUserName(username);
                user.setPassword(pass);
                user.setRole(role);
                user.setEtat(true);

            }, () -> {
                System.out.println("Success");

            }, error -> {
                error.printStackTrace();
            });
        }
    }

    public Boolean checkDuplicate(String mail, String username) {
        RealmResults<User> realmObjects = realm.where(User.class).findAll();
        for (User myRealmObject : realmObjects) {
            if (mail.equals(myRealmObject.getUserEmail())) {
                return false;
            }
            if (username.equals(myRealmObject.getUserName())) {
                return false;
            }
        }
        return true;
    }

}
