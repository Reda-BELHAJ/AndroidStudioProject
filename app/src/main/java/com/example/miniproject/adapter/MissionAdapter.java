package com.example.miniproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.miniproject.AccountActivity;
import com.example.miniproject.JavaMailAPI;
import com.example.miniproject.MissionActivity;
import com.example.miniproject.R;
import com.example.miniproject.model.Mission;
import com.example.miniproject.model.User;
import com.google.android.material.button.MaterialButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import io.realm.Realm;

public class MissionAdapter extends BaseAdapter {
    Realm realm;
    JavaMailAPI send;

    private Context context;
    private ArrayList<Mission> missions;
    private String role;

    String user1;

    public MissionAdapter(Context context, ArrayList<Mission> missions, String role, String user1) {
        this.context = context;
        this.missions = missions;
        this.role = role;
        this.user1 = user1;
    }

    @Override
    public int getCount() {
        return missions.size();
    }

    @Override
    public Object getItem(int position) {
        return missions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        realm = Realm.getDefaultInstance();

        if (convertView == null) {
            convertView = View.inflate(context, R.layout.card_mission, null);
        }

        TextView title = convertView.findViewById(R.id.title);
        TextView type = convertView.findViewById(R.id.type);
        TextView description = convertView.findViewById(R.id.description);
        MaterialButton validateBtn = convertView.findViewById(R.id.validateBtn);
        MaterialButton deleteBtn = convertView.findViewById(R.id.deleteBtn);
        TextView etat = convertView.findViewById(R.id.etat);

        Mission mission = missions.get(position);

        switch (mission.getEtat()){
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
        title.setText(mission.getMissionName());
        type.setText(mission.getMissionType());
        description.setText(mission.getDescription());

        if ((role.equals("ResponsableRH") || role.equals("Professeur")) || mission.getEtat().equals("finish")
                || (role.equals("Directeur") &&  mission.getEtat().equals("onhold")) ) {
            validateBtn.setVisibility(View.GONE);
            deleteBtn.setVisibility(View.GONE);
        }

        validateBtn.setOnClickListener(v -> {
            if (role.equals("Directeur")) {
                updateMission(mission.getMissionId(), "onhold");
                validateBtn.setBackgroundColor(Color.parseColor("#418b8c"));
                update();
            } else if (role.equals("President")) {
                updateMission(mission.getMissionId(), "finish");
                sendEmail(getUser(mission.getMissionId()).getUserEmail(), getUser(mission.getMissionId()).getFullName(), mission.getMissionName(), "verified try to download your pdf.");
                update();
            }
        });

        deleteBtn.setOnClickListener(v -> {
            deleteMission(mission.getMissionId());
            sendEmail(getUser(mission.getMissionId()).getUserEmail(), getUser(mission.getMissionId()).getFullName(), mission.getMissionName(), "deleted.");
            update();
        });

        return convertView;
    }

    private void updateMission(int missionId, String etat) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                final Mission result = realm.where(Mission.class).equalTo("missionId", missionId).findFirst();
                result.setEtat(etat);
            }
        });
    }

    private void deleteMission(int missionId) {
        Mission modal = realm.where(Mission.class).equalTo("missionId", missionId).findFirst();
        // on below line we are executing a realm transaction.
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // on below line we are calling a method for deleting this course
                modal.deleteFromRealm();
            }
        });
    }

    public User getUser(int missionId){
        Mission result = realm.where(Mission.class).equalTo("missionId", missionId).findFirst();
        User user = realm.where(User.class).equalTo("userId", result.getUserId()).findFirst();

        return user;
    }

    public void sendEmail(String emailText ,String fullnameText,String missionName, String wo){
        String message = "Hey " + fullnameText + ",\nWe are happy you signed up for Mini-Projet," +
                "\nThe mission " + missionName + "has been " + wo +
                "\nWelcome to Mini-Projet!"+
                "\nMini-Projet Team";
        send = new JavaMailAPI(context , emailText, "Email Validation", message);
        send.execute();
    }

    public void update(){
        MissionActivity ac = (MissionActivity) context;
        Intent intent = ac.getIntent();
        intent.putExtra("USER_NAME", user1);
        intent.putExtra("ROLE", role);
        ac.finish();
        ac.startActivity(intent);
        ac.overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
    }
}
