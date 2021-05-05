package com.example.miniproject.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class User extends RealmObject {

    @PrimaryKey
    private int userId;

    private String userEmail;
    private String fullName;
    private String userName;
    private String password;
    private String role;

//    Professeur
//    ResponsableRH
//    Superviseur

    private boolean etat;

    public boolean getEtat() { return etat; }

    public void setEtat(boolean etat) { this.etat = etat; }

    public String getRole() { return role; }

    public void setRole(String role) { this.role = role; }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userEmail='" + userEmail + '\'' +
                ", fullName='" + fullName + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
