package com.project.searchproducts.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.project.searchproducts.domain.models.ProductFavorite;

import java.util.List;

@Dao
public interface ProductDao {
    @Query("SELECT * FROM favourite_products")
    LiveData<List<ProductFavorite>> getAllFavouriteProducts();

    @Query("SELECT * FROM favourite_products WHERE id == :id")
    ProductFavorite getFavouriteProductById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFavouriteProduct(ProductFavorite favouriteProduct);

    @Query("DELETE FROM favourite_products WHERE id = :id")
    void deleteFavouriteProductById(int id);

    @Query("SELECT EXISTS(SELECT * FROM favourite_products WHERE id = :id)")
    Boolean isExist(int id);

    @Delete
    void deleteFavouriteProduct(ProductFavorite favouriteProduct);
}
