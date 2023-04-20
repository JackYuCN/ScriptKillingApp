package com.competition.scriptkillingapp.model;

public class User {
    private String id;
    private String username;
    private String imageURL;
    private boolean playing;
    private String gameIdx;

    public User() {
    }

    public User(String id, String username, String imageURL, boolean playing, String gameIdx) {
        this.id = id;
        this.username = username;
        this.imageURL = imageURL;
        this.playing = playing;
        this.gameIdx = gameIdx;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public String getGameIdx() {
        return gameIdx;
    }

    public void setGameIdx(String gameIdx) {
        this.gameIdx = gameIdx;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", playing=" + playing +
                ", roomIdx='" + gameIdx + '\'' +
                '}';
    }
}
