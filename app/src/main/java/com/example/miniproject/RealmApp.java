package com.example.miniproject;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class RealmApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .schemaVersion(2)
                .allowWritesOnUiThread(true)
                .name("RealmDB.realm")
                .build();
        Realm.setDefaultConfiguration(configuration);

    }
}
