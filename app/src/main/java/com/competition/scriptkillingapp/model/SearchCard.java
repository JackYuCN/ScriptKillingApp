package com.competition.scriptkillingapp.model;

public class SearchCard {
    private String title_up;
    private String title_down;
    private String info;
    private String imageUrl;

    public SearchCard(String title_up, String title_down, String info, String imageUrl) {
        this.title_up = title_up;
        this.title_down = title_down;
        this.info = info;
        this.imageUrl = imageUrl;
    }

    public String getTitle_up() {
        return title_up;
    }

    public void setTitle_up(String title_up) {
        this.title_up = title_up;
    }

    public String getTitle_down() {
        return title_down;
    }

    public void setTitle_down(String title_down) {
        this.title_down = title_down;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "SearchCard{" +
                "title_up='" + title_up + '\'' +
                ", title_down='" + title_down + '\'' +
                ", info='" + info + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
