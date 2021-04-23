package com.project.searchproducts.models;

public class SeoLinks {
    public String title;
    public String link;

    public SeoLinks(String title, String link) {
        this.title = title;
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }
}
