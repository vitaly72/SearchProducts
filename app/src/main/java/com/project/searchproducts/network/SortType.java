package com.project.searchproducts.network;

public enum SortType {
    SCORE("-score"), MAX("-price"), MIN("price");

    private final String text;

    SortType(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
