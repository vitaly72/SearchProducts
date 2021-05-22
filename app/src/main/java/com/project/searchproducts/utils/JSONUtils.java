package com.project.searchproducts.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JSONUtils {
    private static Gson gson;

    /**
     * Повертає об'єкт класу GsonBuilder
     * @return
     */
    public static Gson getGsonParser() {
        if (null == gson) {
            GsonBuilder builder = new GsonBuilder();
            gson = builder.create();
        }
        return gson;
    }
}
