package com.project.searchproducts.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.searchproducts.models.Product;
import com.project.searchproducts.network.NetworkRepository;

import java.util.List;

public class ProductsViewModel extends ViewModel {
    private MutableLiveData<List<Product>> productsData;

    public void search(String searchTerm) {
        if (productsData != null) return;
        productsData = NetworkRepository.getInstance().searchProducts(searchTerm);
    }

    public MutableLiveData<List<Product>> getProductsData() {
        return productsData;
    }
}
