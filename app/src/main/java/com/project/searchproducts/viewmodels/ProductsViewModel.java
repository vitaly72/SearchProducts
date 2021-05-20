package com.project.searchproducts.viewmodels;

import androidx.databinding.Observable;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.searchproducts.models.Product;
import com.project.searchproducts.models.ProductDetails;
import com.project.searchproducts.models.SeoLinks;
import com.project.searchproducts.network.NetworkRepository;
import com.project.searchproducts.network.SortType;

import java.util.List;

public class ProductsViewModel extends ViewModel implements Observable, ISearchProducts {
    private IOnClickListenerTag OnClickListenerTag;
    private MutableLiveData<List<Product>> productsData;
    private MutableLiveData<ProductDetails> productsDetailsData;
    public ObservableField<List<SeoLinks>> seoLinks = new ObservableField<>();

    @Override
    public void search(String searchTerm, String minPrice, String maxPrice, SortType sort) {
//        if (productsData != null) return;
        productsData = NetworkRepository.getInstance().searchProducts(searchTerm, minPrice, maxPrice, sort);

        if (productsData.getValue() != null) {
            System.out.println("searchTerm null = " + searchTerm);
            seoLinks.set(productsData.getValue().get(0).getSeoLinks());
        }
    }

    public void setOnClickListenerTag(IOnClickListenerTag OnClickListenerTag) {
        this.OnClickListenerTag = OnClickListenerTag;
    }

    public IOnClickListenerTag getOnClickListenerTag() {
        return this.OnClickListenerTag;
    }

    @Override
    public void searchByTag(String tag) {
        productsData = NetworkRepository.getInstance().searchProductByTag(tag);
    }

    public void detailsProduct(String productId) {
        productsDetailsData = NetworkRepository.getInstance().detailsProduct(productId);
    }

    public MutableLiveData<List<Product>> getProductsData() {
        return productsData;
    }

    public MutableLiveData<ProductDetails> getProductsDetailsData() {
        return productsDetailsData;
    }

    @Override
    public void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
    }

    @Override
    public void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
    }
}
