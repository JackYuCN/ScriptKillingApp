package com.competition.scriptkillingapp.model;

import java.util.ArrayList;

public class RoomSetting {
    private String password;
    private String openTime;
    private ArrayList<String> players = new ArrayList<>();
    private String gameRef;
    private String roomRef;

    public RoomSetting() {
    }

    public RoomSetting(String password, String TimeString, String gameRef, String roomRef) {
        this.password = password;
        if (TimeString.trim() == "即开房") {
            this.openTime = null;
        } else {
            this.openTime = TimeString;
        }
        this.gameRef = gameRef;
        this.roomRef = roomRef;
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

    public String getGameRef() {
        return gameRef;
    }

    public void setGameRef(String gamesRef) {
        this.gameRef = gamesRef;
    }

    public String getRoomRef() {
        return roomRef;
    }

    public void setRoomRef(String roomRef) {
        this.roomRef = roomRef;
    }

    @Override
    public String toString() {
        return "RoomSetting{" +
                "password='" + password + '\'' +
                ", openTime='" + openTime + '\'' +
                ", players=" + players +
                ", gameRef='" + gameRef + '\'' +
                ", roomRef='" + roomRef + '\'' +
                '}';
    }
}
