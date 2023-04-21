package com.competition.scriptkillingapp.model;

public class ScriptCharacter {
    private String name;
    private String intro;
    private String sex;

    public ScriptCharacter() {
    }

    public ScriptCharacter(String name, String intro, String sex) {
        this.name = name;
        this.intro = intro;
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "ScriptCharacter{" +
                "name='" + name + '\'' +
                ", intro='" + intro + '\'' +
                ", sex='" + sex + '\'' +
                '}';
    }
}
