package com.competition.scriptkillingapp.model;

public class Room {
    private String password;
    private boolean isOpenNow;
    private String openTime;

    public Room() {
    }

    public Room(String password, String TimeString) {
        this.password = password;
        if (TimeString.trim() == "即开房") {
            this.isOpenNow = true;
            this.openTime = null;
        } else {
            this.isOpenNow = false;
            this.openTime = TimeString;
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isOpenNow() {
        return isOpenNow;
    }

    public void setOpenNow(boolean openNow) {
        isOpenNow = openNow;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }
}
