package com.project.searchproducts.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "favourite_products")
public class ProductFavorite {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String title;
    public String price;
    public String presence;
    public String detailsLink;

    public ProductFavorite() {
    }

    @Ignore
    public ProductFavorite(int id, String title, String price, String presence, String detailsLink) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.presence = presence;
        this.detailsLink = detailsLink;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPresence() {
        return presence;
    }

    public void setPresence(String presence) {
        this.presence = presence;
    }

    public String getDetailsLink() {
        return detailsLink;
    }

    public void setDetailsLink(String detailsLink) {
        this.detailsLink = detailsLink;
    }
}
