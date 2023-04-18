package com.competition.scriptkillingapp.model;


public class Script {
    private String name;

    @Override
    public String toString() {
        return "Script{" +
                "name=" + name +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Script(String name) {
        this.name = name;
    }
}
