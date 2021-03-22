package com.project.searchproducts.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.searchproducts.models.Product;
import com.project.searchproducts.models.ProductDetails;
import com.project.searchproducts.network.NetworkRepository;

import java.util.List;

public class ProductsViewModel extends ViewModel {
    private MutableLiveData<List<Product>> productsData;
    private MutableLiveData<ProductDetails> productsDetailsData;

    public void search(String searchTerm) {
        if (productsData != null) return;
        productsData = NetworkRepository.getInstance().searchProducts(searchTerm);
    }

    public void detailsProduct(String productId) {
        if (productsDetailsData != null) return;
        productsDetailsData = NetworkRepository.getInstance().detailsProduct(productId);
    }

    public MutableLiveData<List<Product>> getProductsData() {
        return productsData;
    }

    public MutableLiveData<ProductDetails> getProductsDetailsData() {
        return productsDetailsData;
    }
}
