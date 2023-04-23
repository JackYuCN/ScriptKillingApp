package com.competition.scriptkillingapp.model;


import java.util.Arrays;

public class Script {
    private String name;
    private String type;
    private String Score;
    private int people;
    private int hour;

    public Script() {
    }

    public Script(String name, String type, String score, int people, int hour) {
        this.name = name;
        this.type = type;
        Score = score;
        this.people = people;
        this.hour = hour;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getScore() {
        return Score;
    }

    public void setScore(String score) {
        Score = score;
    }

    public int getPeople() {
        return people;
    }

    public void setPeople(int people) {
        this.people = people;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Script{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", Score='" + Score + '\'' +
                ", people=" + people +
                ", hour=" + hour +
                '}';
    }
}
