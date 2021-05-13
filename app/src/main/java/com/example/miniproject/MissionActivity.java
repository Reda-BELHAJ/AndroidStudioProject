package com.example.miniproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.miniproject.adapter.MissionAdapter;
import com.example.miniproject.model.Mission;
import com.example.miniproject.model.User;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class MissionActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Realm realm;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ListView list_view;
    MissionAdapter missionAdapter ;
    MaterialCardView card;

    TextView small_message;
    Button All, started, onHold, finished;

    String username, role;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission);
        realm = Realm.getDefaultInstance();

        int id = R.id.nav_missions;

        drawerLayout = findViewById(R.id.drawler_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.tool_bar);
        list_view = findViewById(R.id.list_view);
        card = findViewById(R.id.card);
        small_message = findViewById(R.id.small_message);
        All = findViewById(R.id.All);
        started = findViewById(R.id.started);
        onHold = findViewById(R.id.onHold);
        finished = findViewById(R.id.finished);

        username = getIntent().getExtras().getString("USER_NAME");
        role = getIntent().getExtras().getString("ROLE");

        navigationView.bringToFront();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_open, R.string.navigation_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(id);

        navigationView.getMenu().findItem(R.id.profile).setTitle(username);

        missionAdapter = new MissionAdapter(this, getMissions(), role);
        list_view.setAdapter(missionAdapter);

        card.setOnClickListener(v -> {
            Intent intent = new Intent(MissionActivity.this, MissionForm.class);
            intent.putExtra("USER_NAME", username);
            intent.putExtra("ROLE", role);
            MissionActivity.this.startActivity(intent);
            overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
            finish();
        });

        if(role.equals("Professeur") || role.equals("ResponsableRH")){
            small_message.setVisibility(View.GONE);
            All.setVisibility(View.GONE);
            onHold.setVisibility(View.GONE);
            finished.setVisibility(View.GONE);
            started.setVisibility(View.GONE);
        }

        All.setOnClickListener(v -> {
            missionAdapter = new MissionAdapter(this, getMissions(), role);
            list_view.setAdapter(missionAdapter);
        });

        started.setOnClickListener(v -> {
            missionAdapter = new MissionAdapter(this, getMissionsFilter("start"), role);
            list_view.setAdapter(missionAdapter);
        });

        onHold.setOnClickListener(v -> {
            missionAdapter = new MissionAdapter(this, getMissionsFilter("onhold"), role);
            list_view.setAdapter(missionAdapter);
        });

        finished.setOnClickListener(v -> {
            missionAdapter = new MissionAdapter(this, getMissionsFilter("finish"), role);
            list_view.setAdapter(missionAdapter);
        });

        list_view.setOnItemClickListener((parent, view, position, id1) -> {
            Object mission = parent.getAdapter().getItem(position);
            int missionId = ((Mission) mission).getMissionId();
            Intent intent = new Intent(MissionActivity.this, DescriptionActivity.class);
            intent.putExtra("USER_NAME", username);
            intent.putExtra("ROLE", role);
            intent.putExtra("missionId", missionId);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
            finish();
        });

        visibility(role);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                gotoHome();
                break;
            case R.id.nav_missions:
                break;
            case R.id.accounts:
                gotoAccounts();
                break;
            case R.id.profile:
                gotoPrfile();
                break;
            case R.id.logout:
                Intent intent = new Intent(MissionActivity.this, MainActivity.class);

                startActivity(intent);
                overridePendingTransition(R.anim.slide_left, R.anim.slide_out_right);
                finish();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    public void gotoHome(){
        Intent intent = new Intent(MissionActivity.this, HomeActivity.class);
        intent.putExtra("USER_NAME", username);
        intent.putExtra("ROLE", role);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
        finish();
    }

    public void visibility(String role){
        if(role.equals("Professeur") || role.equals("Directeur")) {
            navigationView.getMenu().findItem(R.id.accounts).setVisible(false);
        }
    }

    public ArrayList<Mission> getMissions(){
        ArrayList<Mission> list = new ArrayList<>();
        if(role.equals("Professeur") || role.equals("ResponsableRH") ){
            User user = realm.where(User.class).equalTo("userName", username).findFirst();
            int userId = user.getUserId();
            RealmResults<Mission> realmObjects = realm.where(Mission.class).equalTo("userId", userId).findAll();
            list.addAll(realm.copyFromRealm(realmObjects));
        }
        else {
            RealmResults<Mission> realmObjects = realm.where(Mission.class).findAll();
            list.addAll(realm.copyFromRealm(realmObjects));
        }

        return list;
    }

    public ArrayList<Mission> getMissionsFilter(String etat){
        ArrayList<Mission> list = new ArrayList<>();

        RealmResults<Mission> mission = realm.where(Mission.class).equalTo("etat", etat).findAll();

        list.addAll(realm.copyFromRealm(mission));

        return list;
    }

    public void gotoAccounts(){
        Intent intent = new Intent(MissionActivity.this, AccountActivity.class);
        intent.putExtra("USER_NAME", username);
        intent.putExtra("ROLE", role);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
        finish();
    }

    public void gotoPrfile(){
        Intent intent = new Intent(MissionActivity.this, UpdateActivity.class);
        intent.putExtra("USER_NAME", username);
        intent.putExtra("ROLE", role);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
        finish();
    }

}