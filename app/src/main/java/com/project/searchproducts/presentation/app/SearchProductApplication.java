package com.project.searchproducts.presentation.app;

import android.app.Application;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class SearchProductApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }
}
