package com.project.searchproducts.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.project.searchproducts.models.Product;
import com.project.searchproducts.models.ProductFavorite;

import java.util.List;

@Dao
public interface ProductDao {
    @Query("SELECT * FROM favourite_products")
    LiveData<List<ProductFavorite>> getAllFavouriteProducts();

    @Query("SELECT * FROM favourite_products WHERE id == :id")
    ProductFavorite getFavouriteProductById(int id);

    @Insert
    void insertFavouriteProduct(ProductFavorite favouriteProduct);

    @Query("DELETE FROM favourite_products WHERE id = :id")
    void deleteFavouriteProductById(int id);

    @Delete
    void deleteFavouriteProduct(ProductFavorite favouriteProduct);
}
