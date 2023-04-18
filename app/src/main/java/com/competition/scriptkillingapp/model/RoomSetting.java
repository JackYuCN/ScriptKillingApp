package com.competition.scriptkillingapp.model;

import java.util.ArrayList;

public class RoomSetting {
    private String password;
    private String openTime;
    private ArrayList<String> players;

    public RoomSetting() {
    }

    public RoomSetting(String password, String TimeString, String roomOwnerUid) {
        this.players = new ArrayList<>();
        this.players.add(roomOwnerUid);
        this.password = password;
        if (TimeString.trim() == "即开房") {
            this.openTime = null;
        } else {
            this.openTime = TimeString;
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public ArrayList<String> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<String> players) {
        this.players = players;
    }
}
