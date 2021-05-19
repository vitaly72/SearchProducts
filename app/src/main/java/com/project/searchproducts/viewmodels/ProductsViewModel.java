package com.project.searchproducts.viewmodels;

import android.content.Intent;
import android.net.Uri;
import android.view.View;

import androidx.databinding.Bindable;
import androidx.databinding.Observable;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.searchproducts.adapters.IOnClickListener;
import com.project.searchproducts.models.Product;
import com.project.searchproducts.models.ProductDetails;
import com.project.searchproducts.models.SeoLinks;
import com.project.searchproducts.network.NetworkRepository;

import java.util.List;

public class ProductsViewModel extends ViewModel implements
        Observable,
        View.OnClickListener,
        ISearchProducts {
    private IOnClickListenerTag OnClickListenerTag;
    private MutableLiveData<List<Product>> productsData;
    private MutableLiveData<ProductDetails> productsDetailsData;

    public ObservableField<List<SeoLinks>> seoLinks = new ObservableField<>();

//    @Bindable
//    public boolean getIsLoad() {
//        return isLoad;
//    }

    private boolean isLoad = false;

    @Override
    public void search(String searchTerm) {
        if (productsData != null) return;
        productsData = NetworkRepository.getInstance().searchProducts(searchTerm);

        if (productsData.getValue() != null) {
            System.out.println("searchTerm null = " + searchTerm);
            seoLinks.set(productsData.getValue().get(0).getSeoLinks());
        }
    }

//    private void initProducts() {
//        productsData.observe((LifecycleOwner) this, observer -> {
//
//        });
//    }

    public void setOnClickListenerTag(IOnClickListenerTag OnClickListenerTag) {
        this.OnClickListenerTag = OnClickListenerTag;
    }

    public IOnClickListenerTag getOnClickListenerTag(){
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

    @Override
    public void onClick(View v) {
//        searchBinding.progressIndicator.setVisibility(View.VISIBLE);
//        searchByTag(seoLinks.get(0).getLink());
//        System.out.println("seoLinks = " + seoLinks.size());
//        productsViewModel.getProductsData().observe(this, products -> {
//            searchBinding.progressIndicator.setVisibility(View.GONE);
//            productAdapter.setProducts(products);
//            System.out.println("products = " + products.size());
//            seoLinks = products.get(0).getSeoLinks();
//            initTags(seoLinks);
//        });
    }
}
