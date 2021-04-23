package com.project.searchproducts.models;

import java.util.List;

public class Product {
    public String title;
    public String imageUrl;
    public String price;
    public int sale;
    public String presence;
    public String detailsLink;
    public List<SeoLinks> seoLinks;

    public Product(String title, String imageUrl, String price,
                   int sale, String presence, String detailsLink, List<SeoLinks> seoLinks) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.price = price;
        this.sale = sale;
        this.presence = presence;
        this.detailsLink = detailsLink;
        this.seoLinks = seoLinks;
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

    public List<SeoLinks> getSeoLinks() {
        return seoLinks;
    }
}
