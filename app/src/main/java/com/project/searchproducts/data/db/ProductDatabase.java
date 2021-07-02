package com.project.searchproducts.data.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.project.searchproducts.domain.models.ProductFavorite;

@Database(entities = {ProductFavorite.class}, version = 2, exportSchema = false)
public abstract class ProductDatabase extends RoomDatabase {
    public abstract ProductDao productDao();
}
