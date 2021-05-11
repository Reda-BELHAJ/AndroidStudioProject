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
import android.widget.ListView;

import com.google.android.material.navigation.NavigationView;

import io.realm.Realm;

public class AccountActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Realm realm;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ListView list_view;

    String username, role;
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

        username = getIntent().getExtras().getString("USER_NAME");
        role = getIntent().getExtras().getString("ROLE");

        navigationView.bringToFront();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_open, R.string.navigation_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(id);

        navigationView.getMenu().findItem(R.id.profile).setTitle(username);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                gotoHome();
                break;
            case R.id.nav_missions:
                break;
            case R.id.nav_rembo:
                break;

            case R.id.profile:
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

    public void gotoHome(){
        Intent intent = new Intent(AccountActivity.this, HomeActivity.class);
        intent.putExtra("USER_NAME", username);
        intent.putExtra("ROLE", role);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
        finish();
    }
}