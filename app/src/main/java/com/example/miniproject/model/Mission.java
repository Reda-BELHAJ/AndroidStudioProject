package com.example.miniproject.model;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Mission extends RealmObject {
    @PrimaryKey
    private int missionId;

    private int userId;
    private String missionName;
    private String missionType;
    private Date debutMission;
    private Date finMission;
    private String etat;
    // start : In process
    // onhold : On hold
    // finish : Successful
    private String description;

    private String adresse;
    private String typeTransport;

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTypeTransport() {
        return typeTransport;
    }

    public void setTypeTransport(String typeTransport) {
        this.typeTransport = typeTransport;
    }

    public int getMissionId() {
        return missionId;
    }

    public void setMissionId(int missionId) {
        this.missionId = missionId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getMissionName() {
        return missionName;
    }

    public void setMissionName(String missionName) {
        this.missionName = missionName;
    }

    public String getMissionType() {
        return missionType;
    }

    public void setMissionType(String missionType) {
        this.missionType = missionType;
    }

    public Date getDebutMission() {
        return debutMission;
    }

    public void setDebutMission(Date debutMission) {
        this.debutMission = debutMission;
    }

    public Date getFinMission() {
        return finMission;
    }

    public void setFinMission(Date finMission) {
        this.finMission = finMission;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
