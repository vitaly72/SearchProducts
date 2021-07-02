package com.project.searchproducts.presentation.details;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.project.searchproducts.R;
import com.project.searchproducts.databinding.ActivityProductBinding;
import com.project.searchproducts.domain.models.Product;
import com.project.searchproducts.utils.Constants;
import com.project.searchproducts.utils.JSONUtils;
import com.project.searchproducts.presentation.home.ProductsViewModel;

public class DetailsActivity extends AppCompatActivity {
    /**
     * Отримує об'єкт класу Product та виконує запит для детальної інформації
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityProductBinding productBinding = DataBindingUtil
                .setContentView(this, R.layout.activity_product);
        ProductsViewModel productsViewModel = ViewModelProviders
                .of(this)
                .get(ProductsViewModel.class);

        Intent intent = getIntent();
        String movieJsonString = intent.getStringExtra(Constants.INTENT_KEY);
        Product product = JSONUtils.getGsonParser().fromJson(movieJsonString, Product.class);
        productBinding.setProduct(product);

        System.out.println("detail link = " + product.getDetailsLink());

        productBinding.textViewOpenBrowser.setOnClickListener(v -> {
            String link = Constants.BASE.URL + product.getDetailsLink().replace("/ua", "ua").split("\\?token")[0];
            System.out.println("link = " + link);
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
            startActivity(browserIntent);
        });

        productsViewModel.detailsProduct(product.getDetailsLink());
        productBinding.progressIndicatorProduct.setVisibility(View.VISIBLE);
        productsViewModel.getProductsDetailsData().observe(this, response -> {
            if (response != null) {
                productBinding.progressIndicatorProduct.setVisibility(View.GONE);
                ViewPagerAdapter adapter = new ViewPagerAdapter(this, response.getImageLinks());
                productBinding.viewPager.setAdapter(adapter);
                productBinding.dotsIndicator.setViewPager(productBinding.viewPager);

                System.out.println("response.getDescriptions() = " + response.getDescriptions());
                String descriptions = response.getDescriptions().replace(". ", ".\n");
                productBinding.textViewDescriptions.setText(descriptions);
            }
        });
    }
}