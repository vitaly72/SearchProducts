package com.project.searchproducts.models;

import java.util.HashMap;
import java.util.List;

public class ProductDetails {
    private final String link;
    private final List<String> imageLinks;
    private final HashMap<String, String> characteristic;
    private final String descriptions;

    public ProductDetails(String link, List<String> imageLinks,
                          HashMap<String, String> characteristic, String descriptions) {
        this.link = link;
        this.imageLinks = imageLinks;
        this.characteristic = characteristic;
        this.descriptions = descriptions;
    }

    public String getLink() {
        return link;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public List<String> getImageLinks() {
        return imageLinks;
    }

    public HashMap<String, String> getCharacteristic() {
        return characteristic;
    }
}
