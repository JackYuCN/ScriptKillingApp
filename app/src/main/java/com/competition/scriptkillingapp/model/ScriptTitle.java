package com.competition.scriptkillingapp.model;

public class ScriptTitle {
    private String name;
    private int char_cnt;

    public ScriptTitle() {
    }

    public ScriptTitle(String name, int char_cnt) {
        this.name = name;
        this.char_cnt = char_cnt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getChar_cnt() {
        return char_cnt;
    }

    public void setChar_cnt(int char_cnt) {
        this.char_cnt = char_cnt;
    }

    @Override
    public String toString() {
        return "Script{" +
                "name='" + name + '\'' +
                ", char_cnt=" + char_cnt +
                '}';
    }
}
