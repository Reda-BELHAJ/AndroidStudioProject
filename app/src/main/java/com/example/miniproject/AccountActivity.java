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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.miniproject.adapter.AccountAdapter;
import com.example.miniproject.adapter.MissionAdapter;
import com.example.miniproject.model.Mission;
import com.example.miniproject.model.User;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class AccountActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Realm realm;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ListView list_view;

    Button All, Notvalidate;

    String username, role;
    TextView count;

    AccountAdapter accountAdapter, accountAdapter1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        realm = Realm.getDefaultInstance();

        int id = R.id.accounts;

        drawerLayout = findViewById(R.id.drawler_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.tool_bar);
        list_view = findViewById(R.id.list_view);
        All = findViewById(R.id.All);
        Notvalidate = findViewById(R.id.Notvalidate);
        count = findViewById(R.id.count);

        username = getIntent().getExtras().getString("USER_NAME");
        role = getIntent().getExtras().getString("ROLE");

        navigationView.bringToFront();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_open, R.string.navigation_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(id);

        navigationView.getMenu().findItem(R.id.profile).setTitle(username);

        accountAdapter = new AccountAdapter(this, getUsers(),username, role );
        accountAdapter1 = new AccountAdapter(this, getUsersFilter(),username, role);

        list_view.setAdapter(accountAdapter);

        All.setOnClickListener(v -> list_view.setAdapter(accountAdapter));
        Notvalidate.setOnClickListener(v -> list_view.setAdapter(accountAdapter1));

        count.setText(getUsersFilter().size() + " Not Validate Account!");

        accountAdapter.notifyDataSetChanged();
        accountAdapter1.notifyDataSetChanged();

        visibility();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                gotoHome();
                break;
            case R.id.nav_missions:
                gotoMissions();
                break;
            case R.id.accounts:
                break;
            case R.id.charts:
                gotoCharts();
                break;
            case R.id.profile:
                gotoPrfile();
                break;
            case R.id.logout:
                Intent intent = new Intent(AccountActivity.this, MainActivity.class);

                startActivity(intent);
                overridePendingTransition(R.anim.slide_left, R.anim.slide_out_right);
                finish();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    public void visibility(){
        if(!(role.equals("ResponsableRH"))){
            navigationView.getMenu().findItem(R.id.accounts).setVisible(false);
        }
        if(!(role.equals("Directeur") || role.equals("President") )){
            navigationView.getMenu().findItem(R.id.charts).setVisible(false);
        }
    }

    public void gotoCharts(){
        Intent intent = new Intent(AccountActivity.this, PieChartActivity.class);
        intent.putExtra("USER_NAME", username);
        intent.putExtra("ROLE", role);
        intent.putExtra("FLAG","");
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
        finish();
    }

    public void gotoHome(){
        Intent intent = new Intent(AccountActivity.this, HomeActivity.class);
        intent.putExtra("USER_NAME", username);
        intent.putExtra("ROLE", role);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
        finish();
    }

    public void gotoMissions(){
        Intent intent = new Intent(AccountActivity.this, MissionActivity.class);
        intent.putExtra("USER_NAME", username);
        intent.putExtra("ROLE", role);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
        finish();
    }

    public ArrayList<User> getUsers(){
        ArrayList<User> list = new ArrayList<>();

        RealmResults<User> users = realm.where(User.class).notEqualTo("userName", username).findAll();
        list.addAll(realm.copyFromRealm(users));

        return list;
    }

    public ArrayList<User> getUsersFilter(){
        ArrayList<User> list = new ArrayList<>();

        RealmResults<User> users = realm.where(User.class).notEqualTo("userName", username).equalTo("etat", false).findAll();

        list.addAll(realm.copyFromRealm(users));

        return list;
    }

    public void gotoPrfile(){
        Intent intent = new Intent(AccountActivity.this, UpdateActivity.class);
        intent.putExtra("USER_NAME", username);
        intent.putExtra("ROLE", role);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            gotoHome();
            super.onBackPressed();
        }
    }
}