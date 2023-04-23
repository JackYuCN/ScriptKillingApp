package com.competition.scriptkillingapp.model;

public class Profile {
    String username;

    String imageURL;

    public Profile() {
    }

    public Profile(String username, String imageURL) {
        this.username = username;
        this.imageURL = imageURL;
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

    public void setImageURL(String imageUrl) {
        this.imageURL = imageUrl;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "username='" + username + '\'' +
                ", imageURL='" + imageURL + '\'' +
                '}';
    }
}
