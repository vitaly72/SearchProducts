package com.project.searchproducts.di;

import android.app.Application;

import androidx.room.Room;

import com.project.searchproducts.data.db.ProductDao;
import com.project.searchproducts.data.db.ProductDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class DataBaseModule {
    @Provides
    @Singleton
    public static ProductDatabase providePokemonDB(Application application) {
        return Room.databaseBuilder(application, ProductDatabase.class, "product.db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }

    @Provides
    @Singleton
    public static ProductDao provideProductDao(ProductDatabase productDatabase) {
        return productDatabase.productDao();
    }
}