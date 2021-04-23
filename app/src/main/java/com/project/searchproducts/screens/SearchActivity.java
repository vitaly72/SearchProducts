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

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.chip.Chip;
import com.project.searchproducts.R;
import com.project.searchproducts.adapters.GridSpacingItemDecoration;
import com.project.searchproducts.adapters.ProductAdapter;
import com.project.searchproducts.databinding.ActivitySearchBinding;
import com.project.searchproducts.models.Product;
import com.project.searchproducts.models.SeoLinks;
import com.project.searchproducts.utils.Constants;
import com.project.searchproducts.utils.JSONUtils;
import com.project.searchproducts.viewmodels.ProductsViewModel;

import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private ProductsViewModel productsViewModel;
    private ActivitySearchBinding searchBinding;
    private ProductAdapter productAdapter;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchBinding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        productsViewModel = ViewModelProviders.of(this).get(ProductsViewModel.class);

        productAdapter = new ProductAdapter();
        searchBinding.recyclerViewMain.setLayoutManager(new GridLayoutManager(this, 2));
        searchBinding.recyclerViewMain.addItemDecoration(
                new GridSpacingItemDecoration(2, 40, false));
        searchBinding.recyclerViewMain.setAdapter(productAdapter);

        searchBinding.searchButton.setOnClickListener(v -> {
            searchBinding.progressIndicator.setVisibility(View.VISIBLE);
//            String searchTerm = searchBinding.searchTextView.getText().toString();
            String searchTerm = "JBL";
            productsViewModel.search(searchTerm);
            productsViewModel.getProductsData().observe(this, products -> {
                searchBinding.progressIndicator.setVisibility(View.GONE);
                productAdapter.setProducts(products);
                initTags(products);
            });
        });

//        searchBinding.progressIndicator.setVisibility(View.VISIBLE);
//        String searchTerm = "ua/Kolonka-dlya-muzyki.html";
//        productsViewModel.searchByTag(searchTerm);
//        productsViewModel.getProductsData().observe(this, products -> {
//            searchBinding.progressIndicator.setVisibility(View.GONE);
//            productAdapter.setProducts(products);
//            initTags(products);
//        });

        //Button tag1
        //Button tag2
        //Button tag3
        //Button tag4
        //Button tag5

        productAdapter.setOnClickListener(product -> {
            Intent intent = new Intent(SearchActivity.this, DetailsActivity.class);
            String movieJsonString = JSONUtils.getGsonParser().toJson(product);
            intent.putExtra(Constants.INTENT_KEY, movieJsonString);
            startActivity(intent);
        });
    }

    @SuppressLint("ResourceAsColor")
    private void initTags(List<Product> products) {
        if (products.size() == 0) return;
        for (SeoLinks seoLinks : products.get(0).getSeoLinks()) {
            System.out.println("seoLinks.getTitle() = " + seoLinks.getTitle());
            System.out.println("seoLinks.getTitle() = " + seoLinks.getLink());
            Button tag = new Button(this);
            tag.setOnClickListener(v -> {
                searchBinding.progressIndicator.setVisibility(View.VISIBLE);
                productsViewModel.searchByTag(seoLinks.getLink());
                productsViewModel.getProductsData().observe(this, response -> {
                    searchBinding.progressIndicator.setVisibility(View.GONE);
                    productAdapter.clear();
                    productAdapter.setProducts(response);
                    searchBinding.viewGroupSeoLinks.removeAllViews();
                    System.out.println("response.size() = " + response.size());
                    initTags(response);
                });
            });
            tag.setText(seoLinks.getTitle());
            tag.setTextSize(12);
            tag.setTextColor(getResources().getColor(R.color.dark));
//            tag.setTextColor(Color.parseColor("#006D77"));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(10, 0, 5, 0);
            tag.setLayoutParams(params);
            tag.setAllCaps(false);
            tag.setPadding(10, 0, 10, 0);
            tag.setHeight(30);
            tag.setBackgroundResource(R.drawable.tags_background);
//            tag.setBackgroundColor(R.color.dark);
            tag.setTextColor(getResources().getColor(R.color.white));

            searchBinding.viewGroupSeoLinks.addView(tag);
        }
    }
}