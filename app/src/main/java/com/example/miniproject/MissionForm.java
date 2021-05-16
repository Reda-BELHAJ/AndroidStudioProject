package com.example.miniproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;

import com.example.miniproject.model.Mission;
import com.example.miniproject.model.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.Calendar;

import io.realm.Realm;

public class MissionForm extends AppCompatActivity {
    ArrayAdapter<String> adapter, adapter2;
    ImageView backarrow;

    TextInputEditText fin, debut, description, nomMission, adresse;
    TextInputLayout layoutfininsh, layoutstart, layoutdes, layouttype, layoutmission, layouttypeTransport, layoutAdresse;

    String username, role;

    Realm realm;

    Calendar calendar = Calendar.getInstance();
    int Day = calendar.get(Calendar.DAY_OF_MONTH);
    int Month = calendar.get(Calendar.MONTH);
    int Year = calendar.get(Calendar.YEAR);

    @SuppressLint("WrongViewCast") AutoCompleteTextView type, typeTransport;

    Button create;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission_form);
        realm = Realm.getDefaultInstance();

        backarrow = findViewById(R.id.backarrow);
        fin = findViewById(R.id.fin);
        debut = findViewById(R.id.debut);
        description = findViewById(R.id.description);
        create = findViewById(R.id.create);
        nomMission = findViewById(R.id.nomMission);
        adresse = findViewById(R.id.adresse);

        layoutfininsh = findViewById(R.id.layoutfininsh);
        layoutstart = findViewById(R.id.layoutstart);
        layoutdes = findViewById(R.id.layoutdes);
        layoutmission = findViewById(R.id.layoutmission);
        layouttype = findViewById(R.id.layouttype);
        layouttypeTransport = findViewById(R.id.layouttypeTransport);
        layoutAdresse = findViewById(R.id.layoutAdresse);

        fin.setInputType(InputType.TYPE_NULL);
        debut.setInputType(InputType.TYPE_NULL);

        adapter = new ArrayAdapter<String>(this, R.layout.list_type, R.id.textview, TYPE);
        adapter2 = new ArrayAdapter<String>(this, R.layout.list_type, R.id.textview, TYPE2);

        username = getIntent().getExtras().getString("USER_NAME");
        role = getIntent().getExtras().getString("ROLE");

        type = findViewById(R.id.type);
        typeTransport = findViewById(R.id.typeTransport);
        type.setAdapter(adapter);
        typeTransport.setAdapter(adapter2);

        backarrow.setOnClickListener(v -> {
            Intent intent = new Intent(MissionForm.this, MissionActivity.class);
            intent.putExtra("USER_NAME", username);
            intent.putExtra("ROLE", role);
            MissionForm.this.startActivity(intent);
            overridePendingTransition(R.anim.slide_left, R.anim.slide_out_right);
            finish();
        });


        fin.setOnClickListener(v -> datePicker(fin));

        debut.setOnClickListener(v -> datePicker(debut));

        int useId = getId(username);
        create.setOnClickListener(v -> {
            if (checkNotEmpty( fin,  debut, description, nomMission)) {
                addMission(useId);
                Intent intent = new Intent(MissionForm.this, MissionActivity.class);
                intent.putExtra("USER_NAME", username);
                intent.putExtra("ROLE", role);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
                finish();
            }
        });
    }

    private static final String[] TYPE = new String[] {
            "Organization of the Forum", "Academic Orientation", "Conference", "Discovery Day" , "Academic support" , "Exchange of Practices", "Presentation"
    };

    private static final String[] TYPE2 = new String[] {
            "Bus", "Train", "Airplane", "Taxi", "Transit"
    };

    public void datePicker(TextInputEditText edit){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this, new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month += 1;
                String date = dayOfMonth+"/"+month+"/"+year;
                edit.setText(date);
            }
        }, Year,Month,Day);
        datePickerDialog.show();
    }

    public int getId(String usernameText){
        User realmObjects = realm.where(User.class).equalTo("userName", usernameText).findFirst();
        return realmObjects.getUserId();
    }

    public void addMission(int userId){
        realm.executeTransactionAsync(bgRealm -> {
            Number maxId = bgRealm.where(Mission.class).max("missionId");

            int newId = (maxId == null) ? 1 : maxId.intValue() + 1;
            Mission mission = bgRealm.createObject(Mission.class, newId);
            mission.setUserId(userId);
            mission.setMissionName(nomMission.getText().toString());
            mission.setMissionType(type.getText().toString());
            mission.setDescription(description.getText().toString());
            mission.setAdresse(adresse.getText().toString());
            mission.setTypeTransport(typeTransport.getText().toString());

            try {
                mission.setDebutMission(new SimpleDateFormat("dd/MM/yyyy").parse(debut.getText().toString()));
                mission.setFinMission(new SimpleDateFormat("dd/MM/yyyy").parse(fin.getText().toString()));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if(role.equals("Directeur")) {
                mission.setEtat("onhold");
            }
            else if(role.equals("President")){
                mission.setEtat("finish");
            }
            else{
                mission.setEtat("start");
            }

        }, () -> {
            System.out.println("Success");

        }, error -> {
            error.printStackTrace();
        });
    }

    public boolean checkNotEmpty(TextInputEditText fin, TextInputEditText debut,TextInputEditText description,TextInputEditText nomMission){

        boolean state = true;
        if(nomMission.getText().toString().isEmpty()){
            layoutmission.setError("Name is Empty!");
            state = false;
        }
        if(description.getText().toString().isEmpty()){
            layoutdes.setError("Description is Empty!");
            state = false;
        }
        if(debut.getText().toString().isEmpty()){
            layoutstart.setError("Starting Date is Empty!");
            state = false;
        }
        if(fin.getText().toString().isEmpty()){
            layoutfininsh.setError("Finishing Date is Empty!");
            state = false;
        }
        if(typeTransport.getText().toString().isEmpty()){
            layouttypeTransport.setError("Transport is Empty!");
            state = false;
        }
        if(adresse.getText().toString().isEmpty()){
            layoutAdresse.setError("Address is Empty!");
            state = false;
        }
        return state;
    }
}