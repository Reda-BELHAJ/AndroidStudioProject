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

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;

import com.example.miniproject.model.Mission;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class PieChartActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    String username, role, flag;
    AnyChartView pieChart;

    MaterialCardView card1, card3;

    Realm realm;
    String[] missionType1 = {"Organization of the Forum", "Academic Orientation", "Conference", "Discovery Day" , "Academic support" , "Exchange of Practices", "Presentation"};
    String[] missionType2 ={"Bus", "Train", "Airplane", "Taxi", "Transit"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);
        realm = Realm.getDefaultInstance();

        int id = R.id.charts;

        username = getIntent().getExtras().getString("USER_NAME");
        role = getIntent().getExtras().getString("ROLE");

        drawerLayout = findViewById(R.id.drawler_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.tool_bar);

        pieChart = findViewById(R.id.pieChart);
        card1 = findViewById(R.id.card1);
        card3 = findViewById(R.id.card3);

        navigationView.bringToFront();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_open, R.string.navigation_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(id);

        navigationView.getMenu().findItem(R.id.profile).setTitle(username);

        flag = getIntent().getExtras().getString("FLAG");

        visibility();

        if("type2".equals(flag)){
            setupPieChart(missionType2, "typeTransport");
        }
        else if ("type1".equals(flag)){
            setupPieChart(missionType1, "missionType");
        }

        card1.setOnClickListener(v -> {
            update("type1");
        });

        card3.setOnClickListener(v -> {
            update("type2");
        });
    }

    public void setupPieChart(String[] missionType, String field){
        Pie pie = AnyChart.pie();

        List<DataEntry> dataEntries = new ArrayList<>();
        int[] missionVal = getMission(missionType, field);

        for(int i = 0; i < missionType.length; i++){
            dataEntries.add(new ValueDataEntry(missionType[i], missionVal[i]));
        }

        pie.data(dataEntries);

        pie.legend()
                .position("center-bottom")
                .itemsLayout(LegendLayout.HORIZONTAL_EXPANDABLE)
                .align(Align.CENTER);

        pieChart.setChart(pie);
        pieChart.invalidate();
    }

    public void update(String flag){
        Intent intent = getIntent();
        intent.putExtra("FLAG",flag);
        finish();
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
    }

    public int[] getMission(String[] missionType, String field){
        int[] missionValues = new int[missionType.length];
        int i = 0;

        for(String item: missionType){
            missionValues[i] = realm.where(Mission.class).equalTo(field, item).findAll().size();
            i++;
        }
        return missionValues;
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
                gotoAccounts();
                break;
            case R.id.charts:
                break;
            case R.id.profile:
                gotoPrfile();
                break;
            case R.id.logout:
                Intent intent = new Intent(PieChartActivity.this, MainActivity.class);

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
            navigationView.getMenu().findItem(R.id.nav_missions).setVisible(false);
            navigationView.getMenu().findItem(R.id.charts).setVisible(false);
        }
    }

    public void gotoHome(){
        Intent intent = new Intent(PieChartActivity.this, HomeActivity.class);
        intent.putExtra("USER_NAME", username);
        intent.putExtra("ROLE", role);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
        finish();
    }

    public void gotoMissions(){
        Intent intent = new Intent(PieChartActivity.this, MissionActivity.class);
        intent.putExtra("USER_NAME", username);
        intent.putExtra("ROLE", role);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
        finish();
    }

    public void gotoAccounts(){
        Intent intent = new Intent(PieChartActivity.this, AccountActivity.class);
        intent.putExtra("USER_NAME", username);
        intent.putExtra("ROLE", role);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
        finish();
    }

    public void gotoPrfile(){
        Intent intent = new Intent(PieChartActivity.this, UpdateActivity.class);
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