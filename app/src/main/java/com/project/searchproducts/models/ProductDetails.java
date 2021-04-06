package com.project.searchproducts.models;

import java.util.HashMap;
import java.util.List;

public class ProductDetails {
    private final String link;
    private final List<String> imageLinks;
    private final HashMap<String, String> characteristic;
    
    public ProductDetails(String link, List<String> imageLinks, HashMap<String, String> characteristic) {
        this.link = link;
        this.imageLinks = imageLinks;
        this.characteristic = characteristic;
    }

    public String getLink() {
        return link;
    }

    public List<String> getImageLinks() {
        return imageLinks;
    }

    public HashMap<String, String> getCharacteristic() {
        return characteristic;
    }
}
