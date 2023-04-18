package com.competition.scriptkillingapp.model;

public class ScriptTitle {
    private String name;
    private int char_male;
    private int char_female;

    public ScriptTitle() {
    }

    public ScriptTitle(String name, int char_male, int char_female) {
        this.name = name;
        this.char_male = char_male;
        this.char_female = char_female;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getChar_male() {
        return char_male;
    }

    public void setChar_male(int char_male) {
        this.char_male = char_male;
    }

    public int getChar_female() {
        return char_female;
    }

    public void setChar_female(int char_female) {
        this.char_female = char_female;
    }

    @Override
    public String toString() {
        return "Script{" +
                "name='" + name + '\'' +
                ", char_male=" + char_male +
                ", char_female=" + char_female +
                '}';
    }
}
