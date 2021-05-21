package com.project.searchproducts.network;

import org.jetbrains.annotations.NotNull;

public enum SortType {
    SCORE("-score"), MAX("-price"), MIN("price");

    private final String text;

    SortType(final String text) {
        this.text = text;
    }

    @NotNull
    @Override
    public String toString() {
        return text;
    }
}
