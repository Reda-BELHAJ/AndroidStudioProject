package com.example.miniproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.miniproject.model.Mission;
import com.example.miniproject.model.User;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationView;

import io.realm.Realm;
import io.realm.RealmResults;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    TextView message_welcome, small_message2, small_message3, countAll, countInv, countAll1, countInv1, countAll2, countInv2;
    TextView title, type, description, etat;
    MaterialCardView card1, card3, cardMission, notFound;
    LinearLayout accountslayout, missionlayout, missionlayout2;

    String username, role;

    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        realm = Realm.getDefaultInstance();

        int id = R.id.nav_home;

        username = getIntent().getExtras().getString("USER_NAME");
        role = getIntent().getExtras().getString("ROLE");

        drawerLayout = findViewById(R.id.drawler_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.tool_bar);
        message_welcome = findViewById(R.id.message_welcome);
        card1 = findViewById(R.id.card1);
        card3 = findViewById(R.id.card3);
        accountslayout = findViewById(R.id.accountslayout);
        missionlayout = findViewById(R.id.missionlayout);
        missionlayout2 = findViewById(R.id.missionlayout2);
        small_message2 = findViewById(R.id.small_message2);
        small_message3 = findViewById(R.id.small_message3);
        cardMission = findViewById(R.id.cardMission);
        notFound = findViewById(R.id.notFound);

        title = findViewById(R.id.title);
        type = findViewById(R.id.type);
        description = findViewById(R.id.description);
        etat = findViewById(R.id.etat);

        notFound.setVisibility(View.GONE);

        countAll = findViewById(R.id.countAll);
        countInv = findViewById(R.id.countInv);

        countAll1 = findViewById(R.id.countAll1);
        countInv1 = findViewById(R.id.countInv1);

        countAll2 = findViewById(R.id.countAll2);
        countInv2 = findViewById(R.id.countInv2);

        getLatestMission();

        card1.setOnClickListener(v -> {
            gotoMissions();
        });

        card3.setOnClickListener(v -> {
            gotoAccounts();
        });

        message_welcome.setText("HELLO " + username.toUpperCase() +"!");

        navigationView.bringToFront();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_open, R.string.navigation_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(id);

        navigationView.getMenu().findItem(R.id.profile).setTitle(username);

        visibility(role);

        countAll.setText(getCount());
        countInv.setText(getInvCount());

        countAll1.setText(getCountM());
        countInv1.setText(getInvCountM("finish"));

        countAll2.setText(getInvCountM("start"));
        countInv2.setText(getInvCountM("onhold"));

    }

    public String getCount(){
        long  missions = realm.where(User.class).count();
        return "" + missions;
    }

    public String getInvCount(){
        long  missions = realm.where(User.class).equalTo("etat", false).count();
        return "" + missions;
    }

    public String getCountM(){
        long  users = realm.where(Mission.class).count();
        return "" + users;
    }

    public String getInvCountM(String etat){
        long  users = realm.where(Mission.class).equalTo("etat", etat).count();
        return "" + users;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                break;
            case R.id.nav_missions:
                gotoMissions();
                break;
            case R.id.accounts:
                gotoAccounts();
                break;
            case R.id.charts:
                gotoCharts();
                break;
            case R.id.profile:
                gotoPrfile();
                break;
            case R.id.logout:
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);

                startActivity(intent);
                overridePendingTransition(R.anim.slide_left, R.anim.slide_out_right);
                finish();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    public void visibility(String role){
        if(role.equals("Professeur") || role.equals("Directeur") || role.equals("President")){
            navigationView.getMenu().findItem(R.id.accounts).setVisible(false);
            card3.setVisibility(View.GONE);
        }
        if(!(role.equals("ResponsableRH"))){
            accountslayout.setVisibility(View.GONE);
            small_message2.setVisibility(View.GONE);
        }
        if(!(role.equals("Directeur") || role.equals("President") )){
            navigationView.getMenu().findItem(R.id.charts).setVisible(false);
            missionlayout.setVisibility(View.GONE);
            missionlayout2.setVisibility(View.GONE);
            small_message3.setVisibility(View.GONE);
        }
    }

    public void gotoCharts(){
        Intent intent = new Intent(HomeActivity.this, PieChartActivity.class);
        intent.putExtra("USER_NAME", username);
        intent.putExtra("ROLE", role);
        intent.putExtra("FLAG","");
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
        finish();
    }

    public void gotoMissions(){
        Intent intent = new Intent(HomeActivity.this, MissionActivity.class);
        intent.putExtra("USER_NAME", username);
        intent.putExtra("ROLE", role);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
        finish();
    }

    public void gotoAccounts(){
        Intent intent = new Intent(HomeActivity.this, AccountActivity.class);
        intent.putExtra("USER_NAME", username);
        intent.putExtra("ROLE", role);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
        finish();
    }

    public void gotoPrfile(){
        Intent intent = new Intent(HomeActivity.this, UpdateActivity.class);
        intent.putExtra("USER_NAME", username);
        intent.putExtra("ROLE", role);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
        finish();
    }

    public void getLatestMission(){
        User user = realm.where(User.class).equalTo("userName", username).findFirst();
        int userId = user.getUserId();
        if(! realm.where(Mission.class).equalTo("userId", userId).findAll().isEmpty()) {
            Mission realmObjects = realm.where(Mission.class).equalTo("userId", userId).findAll().last();

            title.setText(realmObjects.getMissionName());
            type.setText(realmObjects.getMissionType());
            description.setText(realmObjects.getDescription());

            cardMission.setOnClickListener(v -> {
                int missionId = ((Mission) realmObjects).getMissionId();
                Intent intent = new Intent(HomeActivity.this, DescriptionActivity.class);
                intent.putExtra("USER_NAME", username);
                intent.putExtra("ROLE", role);
                intent.putExtra("missionId", missionId);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
                finish();
            });

            switch (realmObjects.getEtat()) {
                case "start":
                    etat.setText("In process");
                    break;
                case "onhold":
                    etat.setText("On hold");
                    break;
                case "finish":
                    etat.setText("Successful");
                    break;
            }

        }else {
            notFound.setVisibility(View.VISIBLE);
            cardMission.setVisibility(View.GONE);
        }
    }
}
