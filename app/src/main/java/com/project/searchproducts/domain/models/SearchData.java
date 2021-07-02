package com.project.searchproducts.domain.models;

import com.project.searchproducts.utils.SortType;

public class SearchData {
    private String searchTerm;
    private String minPrice;
    private String maxPrice;
    private SortType sort;
    private String page;

    public SearchData(String searchTerm, String minPrice, String maxPrice, SortType sort, String page) {
        this.searchTerm = searchTerm;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.sort = sort;
        this.page = page;
    }

    public void nextPage() {
        int pageNext = Integer.parseInt(this.page);
        pageNext++;
        this.page = Integer.toString(pageNext);
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public String getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(String minPrice) {
        this.minPrice = minPrice;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }

    public SortType getSort() {
        return sort;
    }

    public void setSort(SortType sort) {
        this.sort = sort;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }
}
