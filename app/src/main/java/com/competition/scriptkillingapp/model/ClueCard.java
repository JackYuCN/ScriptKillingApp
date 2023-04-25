package com.competition.scriptkillingapp.model;

public class ClueCard {
    private String title1;
    private String title2;
    private String imageUrl;

    public ClueCard(String title1, String title2, String imageUrl) {
        this.title1 = title1;
        this.title2 = title2;
        this.imageUrl = imageUrl;
    }

    public String getTitle1() {
        return title1;
    }

    public void setTitle1(String title1) {
        this.title1 = title1;
    }

    public String getTitle2() {
        return title2;
    }

    public void setTitle2(String title2) {
        this.title2 = title2;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "ClueCard{" +
                "title1='" + title1 + '\'' +
                ", title2='" + title2 + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
