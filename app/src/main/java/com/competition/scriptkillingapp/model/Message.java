package com.competition.scriptkillingapp.model;

public class Message {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Message{" +
                "name='" + name + '\'' +
                '}';
    }

    public Message(String name) {
        this.name = name;
    }
}
