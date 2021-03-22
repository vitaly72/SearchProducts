package com.project.searchproducts.models;

public class Product {
    String title;
    String imageUrl;
    String price;
    int sale;
    String presence;
    String detailsLink;

    public Product(String title, String imageUrl, String price,
                   int sale, String presence, String detailsLink) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.price = price;
        this.sale = sale;
        this.presence = presence;
        this.detailsLink = detailsLink;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getPrice() {
        return price;
    }

    public int getSale() {
        return sale;
    }

    public String getPresence() {
        return presence;
    }

    public String getDetailsLink() {
        return detailsLink;
    }
}
