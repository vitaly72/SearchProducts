package com.project.searchproducts.screens;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.chip.Chip;
import com.project.searchproducts.R;
import com.project.searchproducts.adapters.GridSpacingItemDecoration;
import com.project.searchproducts.adapters.IOnClickListener;
import com.project.searchproducts.adapters.ProductAdapter;
import com.project.searchproducts.databinding.ActivitySearchBinding;
import com.project.searchproducts.models.Product;
import com.project.searchproducts.models.SeoLinks;
import com.project.searchproducts.network.NetworkRepository;
import com.project.searchproducts.network.NetworkService;
import com.project.searchproducts.utils.Constants;
import com.project.searchproducts.utils.JSONUtils;
import com.project.searchproducts.viewmodels.IOnClickListenerTag;
import com.project.searchproducts.viewmodels.ProductsViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity implements
        View.OnClickListener,
        IOnClickListenerTag,
        IOnClickListener {
    private ProductsViewModel productsViewModel;
    private ActivitySearchBinding searchBinding;
    private ProductAdapter productAdapter;
    private List<SeoLinks> seoLinks;

    @SuppressLint("ResourceAsColor")
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
//            String searchTag = "/ua/Kolonka-jbl-boombox.html";
//            productsViewModel.searchByTag(searchTag);

            productsViewModel.getProductsData().observe(this, products -> {
                searchBinding.progressIndicator.setVisibility(View.GONE);
                searchBinding.tagsViewGroup.tagsGroup.setVisibility(View.VISIBLE);
                productAdapter.setProducts(products);
                seoLinks = products.get(0).getSeoLinks();
                searchBinding.tagsViewGroup.setTags(seoLinks);
            });
        });

//        searchBinding.tagsViewGroup.tagTextView0.setOnClickListener(v -> {
////            searchBinding.progressIndicator.setVisibility(View.VISIBLE);
////            productsViewModel.searchByTag(seoLinks.get(0).getLink());
////            productAdapter.clear();
////            seoLinks.clear();
////
////            productsViewModel.getProductsData().observe(this, products -> {
////                searchBinding.progressIndicator.setVisibility(View.GONE);
////                productAdapter.setProducts(products);
////
////                seoLinks = products.get(0).getSeoLinks();
////                searchBinding.tagsViewGroup.setTags(seoLinks);
////            });
//        });
    }

    @Override
    public void onClick(View v) {

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
            productAdapter.setProducts(products);

            seoLinks = products.get(0).getSeoLinks();
            searchBinding.tagsViewGroup.setTags(seoLinks);
        });
    }
}