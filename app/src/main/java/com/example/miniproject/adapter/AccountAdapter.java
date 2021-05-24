package com.example.miniproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.miniproject.AccountActivity;
import com.example.miniproject.JavaMailAPI;
import com.example.miniproject.R;
import com.example.miniproject.model.Mission;
import com.example.miniproject.model.User;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

import io.realm.Realm;

public class AccountAdapter extends BaseAdapter {
    Realm realm;
    JavaMailAPI send;

    private Context context;
    private ArrayList<User> users;

    String user1;
    String role1;

    public AccountAdapter(Context context, ArrayList<User> users, String user1, String role1) {
        this.context = context;
        this.users = users;
        this.user1 = user1;
        this.role1 = role1;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        realm = Realm.getDefaultInstance();

        if(convertView == null){
            convertView = View.inflate(context, R.layout.card_user, null);
        }
        TextView username = convertView.findViewById(R.id.username);
        TextView role = convertView.findViewById(R.id.role);
        TextView fullname = convertView.findViewById(R.id.fullname);
        TextView mail = convertView.findViewById(R.id.mail);
        MaterialButton validateBtn = convertView.findViewById(R.id.validateBtn);

        User user = users.get(position);

        username.setText(user.getUserName());
        role.setText(user.getRole());
        fullname.setText(user.getFullName());
        mail.setText(user.getUserEmail());

        if(user.getEtat())
            validateBtn.setVisibility(View.GONE);

        validateBtn.setOnClickListener(v -> {
            if(! user.getEtat()) {
                updateUser(user, user.getUserId(), user.getUserName(), user.getPassword(), user.getUserEmail(), user.getFullName(), user.getRole());
                sendEmail(user.getUserEmail(), user.getFullName(), user.getUserName(), user.getPassword());
                update();
            }
        });

        return convertView;
    }

    private void updateUser(User modal, int id ,String username, String password, String mail, String fullname, String role) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                modal.setUserId(id);
                modal.setUserName(username);
                modal.setEtat(true);
                modal.setPassword(password);
                modal.setUserEmail(mail);
                modal.setFullName(fullname);
                modal.setRole(role);

                realm.copyToRealmOrUpdate(modal);
            }
        });
    }

    public void update(){
        AccountActivity ac = (AccountActivity) context;
        Intent intent = ac.getIntent();
        intent.putExtra("USER_NAME", user1);
        intent.putExtra("ROLE", role1);
        ac.finish();
        ac.startActivity(intent);
        ac.overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
    }

    public void sendEmail(String emailText ,String fullnameText,String usernameText,String passwordText){
        String message = "Hey " + fullnameText + ",\nWe are happy you signed up for Mini-Projet, your request has been verified." +
                "\nHere's your UserName: "+ usernameText + ",And Here's your PassWord: " + passwordText +
                "\nWelcome to Mini-Projet!"+
                "\nMini-Projet Team";
        send = new JavaMailAPI(context , emailText, "Email Validation", message);
        send.execute();
    }
}
