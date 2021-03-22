package com.project.searchproducts.models;

import java.util.List;

public class ProductDetails {
    private String link;
    private List<String> imageLinks;

    public ProductDetails(String link, List<String> imageLinks) {
        this.link = link;
        this.imageLinks = imageLinks;
    }

    public String getLink() {
        return link;
    }

    public List<String> getImageLinks() {
        return imageLinks;
    }
}
