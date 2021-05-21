package com.project.searchproducts.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.project.searchproducts.models.ProductFavorite;

@Database(entities = {ProductFavorite.class}, version = 1, exportSchema = false)
public abstract class ProductDatabase extends RoomDatabase {
    private static final String DB_NAME = "product.db";
    private static ProductDatabase database;

    public static synchronized ProductDatabase getInstance(Context context) {
        if (database == null) {
            database = Room.databaseBuilder(context, ProductDatabase.class, DB_NAME)
                    .fallbackToDestructiveMigration().build();
        }
        return database;
    }

    public abstract ProductDao movieDao();
}
