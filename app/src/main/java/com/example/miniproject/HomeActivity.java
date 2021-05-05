package com.example.miniproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        int id = R.id.nav_home;

        username = getIntent().getExtras().getString("USER_NAME");

        drawerLayout = findViewById(R.id.drawler_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.tool_bar);

//        setSupportActionBar(toolbar);

        navigationView.bringToFront();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_open, R.string.navigation_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(id);

        navigationView.getMenu().findItem(R.id.profile).setTitle(username);
//        navigationView.getMenu().findItem(R.id.profile).setVisible(false);
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
                break;
            case R.id.nav_rembo:
                break;

            case R.id.profile:
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
}
