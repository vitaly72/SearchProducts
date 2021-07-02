package com.project.searchproducts.presentation.details;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.project.searchproducts.R;
import com.project.searchproducts.databinding.ActivityProductBinding;
import com.project.searchproducts.domain.models.Product;
import com.project.searchproducts.presentation.home.ProductsViewModel;
import com.project.searchproducts.utils.Constants;
import com.project.searchproducts.utils.JSONUtils;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DetailsActivity extends AppCompatActivity {
    private ActivityProductBinding productBinding;
    private ProductsViewModel productsViewModel;
    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productBinding = DataBindingUtil.setContentView(this, R.layout.activity_product);
        productsViewModel = new ViewModelProvider(this).get(ProductsViewModel.class);

        product = getProductFromIntent();
        productBinding.setProduct(product);
        initProductsDetails();
        productBinding.textViewOpenBrowser.setOnClickListener(this::onClickDetails);
    }

    private Product getProductFromIntent() {
        String movieJsonString = getIntent().getStringExtra(Constants.INTENT_KEY);
        return JSONUtils.getGsonParser().fromJson(movieJsonString, Product.class);
    }

    private void onClickDetails(View view) {
        String link = Constants.BASE.URL + product.getDetailsLink()
                .replace("/ua", "ua")
                .split("\\?token")[0];

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        startActivity(browserIntent);
    }

    private void initProductsDetails() {
        productsViewModel.detailsProduct(product.getDetailsLink());
        productBinding.progressIndicatorProduct.setVisibility(View.VISIBLE);

        productsViewModel.getProductsDetailsData().observe(this, response -> {
            if (response != null) {
                productBinding.progressIndicatorProduct.setVisibility(View.GONE);
                ViewPagerAdapter adapter = new ViewPagerAdapter(this, response.getImageLinks());
                productBinding.viewPager.setAdapter(adapter);
                productBinding.dotsIndicator.setViewPager(productBinding.viewPager);

                String descriptions = response.getDescriptions().replace(". ", ".\n");
                productBinding.textViewDescriptions.setText(descriptions);
            }
        });
    }
}