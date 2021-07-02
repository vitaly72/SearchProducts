package com.project.searchproducts.presentation.favorite;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.project.searchproducts.data.db.ProductDao;
import com.project.searchproducts.domain.models.ProductFavorite;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class FavoriteProductViewModel extends ViewModel {
    private final ProductDao productDao;

    @Inject
    public FavoriteProductViewModel(ProductDao productDao) {
        this.productDao = productDao;
    }

    public ProductFavorite getFavouriteProductByID(int id) {
        return productDao.getFavouriteProductById(id);
    }

    public LiveData<List<ProductFavorite>> getFavouriteProducts() {
        return productDao.getAllFavouriteProducts();
    }

    public void insertFavouriteProduct(ProductFavorite productFavorite) {
        productDao.insertFavouriteProduct(productFavorite);
    }

    public void deleteFavouriteProduct(ProductFavorite productFavorite) {
        productDao.deleteFavouriteProduct(productFavorite);
    }

    public void deleteFavouriteProduct(int id) {
        productDao.deleteFavouriteProductById(id);
    }
}
