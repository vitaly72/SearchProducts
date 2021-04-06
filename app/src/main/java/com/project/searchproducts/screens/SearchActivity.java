package com.project.searchproducts.screens;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import com.project.searchproducts.R;
import com.project.searchproducts.adapters.GridSpacingItemDecoration;
import com.project.searchproducts.adapters.ProductAdapter;
import com.project.searchproducts.databinding.ActivitySearchBinding;
import com.project.searchproducts.models.Product;
import com.project.searchproducts.utils.JSONUtils;
import com.project.searchproducts.viewmodels.ProductsViewModel;

public class SearchActivity extends AppCompatActivity {
    private ProductsViewModel productsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySearchBinding searchBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_search);
        productsViewModel = ViewModelProviders.of(this).get(ProductsViewModel.class);

        ProductAdapter productAdapter = new ProductAdapter();
        searchBinding.recyclerViewMain.setLayoutManager(new GridLayoutManager(this, 2));
        searchBinding.recyclerViewMain.addItemDecoration(
                new GridSpacingItemDecoration(2, 40, false));
        searchBinding.recyclerViewMain.setAdapter(productAdapter);

        searchBinding.searchButton.setOnClickListener(v -> {
//            String searchTerm = searchBinding.searchTextView.getText().toString();
            String searchTerm = "JBL";
//            productsViewModel.search(searchTerm);
//            productsViewModel.getProductsData().observe(this, productAdapter::setProducts);
        });

        productsViewModel.search("JBL");
        productsViewModel.getProductsData().observe(this, productAdapter::setProducts);

        productAdapter.setOnClickListener(product -> {
            Intent intent = new Intent(SearchActivity.this, DetailsActivity.class);
            String movieJsonString = JSONUtils.getGsonParser().toJson(product);
            intent.putExtra("product", movieJsonString);
            System.out.println("movieJsonString = " + movieJsonString);

            startActivity(intent);
        });
    }
}