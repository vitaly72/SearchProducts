package com.project.searchproducts.screens;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import com.project.searchproducts.R;
import com.project.searchproducts.adapters.GridSpacingItemDecoration;
import com.project.searchproducts.adapters.IOnClickListener;
import com.project.searchproducts.adapters.ProductAdapter;
import com.project.searchproducts.databinding.ActivitySearchBinding;
import com.project.searchproducts.models.Product;
import com.project.searchproducts.models.SeoLinks;
import com.project.searchproducts.network.WebParsing;
import com.project.searchproducts.utils.Constants;
import com.project.searchproducts.utils.JSONUtils;
import com.project.searchproducts.viewmodels.IOnClickListenerTag;
import com.project.searchproducts.viewmodels.ProductsViewModel;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchActivity extends AppCompatActivity implements
        IOnClickListenerTag,
        IOnClickListener {
    private ProductsViewModel productsViewModel;
    private ActivitySearchBinding searchBinding;
    private ProductAdapter productAdapter;
    private List<SeoLinks> seoLinks;

    @SuppressLint({"ResourceAsColor", "SetJavaScriptEnabled"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchBinding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        productsViewModel = ViewModelProviders.of(this).get(ProductsViewModel.class);
        searchBinding.setViewModel(productsViewModel);
        searchBinding.setLifecycleOwner(this);
        productsViewModel.setOnClickListenerTag(this);

        productAdapter = new ProductAdapter();
        productAdapter.setOnClickListener(this);
        searchBinding.recyclerViewMain.setLayoutManager(new GridLayoutManager(this, 2));
        searchBinding.recyclerViewMain.addItemDecoration(
                new GridSpacingItemDecoration(2, 40, false));
        searchBinding.recyclerViewMain.setAdapter(productAdapter);

        searchBinding.headerView.searchButton.setOnClickListener(v -> {
            searchBinding.progressIndicator.setVisibility(View.VISIBLE);
            String searchTerm = "JBL";
            productsViewModel.search(searchTerm);

            productsViewModel.getProductsData().observe(this, products -> {
                searchBinding.backgroundImageView.setVisibility(View.GONE);
                searchBinding.progressIndicator.setVisibility(View.GONE);
                searchBinding.mainScrollView.setVisibility(View.VISIBLE);

                seoLinks = products.get(0).getSeoLinks();
                searchBinding.tagsViewGroup.setTags(seoLinks);
                productAdapter.setProducts(products);
            });
        });
    }

    @Override
    public void onClick(Product product) {
        Intent intent = new Intent(SearchActivity.this, DetailsActivity.class);
        String movieJsonString = JSONUtils.getGsonParser().toJson(product);
        intent.putExtra(Constants.INTENT_KEY, movieJsonString);
        startActivity(intent);
    }

    @Override
    public void OnClickTag(int id) {
        searchBinding.progressIndicator.setVisibility(View.VISIBLE);
        productsViewModel.searchByTag(seoLinks.get(id).getLink());
        productAdapter.clear();
        seoLinks.clear();

        productsViewModel.getProductsData().observe(this, products -> {
            searchBinding.progressIndicator.setVisibility(View.GONE);
            seoLinks = products.get(0).getSeoLinks();
            searchBinding.tagsViewGroup.setTags(seoLinks);

            productAdapter.setProducts(products);
        });
    }
}

