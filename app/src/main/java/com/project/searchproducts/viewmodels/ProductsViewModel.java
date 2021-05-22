package com.project.searchproducts.viewmodels;

import androidx.databinding.Observable;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.searchproducts.models.Product;
import com.project.searchproducts.models.ProductDetails;
import com.project.searchproducts.models.SearchData;
import com.project.searchproducts.models.SeoLinks;
import com.project.searchproducts.network.NetworkRepository;

import java.util.List;

public class ProductsViewModel extends ViewModel implements Observable, ISearchProducts {
    private IOnClickListenerTag OnClickListenerTag;
    private MutableLiveData<List<Product>> productsData;
    private MutableLiveData<ProductDetails> productsDetailsData;
    public ObservableField<List<SeoLinks>> seoLinks = new ObservableField<>();

    /**
     * Виконує запит для пошуку товару
     * @param searchData
     */
    @Override
    public void search(SearchData searchData) {
        productsData = NetworkRepository.getInstance().searchProducts(searchData);

        if (productsData.getValue() != null) {
            seoLinks.set(productsData.getValue().get(0).getSeoLinks());
        }
    }


    public void setOnClickListenerTag(IOnClickListenerTag OnClickListenerTag) {
        this.OnClickListenerTag = OnClickListenerTag;
    }

    public IOnClickListenerTag getOnClickListenerTag() {
        return this.OnClickListenerTag;
    }

    /**
     * Пошуку товарів по тегу
     * @param tag
     */
    @Override
    public void searchByTag(String tag) {
        productsData = NetworkRepository.getInstance().searchProductByTag(tag);
    }

    /**
     * Виконує запит для детальної інформації про товар
     * @param productId
     */
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
